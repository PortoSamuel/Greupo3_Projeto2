package br.edu.insper.al.gustavobb.truckpad_insper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private GeoPlace place;
    private GeoCodingReceived codeReceived;
    private GeoCodingPayload payload = new GeoCodingPayload();
    private GeoRouteReceived routeReceived;
    private int axisNumber;
    private boolean isReturn;
    private String loadType;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://geo.api.truckpad.io/v1/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    GeoCodingInterface client = retrofit.create(GeoCodingInterface.class);

    public int getAxisNumber() { return axisNumber; }

    public boolean isReturn() { return isReturn; }

    public String getLoadType() { return loadType; }

    public void setAxisNumber(int axisNumber) { this.axisNumber = axisNumber; }

    public void setReturn(boolean aReturn) { isReturn = aReturn; }

    public void setLoadType(String loadType) { this.loadType = loadType; }

    public void getAddress(String address){

        Call<GeoCodingReceived> call = client.getGeoCoding(address);

        call.enqueue(new Callback<GeoCodingReceived>() {
            @Override
            public void onResponse(Call<GeoCodingReceived> call, Response<GeoCodingReceived> response) {
                codeReceived = response.body();
                place = codeReceived.getPlaces().get(0);
                payload.putPlace(place);
            }

            @Override
            public void onFailure(Call<GeoCodingReceived> call, Throwable t) { }
        });
    }

    public void postAddress(){

        Call<GeoRouteReceived> call = client.getGeoRoute(payload);

        call.enqueue(new Callback<GeoRouteReceived>() {

        @Override
        public void onResponse(Call<GeoRouteReceived> call, Response<GeoRouteReceived> response) {
            routeReceived = response.body();
            PriceClient client = ServiceGenerator.createService(PriceClient.class);

            PriceInformation priceInformation = new PriceInformation(getAxisNumber(), routeReceived.getDistance()/1000, isReturn(), getLoadType());

            Call<Price> callprice = client.calculatePrice(priceInformation);

            callprice.enqueue(new Callback<Price>() {
                @Override
                public void onResponse(Call<Price> call, Response<Price> response) { System.out.println(response.body().getShipment_value()); }

                @Override
                public void onFailure(Call<Price> call, Throwable t) { }
            });
        }

            @Override
            public void onFailure(Call<GeoRouteReceived> call, Throwable t) { }
        });
    }
}
