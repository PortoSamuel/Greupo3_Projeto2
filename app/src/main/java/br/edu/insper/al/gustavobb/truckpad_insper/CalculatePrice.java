package br.edu.insper.al.gustavobb.truckpad_insper;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalculatePrice{
    public static void main(String[] args) {
        String API_BASE_URL = "https://tictac.api.truckpad.io/v1/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        PriceInformation priceInformation = new PriceInformation(2, 113.128, false, "geral");
        Price price = new Price();

        PriceClient client = retrofit.create(PriceClient.class);

        Call<Price> call = client.calculatePrice(priceInformation);

        call.enqueue(new Callback<Price>() {
            @Override
            public void onResponse(Call<Price> call, Response<Price> response) {
                System.out.println(response.body().getShipment_value());
            }

            @Override
            public void onFailure(Call<Price> call, Throwable t) {

            }
        });




    }

}