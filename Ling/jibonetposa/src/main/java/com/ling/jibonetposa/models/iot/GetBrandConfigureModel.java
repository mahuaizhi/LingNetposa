package com.ling.jibonetposa.models.iot;

import com.ling.jibonetposa.base.BaseRequestModel;
import com.ling.jibonetposa.entities.iot.ResultBrandConfigure;
import com.ling.jibonetposa.iretrofit.IRequestCallback;
import com.ling.jibonetposa.iretrofit.iot.IIOTGetBrandConfigureFromServer;

import retrofit2.Call;

import static com.ling.jibonetposa.constants.IOTApiConstant.API_PATH_JIBO;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public class GetBrandConfigureModel extends BaseRequestModel<ResultBrandConfigure> {

    public GetBrandConfigureModel(IRequestCallback requestCallback) {
        super(requestCallback);
        this.mApiPath = API_PATH_JIBO;
    }

    public void getBrandConfigure() {
        IIOTGetBrandConfigureFromServer iiotGetToken = retrofit().create(IIOTGetBrandConfigureFromServer.class);
        Call<ResultBrandConfigure> token = iiotGetToken.execute(organizeParams());
        execute(token);
    }
}
