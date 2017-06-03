package com.ling.jibonetposa.modules;

import com.ling.jibonetposa.LingManager;
import com.ling.jibonetposa.base.BaseEntity;
import com.ling.jibonetposa.entities.bean.BrandBean;
import com.ling.jibonetposa.entities.bean.DeviceBean;
import com.ling.jibonetposa.entities.iot.BrandStatusEntity;
import com.ling.jibonetposa.entities.iot.DevicesEntity;
import com.ling.jibonetposa.entities.iot.PutUpdateDevNameEntity;
import com.ling.jibonetposa.entities.iot.ResultGetBrandEntity;
import com.ling.jibonetposa.entities.iot.ResultGetDevicesEntity;
import com.ling.jibonetposa.entities.iot.SaveAuthDataEntity;
import com.ling.jibonetposa.iretrofit.IRequestCallback;
import com.ling.jibonetposa.models.iot.CancelAuthorizedModel;
import com.ling.jibonetposa.models.iot.CheckBrandsFromServerModel;
import com.ling.jibonetposa.models.iot.GetBrandConfigureModel;
import com.ling.jibonetposa.models.iot.GetDevicesFromServerModel;
import com.ling.jibonetposa.models.iot.GetScenariosFromServerModel;
import com.ling.jibonetposa.models.iot.GetTokenFromServerModel;
import com.ling.jibonetposa.models.iot.SaveTokenToServerModel;
import com.ling.jibonetposa.models.iot.UpdateDevNameModel;
import com.ling.jibonetposa.models.iot.broadlink.GetBroadLinkTokenModel;
import com.ling.jibonetposa.models.iot.broadlink.UpdataBLGetAccessKeyModel;
import com.ling.jibonetposa.models.iot.haier.HEModel;
import com.ling.jibonetposa.models.iot.phantom.GetPhantomTokenModel;
import com.ling.jibonetposa.models.iot.phantom.UpdatePhantomNameModel;
import com.ling.jibonetposa.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ling.jibonetposa.base.BaseRequestModel.RETROFIT_SUCCESS;
import static com.ling.jibonetposa.constants.IOTApiConstant.API_PATH_PHANTON_AUTHORIZE;
import static com.ling.jibonetposa.constants.IOTApiConstant.BROADLINK_API_PATH;
import static com.ling.jibonetposa.constants.IOTApiConstant.BROADLINK_CLIENT_ID;
import static com.ling.jibonetposa.constants.IOTApiConstant.OAUTH_REDIRECT_URI;
import static com.ling.jibonetposa.constants.IOTApiConstant.PHANTON_APP_ID;
import static com.ling.jibonetposa.constants.IOTApiConstant.PHANTON_SCOPE;

/**
 * Created by mhz小志 on 2017/3/17.
 */

public class IOTAgent {

    /**
     * 获取所有设备
     *
     * @param userid 用户id
     * @param type   刷新的类别，（0：获取存储在Ling服务器的设备信息。1：获取所有第三方最新的设备信息）
     */
    public void getAllDevices(String userid, int type, final IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            userid = LingManager.getInstance().getTestUserId();
        }
        final String finalUserid = userid;
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + finalUserid);
        new GetDevicesFromServerModel(new IRequestCallback() {
            @Override
            public void responsedCallback(BaseEntity entity, int errorCode, Throwable error) {
                if (errorCode == RETROFIT_SUCCESS) {
                    ResultGetDevicesEntity brandEntity = (ResultGetDevicesEntity) entity;
                    if (brandEntity != null) {
                        DevicesEntity devicesEntity = getDevicesEntity(finalUserid, brandEntity);
                        checkDevicesName(devicesEntity);
                        requestCallback.responsedCallback(devicesEntity, errorCode, error);
                    }
                } else {
                    requestCallback.responsedCallback(entity, errorCode, error);
                }

            }
        }).getDevices(userid, type);
    }

    /**
     * 检查品牌的绑定信息
     *
     * @param userid
     */
    public void checkBrandStatus(String userid, final IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            userid = LingManager.getInstance().getTestUserId();
        }
        final String finalUserid = userid;
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + finalUserid);
        new CheckBrandsFromServerModel(new IRequestCallback() {
            @Override
            public void responsedCallback(BaseEntity entity, int errorCode, Throwable error) {
                if (errorCode == RETROFIT_SUCCESS) {
                    ResultGetBrandEntity brandEntity = (ResultGetBrandEntity) entity;
                    if (brandEntity != null) {
                        BrandStatusEntity brandStatusEntity = getBrandStatusEntity(brandEntity);
                        brandStatusEntity.setUserid(finalUserid);
                        requestCallback.responsedCallback(brandStatusEntity, errorCode, error);
                    } else {
                        requestCallback.responsedCallback(null, errorCode, error);
                    }
                } else {
                    requestCallback.responsedCallback(null, errorCode, error);
                }
            }
        }).getBrands(userid);
    }

    /**
     * 取消用户授权
     */
    public void cancelAuthorized(String userid, String brandType, final IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            userid = LingManager.getInstance().getTestUserId();
        }
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + userid);
        new CancelAuthorizedModel(requestCallback).cancelAuthorized(userid, brandType);
    }

    /**
     * 获取保存的Phantom Token
     */
    public void getTokenFromServer(String userid, String brandType, final IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            userid = LingManager.getInstance().getTestUserId();
        }
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + userid);
        new GetTokenFromServerModel(requestCallback).getPhantomToken(userid, brandType);
    }

    /**
     * 更改幻腾设备名称
     */
    public void updatePhantomDevName(String accessToken, String identId, String newName, final IRequestCallback requestCallback) {
        new UpdatePhantomNameModel(requestCallback).updateName(accessToken, identId, newName);
    }

    /**
     * 更改海尔设备名称
     */
    public void updateHaierDevName(String identId, String newName, final IRequestCallback requestCallback) {
        new HEModel(LingManager.getInstance().getAppContext()).updateDeviceNickName(identId, newName, requestCallback);
    }

    /**
     * 登录海尔账号  成功后将账号数据存到server
     */
    public void doHELogin(String username, String password, final IRequestCallback requestCallback) {
        new HEModel(LingManager.getInstance().getAppContext()).doHELogin(username, password, requestCallback);
    }

    /**
     * 获取幻腾授权
     * <p>
     * 根据authorizedCode去获取幻腾Token，然后将Token保存到服务器
     */
    public void getPhantomAuthorized(final String authorizedCode, final IRequestCallback requestCallback) {
        new GetPhantomTokenModel(requestCallback).getPhantomToken(authorizedCode);
    }

    /**
     * 获取BroadLink授权
     * <p>
     * 根据authorizedCode去获取BroadLink Token，然后将Token保存到服务器
     */
    public void getBroadLinkAuthorized(final String authorizedCode, final IRequestCallback requestCallback) {
        new GetBroadLinkTokenModel(requestCallback).getBroadLinkToken(authorizedCode);
    }

    /**
     * 保存授权数据
     */
    public void doSaveAuthDataToServer(SaveAuthDataEntity tokenEntity, final IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            tokenEntity.setUserid(LingManager.getInstance().getTestUserId());
        }
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + tokenEntity.getUserid());
        new SaveTokenToServerModel(requestCallback).saveToken(tokenEntity);
    }


    /**
     * 获取场景列表
     */
    public void getScenariosFromServer(String userid, IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            userid = LingManager.getInstance().getTestUserId();
        }
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + userid);
        new GetScenariosFromServerModel(requestCallback).execute(userid);
    }

    /**
     *
     */
    public void updateDevName(PutUpdateDevNameEntity putUpdateNameEntity, IRequestCallback requestCallback) {
        if (LingManager.getInstance().isUseTestUserid()) {
            putUpdateNameEntity.getData().getAttributes().setUserid(LingManager.getInstance().getTestUserId());
        }
        LingManager.getInstance().getLingLog().LOGD("finalUserid: " + putUpdateNameEntity.getData().getAttributes().getUserid());
        new UpdateDevNameModel(requestCallback).execute(putUpdateNameEntity);
    }

    public BrandStatusEntity getBrandStatusEntity(ResultGetBrandEntity getBrandEntity) {
        BrandStatusEntity brandStatusEntity = new BrandStatusEntity();
        List<BrandStatusEntity.Brand> brand_list = new ArrayList<>();
        for (BrandBean brandBean : getBrandEntity.getData()) {
            brand_list.add(new BrandStatusEntity.Brand(brandBean.getKey(), brandBean.getName(), brandBean.getCode()));
        }
        brandStatusEntity.setBrand_list(brand_list);
        return doSoreBrandList(brandStatusEntity);
    }

    public DevicesEntity getDevicesEntity(String userid, ResultGetDevicesEntity getBrandEntity) {
        DevicesEntity devicesEntity = new DevicesEntity();
        devicesEntity.setUserid(userid);
        devicesEntity.setBrand_list(getBrandEntity.getData());
        return devicesEntity;
    }

    /**
     * 对品牌列表排序，将已绑定的品牌放在集合最前面
     */
    public BrandStatusEntity doSoreBrandList(BrandStatusEntity brandStatusEntity) {
        Comparator<BrandStatusEntity.Brand> comparator = new Comparator<BrandStatusEntity.Brand>() {
            @Override
            public int compare(BrandStatusEntity.Brand brand1, BrandStatusEntity.Brand brand2) {
                // 以品牌的绑定状态排序，已绑定的品牌在List前面
                if (brand1.getBrand_status() != brand2.getBrand_status()) {
//                    if (brand1.getBrand_status() == 0 || brand1.getBrand_status() == 2) {
//                        return -1;
//                    } else if (brand2.getBrand_status() == 0 || brand2.getBrand_status() == 2) {
//                        return 1;
//                    }
                    return brand2.getBrand_status() - brand1.getBrand_status();
                } else if (!brand1.getBrand_id().equals(brand2.getBrand_id())) {
                    // 以品牌的名称排序
                    return brand1.getBrand_name().compareTo(brand2.getBrand_name());
                } else {
                    // 以品牌的id排序
                    return brand1.getBrand_id().compareTo(brand2.getBrand_id());
                }
            }
        };
        Collections.sort(brandStatusEntity.getBrand_list(), comparator);
        return brandStatusEntity;
    }

    /**
     * 合成幻腾授权页面的请求地址
     */
    public String getPhantomAuthorizedUrl() {
        Map<String, Object> mParams = new HashMap<String, Object>();
        mParams.put("client_id", PHANTON_APP_ID);
        mParams.put("redirect_uri", OAUTH_REDIRECT_URI);
        mParams.put("response_type", "code");
        mParams.put("scope", PHANTON_SCOPE);//PHANTON_SCOPE
        return API_PATH_PHANTON_AUTHORIZE + NetWorkUtil.organizeParams(mParams);
    }

    /**
     * 合成BroadLink授权页面的请求地址
     */
    public String getBroadLinkAuthorizedUrl() {
        Map<String, Object> mParams = new HashMap<String, Object>();
        mParams.put("client_id", BROADLINK_CLIENT_ID);
        mParams.put("redirect_uri", OAUTH_REDIRECT_URI);
        mParams.put("response_type", "code");
        return BROADLINK_API_PATH + NetWorkUtil.organizeParams(mParams);
    }

    public void queryBLKey(IRequestCallback requestCallback) {
        new UpdataBLGetAccessKeyModel(requestCallback).queryKey();
    }

    public void getBrandConfigureModel(IRequestCallback requestCallback) {
        new GetBrandConfigureModel(requestCallback).getBrandConfigure();
    }

    public void checkDevicesName(DevicesEntity devicesEntity) {
        if (devicesEntity == null || devicesEntity.getBrand_list() == null || !(devicesEntity.getBrand_list().size() > 0))
            return;
        List<DeviceBean> repeatDev = new ArrayList<>();

        List<DeviceBean> allDev = new ArrayList<>();

        for (BrandBean brandBean : devicesEntity.getBrand_list()) {
            if (brandBean.getVal() != null && brandBean.getVal().size() > 0) {
                allDev.addAll(brandBean.getVal());
            }
        }
        for (int i = 0; i < allDev.size(); i++) {
            for (int j = 0; j < allDev.size(); j++) {
                if (i != j) {
                    boolean equals = allDev.get(i).getDevice_name().equals(allDev.get(j).getDevice_name());
                    if (equals) {
                        repeatDev.add(allDev.get(i));
                    }
                }
            }
        }
        devicesEntity.setRepeatDev(repeatDev);
    }

    public boolean hasSameName(String devName, DevicesEntity devicesEntity) {
        if (devicesEntity == null || devicesEntity.getBrand_list() == null || !(devicesEntity.getBrand_list().size() > 0))
            return false;
        for (BrandBean brandBean : devicesEntity.getBrand_list()) {
            if (brandBean.getVal() != null && brandBean.getVal().size() > 0) {
                for (DeviceBean deviceBean : brandBean.getVal()) {
                    if (devName.equals(deviceBean.getDevice_name())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasSameName(String devName, List<String> names) {
        if (names == null || !(names.size() > 0))
            return false;
        for (String name : names) {
            if (devName.equals(name)) {
                return true;
            }
        }
        return false;
    }


    public List<String> getRandomName(List<String> strings, DevicesEntity devicesEntity) {
        int flag = 1;
        List<String> names = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String name = names.get(i) + flag++;
            while (hasSameName(name, devicesEntity)) {
                name += flag++;
            }
            names.add(name);
        }
        return names;
    }


}