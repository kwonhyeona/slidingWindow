package sungshin.ac.kr.smartwindow.application;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import sungshin.ac.kr.smartwindow.weather.WeatherRepo;

/**
 * Created by gominju on 2017. 7. 24..
 */

public interface NetworkService {
    // 기본 날씨 정보
    @Headers({"Accept: application/json", "appKey: d8fca7ea-2801-3350-90e2-7c7190135f39"})
    @GET("weather/current/minutely")
    Call<WeatherRepo> getBasicWeather(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);

    // 미세먼지
    @Headers({"Accept: application/json", "appKey: d8fca7ea-2801-3350-90e2-7c7190135f39"})
    @GET("weather/weather/dust")
    Call<WeatherRepo> getDust(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);

}
