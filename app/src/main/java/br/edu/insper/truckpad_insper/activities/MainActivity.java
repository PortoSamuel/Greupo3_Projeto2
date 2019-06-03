package br.edu.insper.truckpad_insper.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import br.edu.insper.truckpad_insper.api.client.Client;
import br.edu.insper.truckpad_insper.R;


public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter, arrayAdapterOrigin, arrayAdapterDestiny;
    private ListView sideBarListView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private static Spinner spinnerTruckLoad, spinnerAxleNumbers;
    private AutoCompleteTextView textOrigin, textDestiny;
    private Button validateButton;
    private Client client;
    private String result, distance;
    private static TextView textResult, textValueIntro, textDistance, loadingTxt, textResultReturn, textResultNumber, textReturnResultNumber, textResultDistance, textGas, textResultGas, textToll, textResultToll;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private static ProgressBar pBar;
    private boolean originPerformed, destinyPerformed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sideBarListView = findViewById(R.id.sideBarList);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        spinnerTruckLoad = findViewById(R.id.spinnerTruckLoad);
        spinnerAxleNumbers = findViewById(R.id.spinnerAxleNumbers);
        textOrigin = findViewById(R.id.editTextOrigin);
        textDestiny = findViewById(R.id.editTextDestiny);
        validateButton = findViewById(R.id.buttonValidate);

        //bottomSheet
        bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        textResult = findViewById(R.id.resultText);
        textResultReturn = findViewById(R.id.resultTextReturn);
        textResultNumber = findViewById(R.id.resultTextNumber);
        textReturnResultNumber = findViewById(R.id.resultReturnTextNumber);
        textDistance = findViewById(R.id.distanceText);
        textResultDistance = findViewById(R.id.resultTextDistance);
        textGas = findViewById(R.id.gasText);
        textResultGas = findViewById(R.id.resultGas);
        textToll = findViewById(R.id.tollText);
        textResultToll = findViewById(R.id.resultToll);
        pBar = findViewById(R.id.pBar);
        textValueIntro = findViewById(R.id.textIntroduction);
        loadingTxt = findViewById(R.id.loadingTxt);

        ViewGroup.LayoutParams childLayoutParams = bottomSheet.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        childLayoutParams.height = displayMetrics.heightPixels;
        bottomSheet.setLayoutParams(childLayoutParams);
        setStateBottomSheet(5);

        client = new Client(this);
        Handler handler = new Handler();


        setupSideBar();
        setSupportActionBar(toolbar);
        setupSpinners();

        sideBarListView.setOnItemClickListener((parent, view, position ,id) ->{
            switch (position){
                case 0:
                    drawer.closeDrawers();
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, HelpActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
            }
        });

        textOrigin.setOnClickListener((view) -> {
            if(getOrigin().length() != 0){
                client.setOriginCompleted(true);
                client.getAddress(getOrigin());
                textDestiny.requestFocus();
                setOriginPerformed(true);

            }else{showToast("O endereço de origem não pode ser vazio");}   
            
        });

        textDestiny.setOnClickListener((view) -> {
            if(getDestiny().length() != 0){
                client.setDestinyCompleted(true);
                client.getAddress(getDestiny());
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                setDestinyPerformed(true);
            }else{showToast("O endereço de destino não pode ser vazio");}

        });

        validateButton.setOnClickListener((view) -> {
            if(getDestiny().length() != 0 && getOrigin().length() != 0) {
                client.setAxisNumber(getAxleNumbers());
                client.setLoadType(getTruckLoad());
                client.postAddress();
                setStateBottomSheet(3);
                setAllState("none");
            }else{ showToast("Preencha todos os campos"); }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.arrowDown).setRotation(slideOffset * 180);
                findViewById(R.id.arrowUp).setRotation(slideOffset * -180);
            }
        });

        textOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setupAutoCompleteOrigin();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setOriginPerformed(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client.setOriginPlaced(true);
                        String text = textOrigin.getText().toString();
                        try {
                            client.getAddress(text);
                            setupAutoCompleteOrigin();
                        } catch(Exception e){

                        }
                    }
                }, 1200);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        textDestiny.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setupAutoCompleteDestiny();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setDestinyPerformed(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client.setDestinyPlaced(true);
                        String text = textDestiny.getText().toString();
                        try {
                            client.getAddress(text);
                            setupAutoCompleteDestiny();
                        } catch(Exception e){ }
                    }
                }, 1200);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public String getOrigin(){ return textOrigin.getText().toString(); }

    public String getDestiny(){ return textDestiny.getText().toString(); }

    public String getResult(){ return result; }

    public String getTruckLoad() { return spinnerTruckLoad.getSelectedItem().toString().toLowerCase(); }

    public int getAxleNumbers() { return Integer.parseInt(spinnerAxleNumbers.getSelectedItem().toString()); }


    public String getDistance() { return distance; }

    @SuppressLint("SetTextI18n")
    public void setAllState(String state) {
        if(state.equals("showResult")){

            //loading
            pBar.setVisibility(View.GONE);
            loadingTxt.setVisibility(View.GONE);

            textValueIntro.setVisibility(View.VISIBLE);

            //distance
            textDistance.setVisibility(View.VISIBLE);
            textDistance.setText("     Distância:");

            textResultDistance.setVisibility(View.VISIBLE);
            textResultDistance.setText(getDistance() + " km");

            //gas
            textGas.setVisibility(View.VISIBLE);
            textGas.setText("     Combustível");

            textResultGas.setVisibility(View.VISIBLE);
            textResultGas.setText("R$ 00,00");

            //toll
            textToll.setVisibility(View.VISIBLE);
            textToll.setText("     Pedagio:");

            textResultToll.setVisibility(View.VISIBLE);
            textResultToll.setText("R$ 00,00");

            //result
            textResult.setVisibility(View.VISIBLE);
            textResult.setText("Valor sem retorno:");

            textResultReturn.setVisibility(View.VISIBLE);
            textResultReturn.setText("Valor com retorno:");

            textResultNumber.setVisibility(View.VISIBLE);
            textResultNumber.setText("R$ " + getResult());

            textReturnResultNumber.setVisibility(View.VISIBLE);
            textReturnResultNumber.setText("R$ " + getResult());


        }else{
            pBar.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.VISIBLE);

            textDistance.setVisibility(View.GONE);
            textValueIntro.setVisibility(View.GONE);
            textResult.setVisibility(View.GONE);
        }

    }

    public void setOnResponsePrice(double price, double dist) {
        this.result = String.valueOf(price);
        this.distance = String.valueOf(dist);
    }

    public void setStateBottomSheet(int state){ bottomSheetBehavior.setState(state); }

    private void setupAutoCompleteOrigin(){
        try {
            arrayAdapterOrigin = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, client.getOriginPlaces());
            textOrigin.setAdapter(arrayAdapterOrigin);
            if(!originPerformed){
                textOrigin.showDropDown();
            }


        } catch(Exception e){ }
    }

    private void setupAutoCompleteDestiny(){
        try {
            arrayAdapterDestiny = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, client.getDestinyPlaces());
            textDestiny.setAdapter(arrayAdapterDestiny);
            if(!destinyPerformed){
                textDestiny.showDropDown();
            }
        } catch(Exception e){ }
    }


    private void setupSpinners(){
        //Truckload
        ArrayAdapter<CharSequence> truckLoadAdapter = ArrayAdapter.createFromResource(this, R.array.truckload, android.R.layout.simple_spinner_item);
        truckLoadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTruckLoad.setAdapter(truckLoadAdapter);

        //AxleNumbers
        ArrayAdapter<CharSequence> axleNumbersAdapter = ArrayAdapter.createFromResource(this, R.array.axlesNumber, android.R.layout.simple_spinner_item);
        axleNumbersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAxleNumbers.setAdapter(axleNumbersAdapter);

    }


    private void setupSideBar() {
        String[] sideBarOptions = {"Home", "Ajuda", "Sobre"};
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sideBarOptions);
        sideBarListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.actionSideBar:
                drawer.openDrawer(Gravity.RIGHT);
        }return true; }

    public void showToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void setOriginPerformed(boolean b){ this.originPerformed = b; }

    public void setDestinyPerformed(boolean b){ this.destinyPerformed = b; }
}