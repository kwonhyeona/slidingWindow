package sungshin.ac.kr.smartwindow.application;

import android.app.Application;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc on 2017-05-09.
 */

public class ApplicationController extends Application {
    static Context context;

    private static ApplicationController instance;    // 먼저 어플리케이션 인스턴스 객체를 하나 선언

    private static String baseUrl = "http://52.79.177.153:3000";  // 베이스 url 초기화



    public static Context getContext() {
        return context; //자원을 반환.//
    }

    public static ApplicationController getInstance() {
        return instance;
    }    // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서
    //자유롭게 사용가능합니다.



    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this; //인스턴스 객체 초기화
        buildService();
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}