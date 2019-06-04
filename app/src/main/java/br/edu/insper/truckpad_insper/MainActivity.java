package br.edu.insper.truckpad_insper;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayAdapter<String> arrayAdapter, arrayAdapterOrigin, arrayAdapterDestiny;
    private ListView sideBarListView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private static Spinner spinnerTruckLoad, spinnerAxleNumbers;
    private AutoCompleteTextView textOrigin, textDestiny;
    private Button validateButton;
    private Client client;
    private String result, distance, fuel, tool, result2;
    private static TextView textResult, textValueIntro, textDistance, loadingTxt, textResultReturn, textResultNumber, textReturnResultNumber, textResultDistance, textGas, textResultGas, textToll, textResultToll;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private static ProgressBar pBar;
    private boolean originPerformed, destinyPerformed;
    private GoogleMap map;
    private List<List<List<Double>>> route;
    private ArrayList<LatLng> routeCoords;
    private RelativeLayout relativeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        relativeMap = findViewById(R.id.relativeMap);

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


        textOrigin.setOnClickListener((view) -> {
            textDestiny.requestFocus();
            textOrigin.dismissDropDown();
        });

        textDestiny.setOnClickListener((view) -> {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            textDestiny.dismissDropDown();
        });


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

        textOrigin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // code to execute when EditText loses focus
                client.setOriginCompleted(true);
                client.getAddress(getOrigin());
                setOriginPerformed(true);

            }if(hasFocus){ try{
                client.deleteOrigin();
                client.setOriginOnResponse(false);
            }catch (Exception e){ } }
        });

        textDestiny.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // code to execute when EditText loses focus
                client.setDestinyCompleted(true);
                client.getAddress(getDestiny());
                setDestinyPerformed(true);

            }if(hasFocus){ try{
                client.deleteDestiny();
                client.setDestinyOnResponse(false);
            }catch (Exception e){ } }
        });

        validateButton.setOnClickListener((view) -> {
            if(getDestiny().length() != 0 && getOrigin().length() != 0) {
                textOrigin.clearFocus();
                textDestiny.clearFocus();
            }else{ if(getDestiny().length() == 0 && getOrigin().length() == 0) { showToast("Preencha todos os campos"); } }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.arrowDown).setRotation(slideOffset * 180);
                findViewById(R.id.arrowUp).setRotation(slideOffset * -180);
            }
        });

        textOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { setupAutoCompleteOrigin(); }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setOriginPerformed(false);
                handler.postDelayed(() -> {
                    client.setOriginPlaced(true);
                    String text = textOrigin.getText().toString();
                    try {
                        client.getAddress(text);
                        setupAutoCompleteOrigin();
                    } catch(Exception e){ }
                }, 1200);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        textDestiny.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { setupAutoCompleteDestiny(); }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setDestinyPerformed(false);
                handler.postDelayed(() -> {
                    client.setDestinyPlaced(true);
                    String text = textDestiny.getText().toString();
                    try {
                        client.getAddress(text);
                        setupAutoCompleteDestiny();
                    } catch(Exception e){ }
                }, 1200);
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

    public String getFuel(){return fuel;}

    public String getTool(){return tool;}

    public String getResult2(){return result2;}

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
            textResultDistance.setText((getDistance() + " km").replace('.', ','));

            //gas
            textGas.setVisibility(View.VISIBLE);
            textGas.setText("     Combustível");

            textResultGas.setVisibility(View.VISIBLE);
            textResultGas.setText(("R$ " + getFuel()).replace('.', ','));

            //toll
            textToll.setVisibility(View.VISIBLE);
            textToll.setText("     Pedagio:");

            textResultToll.setVisibility(View.VISIBLE);
            textResultToll.setText(("R$ " + getTool()).replace('.', ','));

            //result
            textResult.setVisibility(View.VISIBLE);
            textResult.setText("Valor sem retorno:");

            textResultReturn.setVisibility(View.VISIBLE);
            textResultReturn.setText("Valor com retorno:");

            textResultNumber.setVisibility(View.VISIBLE);
            textResultNumber.setText(("R$ " + getResult()).replace('.', ','));

            textReturnResultNumber.setVisibility(View.VISIBLE);
          
            relativeMap.setVisibility(View.VISIBLE);

            setupMap();
            textReturnResultNumber.setText(("R$ " + getResult2()).replace('.', ','));
        }else{

            pBar.setVisibility(View.VISIBLE);
            loadingTxt.setVisibility(View.VISIBLE);

            textDistance.setVisibility(View.GONE);
            textValueIntro.setVisibility(View.GONE);
            textResultDistance.setVisibility(View.GONE);

            textResultToll.setVisibility(View.GONE);
            textToll.setVisibility(View.GONE);
            textGas.setVisibility(View.GONE);
            textResultGas.setVisibility(View.GONE);
            textResult.setVisibility(View.GONE);
         
            textResultReturn.setVisibility(View.GONE);
            textResultNumber.setVisibility(View.GONE);
            textReturnResultNumber.setVisibility(View.GONE);
          
            relativeMap.setVisibility(View.GONE);
        }
    }

    public void setupMap(){
        try{
            //Map
            map.clear();
            setRouteCoords(route);
            LatLng originLatLng = new LatLng(routeCoords.get(0).latitude, routeCoords.get(0).longitude);
            LatLng destinyLatLng = new LatLng(routeCoords.get(routeCoords.size()-1).latitude, routeCoords.get(routeCoords.size()-1).longitude);
            map.addMarker(new MarkerOptions().position(originLatLng).title("Origem"));
            map.addMarker(new MarkerOptions().position(destinyLatLng).title("Destino"));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(originLatLng);
            builder.include(destinyLatLng);
            LatLngBounds bounds =  builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
            map.animateCamera(cameraUpdate);
            System.out.println("AAA");

            //Polyline
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(routeCoords);
            polylineOptions.width(5);
            polylineOptions.color(Color.RED);
            polylineOptions.clickable(true);
            map.addPolyline(polylineOptions);

        } catch(Exception e){

        }
    }


    public void setOnResponsePrice(double price, double dist, float fuel, float tool, double price2) {

        this.result = String.valueOf(price);
        this.distance = String.valueOf(dist);
        this.fuel = String.valueOf(fuel);
        this.tool = String.valueOf(tool);
        this.result2 = String.valueOf(price2);
    }

    public void setStateBottomSheet(int state){ bottomSheetBehavior.setState(state); }

    private void setupAutoCompleteOrigin(){
        try {
            arrayAdapterOrigin = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, client.getOriginPlaces());
            textOrigin.setAdapter(arrayAdapterOrigin);
            if(!originPerformed){ textOrigin.showDropDown(); }
        } catch(Exception e){ }
    }

    private void setupAutoCompleteDestiny(){
        try {
            arrayAdapterDestiny = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, client.getDestinyPlaces());
            textDestiny.setAdapter(arrayAdapterDestiny);
            if(!destinyPerformed){ textDestiny.showDropDown(); }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    public void setRoute(List<List<List<Double>>> list){
        this.route = list;
    }

    public void setRouteCoords(List<List<List<Double>>> list){
        routeCoords = new ArrayList<LatLng>();
        for(int i = 0; i < route.get(0).size(); i++){
            routeCoords.add(new LatLng(list.get(0).get(i).get(1), list.get(0).get(i).get(0)));

        }
    }
}