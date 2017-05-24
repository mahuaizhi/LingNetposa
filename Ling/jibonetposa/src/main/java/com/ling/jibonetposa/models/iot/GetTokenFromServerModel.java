package com.ling.jibonetposa.models.iot;

import com.ling.jibonetposa.base.BaseRequestModel;
import com.ling.jibonetposa.entities.ResultGetTokenEntity;
import com.ling.jibonetposa.iretrofit.IRequestCallback;
import com.ling.jibonetposa.iretrofit.iot.IIOTGetPhantomTokenFromServer;

import retrofit2.Call;

import static com.ling.jibonetposa.constants.IOTApiConstant.API_PATH_JIBO;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public class GetTokenFromServerModel extends BaseRequestModel<ResultGetTokenEntity> {

    public GetTokenFromServerModel(IRequestCallback requestCallback) {
        super(requestCallback);
        this.mApiPath = API_PATH_JIBO;
    }

    public void getPhantomToken(String userid, String type) {
        mParams.put("userid", userid);
        mParams.put("type", type);
        IIOTGetPhantomTokenFromServer iiotGetToken = retrofit().create(IIOTGetPhantomTokenFromServer.class);
        Call<ResultGetTokenEntity> token = iiotGetToken.execute(organizeParams());
        execute(token);
    }
}
