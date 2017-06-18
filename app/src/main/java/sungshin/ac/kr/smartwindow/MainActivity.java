package sungshin.ac.kr.smartwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //추가한 라인
        //주제별로 회원을 선택해서 알림을 보내려는 코드
        //news 라는 주제를 가진 사람에게 알림을 보내는 코드
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();


    }
}
