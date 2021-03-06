package com.ling.jibonetposa.iretrofit.iot;

import com.ling.jibonetposa.entities.iot.BroadLinkTokenEntity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.ling.jibonetposa.constants.APIConstant.BROADLINK_API_PATH_TOKEN;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public interface IIOTGetBroadLinkTokenFromBroadLink {

    @FormUrlEncoded
    @POST(BROADLINK_API_PATH_TOKEN)
    Call<BroadLinkTokenEntity> execute(@FieldMap Map<String, Object> params);
}
