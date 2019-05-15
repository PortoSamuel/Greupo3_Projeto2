package br.edu.insper.al.gustavobb.truckpad_insper;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GeoCodingDestiny  extends AsyncTask<String, Void, Boolean>  {
    private String addressDestiny = "";
    private String jsonDestiny = "";

    public JSONObject constructJsonDestiny() throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonDestiny);
        jsonObj.remove("provider");
        return jsonObj;
    }

    public void setJsonDestiny(String json){ this.jsonDestiny = json; }

    public String getAddressDestiny(){ return addressDestiny; }

    public void setAddressDestiny(String addresses){
        String[] addressSplit = addresses.split("\\s+");
        String urlAddress = "";

        if (addressSplit.length != 0){
            for (int i = 0; i < addressSplit.length; i++){ urlAddress += addressSplit[i] + "%20"; }
            this.addressDestiny = urlAddress;
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean mistake = false;

        try{

            URL url = new URL("https://geo.api.truckpad.io/v1/autocomplete?search=" + getAddressDestiny());
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            setJsonDestiny(in.readLine());
            System.out.println(constructJsonDestiny());
            in.close();

        } catch(Exception e){ mistake = true; }

        return mistake;
    }
}
