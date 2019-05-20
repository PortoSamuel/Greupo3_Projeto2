package br.edu.insper.al.gustavobb.truckpad_insper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeoCodingInterface {
    @GET("autocomplete")
    Call<GeoCoding> getGeoAPI(
            @Query("search") String address

    );
}
