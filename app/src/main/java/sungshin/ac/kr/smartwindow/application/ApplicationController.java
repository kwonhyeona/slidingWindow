package sungshin.ac.kr.smartwindow.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc on 2017-05-09.
 */

public class ApplicationController extends Application {
    static Context context;
    private static ApplicationController instance;    // 먼저 어플리케이션 인스턴스 객체를 하나 선언
    private static String baseUrl = "http://apis.skplanetx.com/";  // 베이스 url 초기화
    private NetworkService networkService;

    public static Context getContext() {
        return context; //자원을 반환.//
    }

    public static ApplicationController getInstance() {
        return instance;
    }
    public NetworkService getNetworkService() {
        return networkService;
    }



    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

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
        networkService = retrofit.create(NetworkService.class);
    }
}