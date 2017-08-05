package sungshin.ac.kr.smartwindow.application;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import sungshin.ac.kr.smartwindow.weather.DustRepo;
import sungshin.ac.kr.smartwindow.weather.WeatherRepo;

/**
 * Created by gominju on 2017. 7. 24..
 */

// appkey(minju) : d8fca7ea-2801-3350-90e2-7c7190135f39

public interface NetworkService {
    // 기본 날씨 정보
    @Headers({"Accept: application/json", "appKey: 05a07ead-ae2c-350a-b4bb-05c54a651176"})
    @GET("weather/current/hourly")
    Call<WeatherRepo> getBasicWeather(@Query("version") int version,
                                      @Query("city") String city, @Query("county") String county, @Query("village") String village);

//    Call<WeatherRepo> getBasicWeather(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon,
//                                      @Query("city") String city, @Query("county") String county, @Query("village") String village,
//                                      @Query("stnid") int stnid);

    // 미세먼지
    @Headers({"Accept: application/json", "appKey: 05a07ead-ae2c-350a-b4bb-05c54a651176"})
    @GET("weather" +
            "/dust")
    Call<DustRepo> getDust(@Query("version") int version,
                           @Query("lat") String lat, @Query("lon") String lon);


}
