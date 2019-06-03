package br.edu.insper.truckpad_insper.api.client;

import br.edu.insper.truckpad_insper.api.payloads.Price;
import br.edu.insper.truckpad_insper.api.payloads.PriceInformation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PriceClient {

    @POST("antt_price")
    Call<Price> calculatePrice(@Body PriceInformation priceInformation);
}
