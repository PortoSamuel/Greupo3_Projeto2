package br.edu.insper.al.gustavobb.truckpad_insper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoCodingClient {
    private GeoPlace place;
    private GeoCodingReceived codeRceived;
    private GeoCodingPayload payload = new GeoCodingPayload();
    private GeoRouteReceived routeReceived;
    private Double distance;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://geo.api.truckpad.io/v1/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    GeoCodingInterface client = retrofit.create(GeoCodingInterface.class);

    public void getAddress(String address){

        Call<GeoCodingReceived> call = client.getGeoCoding(address);

        call.enqueue(new Callback<GeoCodingReceived>() {
            @Override
            public void onResponse(Call<GeoCodingReceived> call, Response<GeoCodingReceived> response) {
                codeRceived = response.body();
                place = codeRceived.getPlaces().get(0);
                payload.putPlace(place);
            }

            @Override
            public void onFailure(Call<GeoCodingReceived> call, Throwable t) {

            }
        });
    }

    public void postAddress(){
        MainActivity main = new MainActivity();

        Call<GeoRouteReceived> call = client.getGeoRoute(payload);

        call.enqueue(new Callback<GeoRouteReceived>() {
            @Override
            public void onResponse(Call<GeoRouteReceived> call, Response<GeoRouteReceived> response) {
                routeReceived = response.body();
                System.out.println(routeReceived.getDistance());
                System.out.println("1");
                distance = routeReceived.getDistance()/1000;
                System.out.println("2");
                PriceClient client = ServiceGenerator.createService(PriceClient.class);

                PriceInformation priceInformation = new PriceInformation(2, distance, false,"geral");

                Call<Price> callprice = client.calculatePrice(priceInformation);

                callprice.enqueue(new Callback<Price>() {
                    @Override
                    public void onResponse(Call<Price> call, Response<Price> response) {
                        System.out.println("PREÇO:");
                        System.out.println(response.body().getShipment_value());
                    }

                    @Override
                    public void onFailure(Call<Price> call, Throwable t) {

                    }
                });






            }

            @Override
            public void onFailure(Call<GeoRouteReceived> call, Throwable t) {

            }
        });
    }

}
