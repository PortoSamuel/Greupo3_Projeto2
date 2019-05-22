package br.edu.insper.truckpad_insper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PriceClient {

    @POST("antt_price")
    Call<Price> calculatePrice(@Body PriceInformation priceInformation);
}
