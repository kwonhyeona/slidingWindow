package sungshin.ac.kr.smartwindow.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungshin.ac.kr.smartwindow.R;
import sungshin.ac.kr.smartwindow.application.NetworkService;

public class TestActivity extends AppCompatActivity {
    private TextView tv_temp;
    private WeatherThread weatherThread;
    private Handler handler;
    private double latitude, longitude;
    private LocationManager locationManager;        //  LocationManager는 디바이스의 위치를 가져오는데 사용하는 매니저
    private LocationListener locationListener;      // locationListener는 위치가 변할때 마다 또는 상태가 변할 때마다 위치를 가져오는 리스너
    //    private boolean canReadLocation = false;
    private static int REQUEST_CODE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tv_temp = (TextView) findViewById(R.id.tv_temp);

        // 사용자의 위치 수신을 위한 세팅
//        settingGPS();
        //사용자의 현재 위치
//        Location userLocation = getMyLocation();

//        if (userLocation != null) {
//            latitude = userLocation.getLatitude();
//            longitude = userLocation.getLongitude();
//        }
//        Log.i("mytag", "latitude : " + latitude + ", longitude : " + longitude);

        latitude = 37.4870600000;   // 임의로 서울 강남구 넣어둠
        longitude = 127.0460400000;

        weatherThread = new WeatherThread(getApplicationContext(), latitude, longitude);
        weatherThread.setDaemon(true);
        weatherThread.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle bundle = msg.getData();
                    String temp = bundle.getString("temp");

                    tv_temp.setText(temp);
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

        private int version = 1;
        private String lat;
        private String lon;

        private WeatherThread(Context mContext, double lat, double lon) {
            this.mContext = mContext;
            this.lat = String.valueOf(lat);
            this.lon = String.valueOf(lon);
        }

        @Override
        public void run() {
            super.run();
            Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
            NetworkService service = client.create(NetworkService.class);
            Call<WeatherRepo> call = service.get_Weather_retrofit(version, lat, lon);
            call.enqueue(new Callback<WeatherRepo>() {
                @Override
                public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                    if (response.isSuccessful()) {
                        weatherRepo = response.body();
                        Log.d(TAG, "response.raw :" + response.raw());
                        if (weatherRepo.getResult().getCode().equals("9200")) { // 9200 = 성공
                            Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
                            Weather.getInstance().setCloud(weatherRepo.getWeather().getHourly().get(0).getSky().getName());
                            Weather.getInstance().setWind_direction(weatherRepo.getWeather().getHourly().get(0).getWind().getWdir());
                            Weather.getInstance().setWind_speed(weatherRepo.getWeather().getHourly().get(0).getWind().getWspd());
                            Weather.getInstance().setIcon(weatherRepo.getWeather().getHourly().get(0).getSky().getCode());

                            Log.i("mytag", "weather thread, temp (singletone) : " + Weather.getInstance().getTemperature());

                            Message msg = Message.obtain();
                            msg.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putString("weather", "weather");
                            bundle.putSerializable("temp", Weather.getInstance().getTemperature());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        } else {
                            Log.e(TAG, "요청 실패 :" + weatherRepo.getResult().getCode());
                            Log.e(TAG, "메시지 :" + weatherRepo.getResult().getMessage());
                        }
                    } else {
                        Log.e(TAG, "response 실패 ");
                    }
                }

                @Override
                public void onFailure(Call<WeatherRepo> call, Throwable t) {
                    Log.e(TAG, "날씨정보 불러오기 실패 :" + t.getMessage());
                    Log.e(TAG, "요청 메시지 :" + call.request());
                }
            });
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


