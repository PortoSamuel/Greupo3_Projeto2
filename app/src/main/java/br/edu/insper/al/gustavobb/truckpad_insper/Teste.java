package br.edu.insper.al.gustavobb.truckpad_insper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Teste {
    public static void main(String[] args) {
        GeoCodingClient client = new GeoCodingClient();
        client.getAddresses("rua quata 300 vila olimpia");

    }
}
