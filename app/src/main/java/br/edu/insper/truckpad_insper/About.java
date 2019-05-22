package br.edu.insper.truckpad_insper;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class About extends AppCompatActivity {
    private ArrayAdapter<String> arrayAdapter;
    private ListView sideBarListView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        sideBarListView = findViewById(R.id.sideBarListAbout);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);


        setupSideBar();
        setSupportActionBar(toolbar);

//        TO DO sideBarListView.setOnItemClickListener((parent, view, position ,id) ->{
//            switch (position){
//                case 0:
//                    startActivity(new Intent(About.this, MainActivity.class));
//                case 1:
//                    startActivity(new Intent(About.this,  Help.class));
//
//            }
//        });
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
