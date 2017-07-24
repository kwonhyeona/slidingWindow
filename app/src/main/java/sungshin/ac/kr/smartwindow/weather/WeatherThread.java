//package sungshin.ac.kr.smartwindow.weather;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import sungshin.ac.kr.smartwindow.application.NetworkService;
//
///**
// * Created by gominju on 2017. 7. 24..
// */
//
//public class WeatherThread extends Thread {
//    private final static String TAG = "mytag";
//    private Context mContext;
//    private WeatherRepo weatherRepo;
//    private Handler handler;
//
//    private int version = 1;
//    private String lat;
//    private String lon;
//
//    public WeatherThread(Handler handler, Context mContext, double lat, double lon) {
//        this.mContext = mContext;
//        this.lat = String.valueOf(lat);
//        this.lon = String.valueOf(lon);
//        this.handler = handler;
//    }
//
//    public WeatherThread(Context mContext, double lat, double lon) {
//        this.mContext = mContext;
//        this.lat = String.valueOf(lat);
//        this.lon = String.valueOf(lon);
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        Retrofit client = new Retrofit.Builder().baseUrl("http://apis.skplanetx.com/").addConverterFactory(GsonConverterFactory.create()).build();
//        NetworkService service = client.create(NetworkService.class);
//        Call<WeatherRepo> call = service.get_Weather_retrofit(version, lat, lon);
//        call.enqueue(new Callback<WeatherRepo>() {
//            @Override
//            public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
//                if (response.isSuccessful()) {
//                    weatherRepo = response.body();
//                    Log.d(TAG, "response.raw :" + response.raw());
//                    if (weatherRepo.getResult().getCode().equals("9200")) { // 9200 = 성공
//                        Weather.getInstance().setTemperature(weatherRepo.getWeather().getHourly().get(0).getTemperature().getTc());
//                        Weather.getInstance().setCloud(weatherRepo.getWeather().getHourly().get(0).getSky().getName());
//                        Weather.getInstance().setWind_direction(weatherRepo.getWeather().getHourly().get(0).getWind().getWdir());
//                        Weather.getInstance().setWind_speed(weatherRepo.getWeather().getHourly().get(0).getWind().getWspd());
//                        Weather.getInstance().setIcon(weatherRepo.getWeather().getHourly().get(0).getSky().getCode());
//
//                        Log.i("mytag", "weather thread, temp (singletone) : " + Weather.getInstance().getTemperature());
//
//                        Message msg = Message.obtain();
//                        msg.what = 0;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("weather", "weather");
//                        bundle.putSerializable("temp", Weather.getInstance().getTemperature());
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
//                    } else {
//                        Log.e(TAG, "요청 실패 :" + weatherRepo.getResult().getCode());
//                        Log.e(TAG, "메시지 :" + weatherRepo.getResult().getMessage());
//                    }
//                } else {
//                    Log.e(TAG, "response 실패 ");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherRepo> call, Throwable t) {
//                Log.e(TAG, "날씨정보 불러오기 실패 :" + t.getMessage());
//                Log.e(TAG, "요청 메시지 :" + call.request());
//            }
//        });
//    }
//}
