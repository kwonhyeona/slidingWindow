package sungshin.ac.kr.smartwindow.application;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import sungshin.ac.kr.smartwindow.weather.WeatherRepo;

/**
 * Created by gominju on 2017. 7. 24..
 */
//@Headers({"Accept: application/json","access_token: ~~~~","appKey: ~~~"})

public interface NetworkService {
    @Headers({"Accept: application/json", "appKey: d8fca7ea-2801-3350-90e2-7c7190135f39"})
    @GET("weather/current/hourly")
    Call<WeatherRepo> get_Weather_retrofit(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);

}
