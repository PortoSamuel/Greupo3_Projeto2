package br.edu.insper.al.gustavobb.truckpad_insper;

import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;



public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private ListView sideBarListView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Spinner spinnerTruckLoad, spinnerAxleNumbers, spinnerReturnLoad;
    private TextInputEditText textOrigin, textDestiny;
    private Button validateButton;
    private GeoCodingInterface geoInterface;
    private GeoCodingClient client;

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

        client = new GeoCodingClient();

        setupSideBar();
        setSupportActionBar(toolbar);
        setupSpinners();

        textOrigin.setOnClickListener((view) -> {
            if(getOrigin().length() != 0){
                client.getAddress(getOrigin());
            }
        });

        textDestiny.setOnClickListener((view) -> {
            if(getDestiny().length() != 0){
                client.getAddress(getDestiny());
            }
        });

        validateButton.setOnClickListener((view) -> {
            System.out.println(getAxleNumbers());
            System.out.println(getTruckLoad());
            System.out.println(getReturnLoad());
            client.postAddress();
        });


    }

    public String getOrigin(){ return textOrigin.getText().toString(); }

    public String getDestiny(){ return textDestiny.getText().toString(); }

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
        String[] sideBarOptions = {"Home", "2"};
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

    public String getTruckLoad() {
        return spinnerTruckLoad.getSelectedItem().toString();
    }

    public int getAxleNumbers() {
        return Integer.parseInt(spinnerAxleNumbers.getSelectedItem().toString());
    }

    public boolean getReturnLoad() {
        if(spinnerReturnLoad.getSelectedItem().toString().equals("Sim")){
            return  true;
        }else{
            return false;
        }
    }
}