package br.edu.insper.al.gustavobb.truckpad_insper;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GeoCoding extends AsyncTask<String, Void, Boolean>  {
    private String address = "";
    private String json = "";

    public String getJson(){ return json; }

    public void setJson(String json){ this.json = json; }

    public String getAddress(){ return address; }

    public void setAddress(String addresses){
        String[] addressSplit = addresses.split("\\s+");
        String urlAddress = "";

        if (addressSplit.length != 0){
            for (int i = 0; i < addressSplit.length; i++){ urlAddress += addressSplit[i] + "%20"; }
            this.address = urlAddress;
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean mistake = false;

        try{

            URL url = new URL("https://geo.api.truckpad.io/v1/autocomplete?search=" + getAddress());
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            setJson(in.readLine());
            System.out.println(getJson());
            in.close();

        } catch(Exception e){ mistake = true; }

        return mistake;
    }
}