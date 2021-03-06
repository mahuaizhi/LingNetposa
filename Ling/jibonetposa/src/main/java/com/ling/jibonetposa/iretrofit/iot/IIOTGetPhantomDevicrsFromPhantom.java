package com.ling.jibonetposa.iretrofit.iot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import static com.ling.jibonetposa.constants.APIConstant.API_PATH_PHANTON_GET_DEV_ALL;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public interface IIOTGetPhantomDevicrsFromPhantom {

    @Headers({"Accept:application/json"})
    @GET(API_PATH_PHANTON_GET_DEV_ALL)
    Call<Object> execute(@Header("Authorization") String accessToken);
}
