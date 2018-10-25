package com.example.chris.nearbykos2;

/**
 * Created by christian on 10/11/17.
 */

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("yopem/nearby/php/getKota.php")
    Call<JSONResponse> getKota();

    @GET("yopem/nearby/php/getFasilitas.php")
    Call<JSONResponse> getFasilitas();

    @GET("yopem/nearby/php/getBank.php")
    Call<JSONResponse> getBank();
}
