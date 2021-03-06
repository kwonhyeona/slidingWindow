package sungshin.ac.kr.smartwindow.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungshin.ac.kr.smartwindow.MainActivity;
import sungshin.ac.kr.smartwindow.R;
import sungshin.ac.kr.smartwindow.application.NetworkService;
import sungshin.ac.kr.smartwindow.service.fcm.Push;
import sungshin.ac.kr.smartwindow.AlarmUtils;
import sungshin.ac.kr.smartwindow.receiver.AlarmBraodCastReciever;

public class TestActivity extends AppCompatActivity {
    private TextView tv_temp, tv_humidity;
    private WeatherThread weatherThread;
    private Handler handler;
    private double latitude, longitude;
    private LocationManager locationManager;        //  LocationManager는 디바이스의 위치를 가져오는데 사용하는 매니저
    private LocationListener locationListener;      // locationListener는 위치가 변할때 마다 또는 상태가 변할 때마다 위치를 가져오는 리스너
    //    private boolean canReadLocation = false;
    private static int REQUEST_CODE_LOCATION = 1;
    private String city, county, village;
    private int stnid;

    public static TestActivity getInstance() {
        return new TestActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);

        if (!AlarmBraodCastReciever.isLaunched) {
            AlarmUtils.getInstance().startOneMinuteAlram(this);
        }

        // 사용자의 위치 수신을 위한 세팅
//        settingGPS();
        //사용자의 현재 위치
//        Location userLocation = getMyLocation();

//        if (userLocation != null) {
//            latitude = userLocation.getLatitude();
//            longitude = userLocation.getLongitude();
//        }
//        Log.i("mytag", "latitude : " + latitude + ", longitude : " + longitude);

        threadProcess();
    }

    public void threadProcess() {
        latitude = 37.4870600000;   // 임의로 서울 강남구 넣어둠
        longitude = 127.0460400000;
        city = "서울";
        county = "성북구";
        village = "돈암동";
        stnid = 108;

        weatherThread = new WeatherThread(TestActivity.this, latitude, longitude, city, county, village, stnid);
        weatherThread.setDaemon(true);
        weatherThread.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle bundle = msg.getData();
                    String temp = bundle.getString("temp");
                    String humidity = bundle.getString("humidity");
                    String dust_value = bundle.getString("dust_value");
                    String dust_grade = bundle.getString("dust_grade");

                    tv_temp.setText(temp);
                    tv_humidity.setText(humidity);
                    Log.i("mytag", "temp : " + temp);

                    if (dust_grade.equals("약간나쁨")) {
                        Push.getInstance().sendPush(TestActivity.this, "dust", "dust_little_bad");
                    }
                    if (dust_grade.equals("나쁨")) {
                        Push.getInstance().sendPush(TestActivity.this, "dust", "dust_bad");
                    }
                    if (dust_grade.equals("매우나쁨")) {
                        Push.getInstance().sendPush(TestActivity.this, "dust", "dust_so_much_bad");
                    }
                }
            }
        };
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("mytag", "longtitude=" + lng + ", latitude=" + lat);
            }
        }
        return currentLocation;
    }

    private void settingGPS() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i("mytag", "gps latitude : " + latitude + ", longitude : " + longitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }


    private class WeatherThread extends Thread {
        private final static String TAG = "mytag";
        private Context mContext;
        private WeatherRepo weatherRepo;
        private DustRepo dustRepo;

        private int version = 1;
        private String lat;
        private String lon;
        private String city;
        private String county;
        private String village;
        private int stnid;

        private WeatherThread(Context mContext, double lat, double lon, String city, String county, String village, int stnid) {
            this.mContext = mContext;
            this.lat = String.valueOf(lat);
            this.lon = String.valueOf(lon);
            this.city = city;
            this.county = county;
            this.village = village;
            this.stnid = stnid;
        }

        @Override
        public void run() {
            super.run();

            Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
            NetworkService service = client.create(NetworkService.class);
            Call<WeatherRepo> call = service.getBasicWeather(version, city, county, village);
            call.enqueue(new Callback<WeatherRepo>() {
                @Override
                public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                    if (response.isSuccessful()) {
                        weatherRepo = response.body();
                        Log.d(TAG, "response.raw :" + response.raw());
                        if (weatherRepo.getResult().getCode().equals("9200")) { // 9200 = 성공
                            Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
                            //Weather.getInstance().setCloud(weatherRepo.getWeather().getHourly().get(0).getSky().getName());
                            //Weather.getInstance().setWind_direction(weatherRepo.getWeather().getHourly().get(0).getWind().getWdir());
                            //Weather.getInstance().setWind_speed(weatherRepo.getWeather().getHourly().get(0).getWind().getWspd());
                            //Weather.getInstance().setIcon(weatherRepo.getWeather().getHourly().get(0).getSky().getCode());
                            Weather.getInstance().setHumidity(weatherRepo.getWeather().getHourly().get(0).getHumidity());

                            Message msg = Message.obtain();
                            msg.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("weather", "weather");
                            bundle.putSerializable("temp", Weather.getInstance().getTemperature());
                            bundle.putSerializable("humidity", Weather.getInstance().getHumidity());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } else {
                            Log.e(TAG, "요청 실패 :" + weatherRepo.getResult().getCode());
                            Log.e(TAG, "메시지 :" + weatherRepo.getResult().getMessage());
                        }
                    } else {
                        Log.e(TAG, "response 실패 " + response.code() + response.errorBody().toString());
                        weatherRepo = response.body();
                        Log.i(TAG, weatherRepo.error.message + ", " + weatherRepo.error.link + ", " + weatherRepo.error.code);
                    }
                }

                @Override
                public void onFailure(Call<WeatherRepo> call, Throwable t) {
                    Log.e(TAG, "날씨정보 불러오기 실패 :" + t.getMessage());
                    Log.e(TAG, "요청 메시지 :" + call.request());
                }
            });

//            Call<DustRepo> result = service.getDust(version, city, county, village);
//            result.enqueue(new Callback<DustRepo>() {
//                @Override
//                public void onResponse(Call<DustRepo> call, Response<DustRepo> response) {
//                    if (response.isSuccessful()) {
//                        dustRepo = response.body();
//                        if (dustRepo.getResult().getCode().equals("9200")) {
//                            Dust.getInstance().setValue(dustRepo.getWeather().getDust().get(0).getPm10().getValue());
//                            Dust.getInstance().setGrade(dustRepo.getWeather().getDust().get(0).getPm10().getGrade());
//
//                            Message msg = Message.obtain();
//                            msg.what = 0;
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("dust_value", Dust.getInstance().getValue());
//                            bundle.putSerializable("dust_grade", Dust.getInstance().getGrade());
//                            msg.setData(bundle);
//                            handler.sendMessage(msg);
//
//                            Log.i(TAG, "미세먼지 양 : " + Dust.getInstance().getValue());
//                            Log.i(TAG, "미세먼지 등급 : " + Dust.getInstance().getGrade());
//                        } else {
//                            Log.e(TAG, "response 실패 " + response.code() + response.errorBody().toString());
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<DustRepo> call, Throwable t) {
//                    Log.e(TAG, "미세먼지 불러오기 실패 : " + t.getMessage());
//                    Log.e(TAG, "미세먼지 요청 메세지 : " + call.request());
//                }
//            });
        }
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == REQUEST_CODE_LOCATION) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // success!
//                Location userLocation = getMyLocation();
//                if (userLocation != null) {
//                    latitude = userLocation.getLatitude();
//                    longitude = userLocation.getLongitude();
//                }
//                canReadLocation = true;
//            } else {
//                // Permission was denied or request was cancelled
//                canReadLocation = false;
//            }
//        }
//    }


}


