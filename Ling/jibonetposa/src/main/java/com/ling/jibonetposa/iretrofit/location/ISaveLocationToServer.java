package com.ling.jibonetposa.iretrofit.location;

import com.ling.jibonetposa.entities.locaiton.ResultLocationEntity;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.ling.jibonetposa.constants.APIConstant.API_PATH_JIBO_LOCATION;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public interface ISaveLocationToServer {

    @Headers({"Content-type:application/json"})
    @POST(API_PATH_JIBO_LOCATION)
    Call<ResultLocationEntity> execute(@Body RequestBody route);
}
