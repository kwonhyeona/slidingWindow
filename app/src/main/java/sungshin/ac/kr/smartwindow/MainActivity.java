package sungshin.ac.kr.smartwindow;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import sungshin.ac.kr.smartwindow.fragment.PageAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //Back 키 두번 클릭 관련 변수
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

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
        Log.d(TAG, "Token : " + FirebaseInstanceId.getInstance().getToken());

        viewPager = (ViewPager) findViewById(R.id.viewpager_main_content);
        pagerAdapter = new PageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * state 종류
                 * 0 : SCROLL_STATE_IDLE
                 * 1 : SCROLL_STATE_DRAGGING
                 * 2 : SCROLL_STATE_SETTLING
                 */

                Log.d(TAG, "onPageScrollStateChanged state: " + state);
            }
        });
        viewPager.setCurrentItem(0);

    }

    // 목적 : Back키 두번 연속 클릭 시 앱 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
