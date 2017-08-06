package sungshin.ac.kr.smartwindow;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungshin.ac.kr.smartwindow.application.ApplicationController;
import sungshin.ac.kr.smartwindow.application.NetworkService;
import sungshin.ac.kr.smartwindow.fragment.OpenResult;
import sungshin.ac.kr.smartwindow.fragment.PageAdapter;
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
    private String api_dust_grade;      // 현재 미세먼지 등급
    private String api_dust_value;      // 현재 미세먼지 값
    private TextView tv_dust, tv_dust_grade, tv_temp, tv_humidity;
    private Switch openSwitch;
    private boolean aWeather = false, aDust = false;
    private CircleAnimIndicator circleAnimIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
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
        initIndicaotor();
        initWeatherData();  // 기온, 강수, 습도 받아오기
        initDustData();     // 미세먼지 받아오기

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                circleAnimIndicator.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * state 종류
                 * 0 : SCROLL_STATE_IDLE
                 * 1 : SCROLL_STATE_DRAGGING
                 * 2 : SCROLL_STATE_SETTLING
                 */
            }
        });
        viewPager.setCurrentItem(0);

        openSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "Switch is : " + b);
                final boolean isOpen = b;
                //// TODO: 2017. 8. 5. 서버랑 문 여닫아라 통신하기
                int openValue = 0;
                if(!b){ openValue = 1; }

                Call<OpenResult> sendOpenValue = networkService.sendOpenValue(openValue);
                sendOpenValue.enqueue(new Callback<OpenResult>() {
                    @Override
                    public void onResponse(Call<OpenResult> call, Response<OpenResult> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "response.body : " + response.body());
                            if(response.body().message.equals("ok")) {
                                // TODO: 성공했을 때 작업
                                if(isOpen) {
                                    Toast.makeText(getBaseContext(), "문이 열렸습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getBaseContext(), "문이 닫혔습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<OpenResult> call, Throwable t) {
                        Log.d(TAG, "response.body : 실패");
                    }
                });
            }
        });
    }

    private void init() {
        // 네트워크 초기화
        networkService = ApplicationController.getInstance().getNetworkService();
        openSwitch = (Switch) findViewById(R.id.switch_main_open);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        circleAnimIndicator = (CircleAnimIndicator)findViewById(R.id.circleAnimIndicator);
        FirebaseApp.initializeApp(this);
        Log.d(TAG, "Token : " + FirebaseInstanceId.getInstance().getToken());
    }

    private void initIndicaotor() {
        //원사이의 간격
        circleAnimIndicator.setItemMargin(15);
        //애니메이션 속도
        circleAnimIndicator.setAnimDuration(300);
        //indecator 생성
        circleAnimIndicator.createDotPanel(4, R.drawable.circle_white, R.drawable.circle_blue);
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
                        Weather.getInstance().setTmax(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTmax());
                        Weather.getInstance().setTmin(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTmin());

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
        // 미세먼지 상태가 변했을때 보내준다
        aDust = true;

        if (api_dust_grade.equals("약간나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, getText(R.string.push_dust).toString(), getText(R.string.dust_1).toString());
        }
        else if (api_dust_grade.equals("나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, "dust", "dust_bad");
        }
        else if (api_dust_grade.equals("매우나쁨")) {
            Push.getInstance().sendPush(MainActivity.this, "dust", "dust_so_much_bad");
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
