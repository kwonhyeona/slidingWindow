package sungshin.ac.kr.smartwindow.weather;

/**
 * Created by gominju on 2017. 8. 5..
 */

public class Dust {
    private String value;
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

    public static Dust dust;
    public static Dust getInstance() {
        if (dust == null) {
            dust = new Dust();
        }
        return  dust;
    }
}
