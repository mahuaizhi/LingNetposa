package com.ling.jibonetposa.iretrofit.iot;

import com.ling.jibonetposa.entities.iot.ResultUpdateNameEntity;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

import static com.ling.jibonetposa.constants.APIConstant.API_PATH_JIBO_DEVICES;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public interface IIOTUpdateDevName {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @PUT(API_PATH_JIBO_DEVICES)
    Call<ResultUpdateNameEntity> execute(@Body RequestBody route);
}
