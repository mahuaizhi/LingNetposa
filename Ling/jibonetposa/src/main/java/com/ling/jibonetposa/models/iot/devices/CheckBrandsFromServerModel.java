package com.ling.jibonetposa.models.iot.devices;

import com.ling.jibonetposa.base.BaseRequestModel;
import com.ling.jibonetposa.entities.iot.ResultGetBrandEntity;
import com.ling.jibonetposa.iretrofit.IRequestCallback;
import com.ling.jibonetposa.iretrofit.iot.IIOTGetBrands;

import retrofit2.Call;

import static com.ling.jibonetposa.constants.APIConstant.API_PATH_JIBO;

/**
 * Created by mhz小志 on 2017/4/18.
 */
public class CheckBrandsFromServerModel extends BaseRequestModel<ResultGetBrandEntity> {

    public CheckBrandsFromServerModel(IRequestCallback requestCallback) {
        super(requestCallback);
        this.mApiPath = API_PATH_JIBO;
    }

    public void getBrands(String userId) {
        mParams.put("userid", userId);
        IIOTGetBrands iiotGetToken = retrofit().create(IIOTGetBrands.class);
        Call<ResultGetBrandEntity> call = iiotGetToken.execute(organizeParams());
        execute(call);
    }
}
