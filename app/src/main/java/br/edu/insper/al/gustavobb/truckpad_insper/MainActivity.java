package br.edu.insper.al.gustavobb.truckpad_insper;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter, arrayAdapterOrigin, arrayAdapterDestiny;
    private ListView sideBarListView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Spinner spinnerTruckLoad, spinnerAxleNumbers, spinnerReturnLoad;
    private AutoCompleteTextView textOrigin, textDestiny;
    private Button validateButton;
    private Client client;
    private String result;
    private TextView textResult;

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
        textResult = findViewById(R.id.textResult);



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
//         TO DO   textResult.setText(result);


        });


        textOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setupAutoCompleteOrigin();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                client.setOriginPlaced(true);
                String text = textOrigin.getText().toString();
                try {
                    client.getAddress(text);
                } catch(Exception e){ }


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
                String text = textDestiny.getText().toString();
                client.setDestinyPlaced(true);
                try {

                    client.getAddress(text);

                } catch(Exception e){ }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    public String getOrigin(){ return textOrigin.getText().toString(); }

    public String getDestiny(){ return textDestiny.getText().toString(); }

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


    public String getTruckLoad() { return spinnerTruckLoad.getSelectedItem().toString().toLowerCase(); }

    public int getAxleNumbers() { return Integer.parseInt(spinnerAxleNumbers.getSelectedItem().toString()); }

    public boolean getReturnLoad() { if(spinnerReturnLoad.getSelectedItem().toString().equals("Sim")){ return  true; }else{ return false; } }

    public void setTextResult(double result){
        this.result = String.valueOf(result);
        System.out.println(this.result);


    }



}