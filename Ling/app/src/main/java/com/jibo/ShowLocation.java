package com.jibo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by mhz小志 on 2017/5/7.
 */

public class ShowLocation extends AppCompatActivity {
    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    private Button btn_show;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        init();//关联控件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);

        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                getLocation(location);//得到当前经纬度并开启线程去反向地理编码
            } else {
                tv_show.setText("暂时无法获得当前位置");
            }
        } else {//不存在位置提供器的情况

        }
    }

    private void init() {
        btn_show = (Button) findViewById(R.id.btn_show_location);
        tv_show = (TextView) findViewById(R.id.tv_location_show);
    }

    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
        Toast.makeText(this, latitude + "　／　" + longitude, Toast.LENGTH_SHORT).show();
        tv_show.setText(latitude + "　／　" + longitude);
//        String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location=" + latitude + "," + longitude + "&output=json&pois=0";
//        new MyAsyncTask(url).execute();
    }

    /**
     * 判断是否有可用的内容提供器
     *
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(ShowLocation.this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

//    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
//        String url = null;//要请求的网址
//        String str = null;//服务器返回的数据
//        String address = null;
//
//        public MyAsyncTask(String url) {
//            this.url = url;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            str = GetHttpConnectionData.getData(url);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            try {
//                str = str.replace("renderReverse&&renderReverse", "");
//                str = str.replace("(", "");
//                str = str.replace(")", "");
//                JSONObject jsonObject = new JSONObject(str);
//                JSONObject address = jsonObject.getJSONObject("result");
//                String city = address.getString("formatted_address");
//                String district = address.getString("sematic_description");
//                tv_show.setText("当前位置：" + city + district);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            super.onPostExecute(aVoid);
//        }
//    }

}
