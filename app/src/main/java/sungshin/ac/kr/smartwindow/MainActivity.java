package sungshin.ac.kr.smartwindow;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import sungshin.ac.kr.smartwindow.fragment.PageAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //추가한 라인
        //주제별로 회원을 선택해서 알림을 보내려는 코드
        //news 라는 주제를 가진 사람에게 알림을 보내는 코드
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseApp.initializeApp(this);
        Log.d("MainActivity", "Token : " + FirebaseInstanceId.getInstance().getToken());

        viewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        pagerAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

    }


}
