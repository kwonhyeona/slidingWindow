package sungshin.ac.kr.smartwindow.weather;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gominju on 2017. 8. 5..
 */

public class DustRepo {
    @SerializedName("result")
    Result result;
    @SerializedName("weather")
    weather weather;

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

    public class weather {
        @SerializedName("dust")
        public List<dust> dust = new ArrayList<>();

        public List<dust> getDust() {
            return dust;
        }

        public class dust {
            @SerializedName("pm10")
            public pm10 pm10;

            public class pm10 {
                @SerializedName("value")
                private String value;
                @SerializedName("grade")
                private String grade;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getGrade() {
                    return grade;
                }

                public void setGrade(String grade) {
                    this.grade = grade;
                }
            }

            public dust.pm10 getPm10() {return pm10;}
        }

    }

    public Result getResult() {
        return result;
    }

    public weather getWeather() {
        return weather;
    }
}