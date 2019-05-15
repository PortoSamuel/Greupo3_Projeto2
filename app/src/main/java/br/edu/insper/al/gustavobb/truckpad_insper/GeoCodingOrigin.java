package br.edu.insper.al.gustavobb.truckpad_insper;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GeoCodingOrigin extends AsyncTask<String, Void, Boolean>  {
    private String addressOrigin = "";
    private String jsonOrigin = "";

    public JSONObject constructJsonOrigin() throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonOrigin);
        jsonObj.remove("provider");
        return jsonObj;
    }

    public void setJsonOrigin(String json){ this.jsonOrigin = json; }

    public String getAddressOrigin(){ return addressOrigin; }

    public void setAddressOrigin(String addresses){
        String[] addressSplit = addresses.split("\\s+");
        String urlAddress = "";

        if (addressSplit.length != 0){
            for (int i = 0; i < addressSplit.length; i++){ urlAddress += addressSplit[i] + "%20"; }
            this.addressOrigin = urlAddress;
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean mistake = false;

        try{

            URL url = new URL("https://geo.api.truckpad.io/v1/autocomplete?search=" + getAddressOrigin());
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            setJsonOrigin(in.readLine());
            System.out.println(constructJsonOrigin());
            in.close();

        } catch(Exception e){ mistake = true; }

        return mistake;
    }
}