package sungshin.ac.kr.smartwindow.weather;

import com.amazonaws.com.google.gson.annotations.SerializedName;

/**
 * Created by gominju on 2017. 8. 5..
 */

public class DustRepo {
    @SerializedName("result")
    WeatherRepo.Result result;
    @SerializedName("weather")
    WeatherRepo.weather weather;

    public class Result {
        @SerializedName("message")
        String message;
        @SerializedName("code")
        String code;

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }
    }



}
