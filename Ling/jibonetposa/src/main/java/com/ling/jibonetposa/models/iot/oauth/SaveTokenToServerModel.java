package com.ling.jibonetposa.models.iot.oauth;

import com.ling.jibonetposa.base.BaseRequestModel;
import com.ling.jibonetposa.entities.iot.ResultSaveAuthDataEntity;
import com.ling.jibonetposa.entities.iot.SaveAuthDataEntity;
import com.ling.jibonetposa.iretrofit.IRequestCallback;
import com.ling.jibonetposa.iretrofit.iot.IIOTSavePhantomTokenToServer;

import retrofit2.Call;

import static com.ling.jibonetposa.constants.APIConstant.API_PATH_JIBO;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public class SaveTokenToServerModel extends BaseRequestModel<ResultSaveAuthDataEntity> {

    public SaveTokenToServerModel(IRequestCallback requestCallback) {
        super(requestCallback);
        this.mApiPath = API_PATH_JIBO;
    }

    public void saveToken(SaveAuthDataEntity tokenEntity) {

        mParams.put("userid", tokenEntity.getUserid());
        mParams.put("type", tokenEntity.getType());
        mParams.put("access_token", tokenEntity.getAccess_token());
        mParams.put("refresh_token", tokenEntity.getRefresh_token());
        mParams.put("token_type", tokenEntity.getToken_type());
        mParams.put("expires_in", tokenEntity.getExpires_in());
        mParams.put("created_at", tokenEntity.getCreated_at());
        mParams.put("scope", tokenEntity.getScope());

        IIOTSavePhantomTokenToServer iiotGetToken = retrofit().create(IIOTSavePhantomTokenToServer.class);
        Call<ResultSaveAuthDataEntity> token = iiotGetToken.execute(organizeJsonParams());
        execute(token);
    }
}
