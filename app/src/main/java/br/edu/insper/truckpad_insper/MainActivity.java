package br.edu.insper.truckpad_insper;

import android.content.Context;
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


public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter, arrayAdapterOrigin, arrayAdapterDestiny;
    private ListView sideBarListView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private static Spinner spinnerTruckLoad, spinnerAxleNumbers, spinnerReturnLoad;
    private AutoCompleteTextView textOrigin, textDestiny;
    private Button validateButton;
    private Client client;
    private String result, distance;
    private static TextView textResult, textValueIntro, textType, textDistance, loadingTxt;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private static ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sideBarListView = findViewById(R.id.sideBarList);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        spinnerTruckLoad = findViewById(R.id.spinnerTruckLoad);
        spinnerAxleNumbers = findViewById(R.id.spinnerAxleNumbers);
        spinnerReturnLoad = findViewById(R.id.spinnerReturnLoad);
        textOrigin = findViewById(R.id.editTextOrigin);
        textDestiny = findViewById(R.id.editTextDestiny);
        validateButton = findViewById(R.id.buttonValidate);

        //bottomSheet
        bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        textResult = findViewById(R.id.resultTxt);
        textType = findViewById(R.id.typeTxt);
        textDistance = findViewById(R.id.distanceTxt);
        pBar = findViewById(R.id.pBar);
        textValueIntro = findViewById(R.id.textIntroduction);
        loadingTxt = findViewById(R.id.loadingTxt);

        ViewGroup.LayoutParams childLayoutParams = bottomSheet.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        childLayoutParams.height = displayMetrics.heightPixels;
        bottomSheet.setLayoutParams(childLayoutParams);
        setStateBottomSheet(5);

        client = new Client();

        setupSideBar();
        setSupportActionBar(toolbar);
        setupSpinners();

//        TO DO sideBarListView.setOnItemClickListener((parent, view, position ,id) ->{
//            switch (position){
//
//                case 1:
//                    startActivity(new Intent(MainActivity.this, Help.class));
//                case 2:
//                    startActivity(new Intent(MainActivity.this, About.class));
//            }
//        });
        textOrigin.setOnClickListener((view) -> {
            if(getOrigin().length() != 0){
                client.setOriginCompleted(true);
                client.getAddress(getOrigin());
                textDestiny.requestFocus();

            }
        });

        textDestiny.setOnClickListener((view) -> {
            if(getDestiny().length() != 0){
                client.setDestinyCompleted(true);
                client.getAddress(getDestiny());
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        validateButton.setOnClickListener((view) -> {
            client.setAxisNumber(getAxleNumbers());
            client.setLoadType(getTruckLoad());
            client.setReturn(getReturnLoad());
            client.postAddress();
            setStateBottomSheet(4);
            setAllState("none");
        });


        textOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { setupAutoCompleteOrigin(); }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                client.setOriginPlaced(true);
                String text = textOrigin.getText().toString();
                try { client.getAddress(text); } catch(Exception e){ }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        textDestiny.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { setupAutoCompleteDestiny(); }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = textDestiny.getText().toString();
                client.setDestinyPlaced(true);
                try { client.getAddress(text); } catch(Exception e){ }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public String getOrigin(){ return textOrigin.getText().toString(); }

    public String getDestiny(){ return textDestiny.getText().toString(); }

    public String getResult(){ return result; }

    public String getTruckLoad() { return spinnerTruckLoad.getSelectedItem().toString().toLowerCase(); }

    public int getAxleNumbers() { return Integer.parseInt(spinnerAxleNumbers.getSelectedItem().toString()); }

    public boolean getReturnLoad() { if(spinnerReturnLoad.getSelectedItem().toString().equals("Sim")){ return  true; }else{ return false; } }

    public String getDistance() { return distance; }

    public void setAllState(String state) {
        if(state == "showResult"){

            //loading
            pBar.setVisibility(View.GONE);
            loadingTxt.setVisibility(View.GONE);

            textValueIntro.setVisibility(View.VISIBLE);

            //distance
            textDistance.setVisibility(View.VISIBLE);
            textDistance.setText("Distância: " + getDistance() + " km");

            //type
            textType.setVisibility(View.VISIBLE);
            textType.setText("Tipo de carga: " + getTruckLoad());

            //result
            textResult.setVisibility(View.VISIBLE);
            textResult.setText("Total do frete: " + getResult() + " R$");

        }else{
            pBar.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.VISIBLE);

            textDistance.setVisibility(View.GONE);
            textType.setVisibility(View.GONE);
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
            arrayAdapterOrigin = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, client.getOriginPlaces());
            textOrigin.setAdapter(arrayAdapterOrigin);

        } catch(Exception e){ }
    }

    private void setupAutoCompleteDestiny(){
        try {
            arrayAdapterDestiny = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, client.getDestinyPlaces());
            textDestiny.setAdapter(arrayAdapterDestiny);
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

        //ReturnLoad
        ArrayAdapter<CharSequence> returnLoadAdapter = ArrayAdapter.createFromResource(this, R.array.returnLoad, android.R.layout.simple_spinner_item);
        returnLoadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReturnLoad.setAdapter(returnLoadAdapter);


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
        }

        return true;
    }
}