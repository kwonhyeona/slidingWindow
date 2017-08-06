package sungshin.ac.kr.smartwindow;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.PushbackInputStream;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;
import sungshin.ac.kr.smartwindow.fragment.PageAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sungshin.ac.kr.smartwindow.application.ApplicationController;
import sungshin.ac.kr.smartwindow.application.NetworkService;
import sungshin.ac.kr.smartwindow.service.fcm.Push;
import sungshin.ac.kr.smartwindow.weather.Dust;
import sungshin.ac.kr.smartwindow.weather.DustRepo;
import sungshin.ac.kr.smartwindow.weather.Weather;
import sungshin.ac.kr.smartwindow.weather.WeatherRepo;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    private NetworkService networkService;
    private int version;
    private String city, county, village, lat, lon;
    private double latitude, longitude;
    private WeatherRepo weatherRepo;
    private DustRepo dustRepo;
    private final static String TAG = "mytag";
//    private String api_humidity;        // 현재 습도
//    private String api_precipitation_type;   // 현재 강수 타입 (0 : 현상없음 / 1 : 비 / 2 : 비 + 눈 / 3 : 눈)
//    private String api_precipitation_value;  // 현재 강우량 or 적설량
    private String api_dust_grade;      // 현재 미세먼지 등급
//    private String api_dust_value;      // 현재 미세먼지 수치
    private boolean aWeather = false, aDust = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 기상정보 받아올 지역 초기화
        version = 1;
        city = "서울";
        county = "성북구";
        village = "돈암동";
        latitude = 37.4870600000;   // 임의로 서울 강남구 넣어둠
        longitude = 127.0460400000;
        lat = String.valueOf(latitude);
        lon = String.valueOf(longitude);

        init();
        initWeatherData();  // 기온, 강수, 습도 받아오기
        initDustData();     // 미세먼지 받아오기

        FirebaseApp.initializeApp(this);
        Log.d("MainActivity", "Token : " + FirebaseInstanceId.getInstance().getToken());

//        if (aWeather && aDust) {
//            pagerAdapter = new PageAdapter(getSupportFragmentManager());
//            viewPager.setAdapter(pagerAdapter);
//            viewPager.setCurrentItem(0);
//        }
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        // 네트워크 초기화
        networkService = ApplicationController.getInstance().getNetworkService();
    }


    private void initWeatherData() {
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        NetworkService service = client.create(NetworkService.class);
        Call<WeatherRepo> resultCall = service.getBasicWeather(version, city, county, village);
        resultCall.enqueue(new Callback<WeatherRepo>() {
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                if (response.isSuccessful()) {
                    weatherRepo = response.body();
                    if (weatherRepo.getResult().getCode().equals("9200")) { // 9200 = 성공
                        Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
                        Weather.getInstance().setHumidity(weatherRepo.getWeather().getHourly().get(0).getHumidity());
                        Weather.getInstance().setPrecipitation_type(weatherRepo.getWeather().getHourly().get(0).getPrecipitation().getType());
                        Weather.getInstance().setPrecipitation_sinceOntime(weatherRepo.getWeather().getHourly().get(0).getPrecipitation().getSinceOntime());

//                        api_temp = Weather.getInstance().getTemperature();
//                        api_humidity = Weather.getInstance().getHumidity();
//                        api_precipitation_type = Weather.getInstance().getPrecipitation_type();
//                        api_precipitation_value = Weather.getInstance().getPrecipitation_sinceOntime();

                        pushWeatherEvent();
                    } else {
                        Log.e(TAG, "요청 실패 :" + weatherRepo.getResult().getCode());
                        Log.e(TAG, "메시지 :" + weatherRepo.getResult().getMessage());
                    }
                } else {
                    Log.e(TAG, "response 실패 " + response.code() + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherRepo> call, Throwable t) {
                Log.e(TAG, "날씨정보 불러오기 실패 :" + t.getMessage() + ", " + call.request());
            }
        });
    }

    private void pushWeatherEvent() {
        aWeather = true;
        updatePager();
    }

    private void initDustData() {
        Log.i("mytag", "initDustData");
        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
        NetworkService service = client.create(NetworkService.class);
        Call<DustRepo> resultCall = service.getDust(version, lat, lon);
        resultCall.enqueue(new Callback<DustRepo>() {
            @Override
            public void onResponse(Call<DustRepo> call, Response<DustRepo> response) {
                if (response.isSuccessful()) {
                    dustRepo = response.body();
                    if (dustRepo.getResult().getCode().equals("9200")) {
                        Dust.getInstance().setValue(dustRepo.getWeather().getDust().get(0).getPm10().getValue());
                        Dust.getInstance().setGrade(dustRepo.getWeather().getDust().get(0).getPm10().getGrade());

                        api_dust_grade = Dust.getInstance().getGrade();
//                        api_dust_value = Dust.getInstance().getValue();

                        Log.i(TAG, "미세먼지 양 : " + Dust.getInstance().getValue());
                        Log.i(TAG, "미세먼지 등급 : " + Dust.getInstance().getGrade());

                        pushDustEvent();
                    }
                } else {
                    Log.e(TAG, "response 실패 " + response.code() + ", " + response.errorBody().toString());

                }
            }

            @Override
            public void onFailure(Call<DustRepo> call, Throwable t) {
                Log.e(TAG, "미세먼지 불러오기 실패 : " + t.getMessage() + ", " + call.request());
            }
        });
    }

    private void pushDustEvent() {
        // 미세먼지 상태가 변했을때 보내준다
        aDust = true;
        if (api_dust_grade.equals("약간나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, getText(R.string.push_dust).toString(), getText(R.string.dust_1).toString());
        }
        if (api_dust_grade.equals("나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, getText(R.string.push_dust).toString(), getText(R.string.dust_2).toString());
        }
        if (api_dust_grade.equals("매우나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, getText(R.string.push_dust).toString(), getText(R.string.dust_3).toString());
        }
        updatePager();
    }

    private void updatePager() {
        if (aWeather && aDust) {
            pagerAdapter = new PageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(0);
        }
    }
}
