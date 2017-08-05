package sungshin.ac.kr.smartwindow;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
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
    private static final String TAG = "MainActivity";
    //Back 키 두번 클릭 관련 변수
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    private NetworkService networkService;
    private int version;
    private String city, county, village, lat, lon;
    private double latitude, longitude;
    private WeatherRepo weatherRepo;
    private DustRepo dustRepo;
    private String api_temp;            // 현재 온도
    private String api_humidity;        // 현재 습도
    private String api_precipitation_type;   // 현재 강수 타입 (0 : 현상없음 / 1 : 비 / 2 : 비 + 눈 / 3 : 눈)
    private String api_precipitation_value;  // 현재 강우량 or 적설량
    private String api_dust_grade;      // 현재 미세먼지 등급
    private String api_dust_value;      // 현재 미세먼지 수치
    private TextView tv_dust, tv_dust_grade, tv_temp, tv_humidity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        Log.d(TAG, "Token : " + FirebaseInstanceId.getInstance().getToken());

        viewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        pagerAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * state 종류
                 * 0 : SCROLL_STATE_IDLE
                 * 1 : SCROLL_STATE_DRAGGING
                 * 2 : SCROLL_STATE_SETTLING
                 */

                Log.d(TAG, "onPageScrollStateChanged state: " + state);
            }
        });
        viewPager.setCurrentItem(0);

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
    }


    private void init() {
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_dust = (TextView) findViewById(R.id.tv_dust);
        tv_dust_grade = (TextView) findViewById(R.id.tv_dust_grade);
        networkService = ApplicationController.getInstance().getNetworkService();
    }


    private void initWeatherData() {
        Call<WeatherRepo> resultCall = networkService.getBasicWeather(version, city, county, village);
        resultCall.enqueue(new Callback<WeatherRepo>() {
            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
                if (response.isSuccessful()) {
                    weatherRepo = response.body();
                    if (weatherRepo.getResult().getCode().equals("9200")) { // 9200 = 성공
                        Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
                        Weather.getInstance().setHumidity(weatherRepo.getWeather().getHourly().get(0).getHumidity());
                        Weather.getInstance().setPrecipitation_type(weatherRepo.getWeather().getHourly().get(0).getPrecipitation().getType());
                        Weather.getInstance().setPrecipitation_sinceOntime(weatherRepo.getWeather().getHourly().get(0).getPrecipitation().getSinceOntime());

                        api_temp = Weather.getInstance().getTemperature();
                        api_humidity = Weather.getInstance().getHumidity();
                        api_precipitation_type = Weather.getInstance().getPrecipitation_type();
                        api_precipitation_value = Weather.getInstance().getPrecipitation_sinceOntime();

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
        tv_temp.setText(api_temp);
        tv_humidity.setText(api_humidity);
    }

    private void initDustData() {
        Log.i("mytag", "initDustData");

        Call<DustRepo> resultCall = networkService.getDust(version, lat, lon);
        resultCall.enqueue(new Callback<DustRepo>() {
            @Override
            public void onResponse(Call<DustRepo> call, Response<DustRepo> response) {
                if (response.isSuccessful()) {
                    dustRepo = response.body();
                    if (dustRepo.getResult().getCode().equals("9200")) {
                        Dust.getInstance().setValue(dustRepo.getWeather().getDust().get(0).getPm10().getValue());
                        Dust.getInstance().setGrade(dustRepo.getWeather().getDust().get(0).getPm10().getGrade());

                        api_dust_grade = Dust.getInstance().getGrade();
                        api_dust_value = Dust.getInstance().getValue();

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
        tv_dust.setText(api_dust_value);
        tv_dust_grade.setText(api_dust_grade);

        if (api_dust_grade.equals("약간나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, "dust", "dust_little_bad");
        }
        if (api_dust_grade.equals("나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, "dust", "dust_bad");
        }
        if (api_dust_grade.equals("매우나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, "dust", "dust_so_much_bad");
        }
    }

    // 목적 : Back키 두번 연속 클릭 시 앱 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
