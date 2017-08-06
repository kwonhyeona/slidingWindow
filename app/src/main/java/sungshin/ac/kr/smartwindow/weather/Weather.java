package sungshin.ac.kr.smartwindow.weather;

/**
 * Created by gominju on 2017. 7. 24..
 */

public class Weather {
    // 필요시 생성자 추가하기
    private String temperature;
    private String dust_grade;
    private String dust_value;
    private String uv;
    private String cloud;
    private String wind_direction;
    private String wind_speed;
    private String icon;
    private String humidity;
    private String precipitation_type;  //현재 강수 타입 (0 : 현상없음 / 1 : 비 / 2 : 비 + 눈 / 3 : 눈)
    private String precipitation_sinceOntime;

    private static Weather weather;

    public static Weather getInstance() {
        if (weather == null) {
            weather = new Weather();
        }
        return weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDust_grade() {
        return dust_grade;
    }

    public void setDust_grade(String dust_grade) {
        this.dust_grade = dust_grade;
    }

    public String getDust_value() {
        return dust_value;
    }

    public void setDust_value(String dust_value) {
        this.dust_value = dust_value;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPrecipitation_type() {
        return precipitation_type;
    }

    public void setPrecipitation_type(String precipitation_type) {
        this.precipitation_type = precipitation_type;
    }

    public String getPrecipitation_sinceOntime() {
        return precipitation_sinceOntime;
    }

    public void setPrecipitation_sinceOntime(String precipitation_sinceOntime) {
        this.precipitation_sinceOntime = precipitation_sinceOntime;
    }
}