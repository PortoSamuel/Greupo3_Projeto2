package br.edu.insper.truckpad_insper.api.client;

import br.edu.insper.truckpad_insper.api.payloads.GeoCodingPayload;
import br.edu.insper.truckpad_insper.api.payloads.GeoCodingReceived;
import br.edu.insper.truckpad_insper.api.payloads.GeoRouteReceived;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeoCodingInterface {
    @GET("autocomplete")
    Call<GeoCodingReceived> getGeoCoding(
            @Query("search") String address

    );

    @POST("route")
    Call<GeoRouteReceived> getGeoRoute(
            @Body GeoCodingPayload payload

    );
}
