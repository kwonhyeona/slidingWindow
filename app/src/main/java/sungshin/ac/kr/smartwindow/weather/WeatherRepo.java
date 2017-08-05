package sungshin.ac.kr.smartwindow.weather;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gominju on 2017. 7. 24..
 */

public class WeatherRepo {

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

        public List<minutely> minutely = new ArrayList<>();

        public List<minutely> getMinutely() {
            return minutely;
        }

        public class minutely {
            @SerializedName("sky")
            Sky sky;
            @SerializedName("precipitation")
            precipitation precipitation;
            @SerializedName("temperature")
            temperature temperature;
            @SerializedName("wind")
            wind wind;
//            @SerializedName("humidity")
//            humidity humidity;

            public class Sky {
                @SerializedName("name")
                String name;
                @SerializedName("code")
                String code;

                public String getName() {
                    return name;
                }

                public String getCode() {
                    return code;
                }
            }

            public class precipitation { // 강수 정보
                @SerializedName("sinceOntime")
                String sinceOntime; // 강우
                @SerializedName("type")
                String type; //0 :없음 1:비 2: 비/눈 3: 눈

                public String getSinceOntime() {
                    return sinceOntime;
                }

                public String getType() {
                    return type;
                }
            }

            public class temperature {
                @SerializedName("tc")
                String tc; // 현재 기온

                public String getTc() {
                    return tc;
                }
            }

            public class wind { // 바람
                @SerializedName("wdir")
                String wdir;
                @SerializedName("wspd")
                String wspd;

                public String getWdir() {
                    return wdir;
                }

                public String getWspd() {
                    return wspd;
                }
            }

            public Sky getSky() {
                return sky;
            }

            public minutely.precipitation getPrecipitation() {
                return precipitation;
            }

            public minutely.temperature getTemperature() {
                return temperature;
            }

            public minutely.wind getWind() {
                return wind;
            }

            public String humidity;

            public String getHumidity() {
                return humidity;
            }

            //public minutely.
        }
    }

    public Result getResult() {
        return result;
    }

    public weather getWeather() {
        return weather;
    }

}

