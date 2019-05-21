package br.edu.insper.al.gustavobb.truckpad_insper;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test {
    public static void main(String[] args) {
        GeoPlace place0 = new GeoPlace();
        place0.putPoint(-46.68664);
        place0.putPoint(-23.59496);
        GeoPlace place1 = new GeoPlace();
        place1.putPoint(-46.67678);
        place1.putPoint(-23.59867);
        place1.setAddress("Rua Quatá");
        place1.setDisplay_name("Rua Quatá 300, Itaim Bibi, São Paulo - SP");
        place1.setNumber("300");
        place1.setPostal_code("04546-000");

        GeoCodingPayload payload = new GeoCodingPayload();
        payload.putPlace(place0);
        payload.putPlace(place1);

        String API_BASE_URL = "https://geo.api.truckpad.io/v1/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        GeoCodingInterface client =  retrofit.create(GeoCodingInterface.class);

        Call<GeoRouteReceived> call =
                client.getGeoRoute(payload);

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<GeoRouteReceived>() {
            @Override
            public void onResponse(Call<GeoRouteReceived> call, Response<GeoRouteReceived> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                System.out.println("Sucess");
                System.out.println(response.body().getDistance());
            }

            @Override
            public void onFailure(Call<GeoRouteReceived> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                System.out.println("Error");
                System.out.println(t.getMessage());
                System.out.println(t.getCause());
                System.out.println(t.getLocalizedMessage());


            }
        });

    }
}


