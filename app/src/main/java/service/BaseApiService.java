package service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by christian on 14/02/18.
 */

public interface BaseApiService {

    @FormUrlEncoded
    @POST("countUserRate.php")
    Call<ResponseBody> countUser(@Field("idKos") Integer idKos,
                                 @Field("idUser") Integer idUser
    );

    @FormUrlEncoded
    @POST("dialogRate.php")
    Call<ResponseBody> saveRating(@Field("idKos") Integer idKos,
                                  @Field("idUser") Integer idUser,
                                  @Field("isi") String isi,
                                  @Field("rate") double rate
    );

}