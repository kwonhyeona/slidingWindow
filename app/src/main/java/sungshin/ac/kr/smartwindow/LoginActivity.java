package sungshin.ac.kr.smartwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:a242f46c-3299-47ff-8dd9-b5958a06b24f", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        // Cognito Sync 클라이언트를 초기화합니다
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.AP_NORTHEAST_2, // 리전
                credentialsProvider);

        // 데이터 세트에서 레코드를 만들고 서버와 동기화합니다
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCzallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //핸들러 코드는 다음과 같습니다
            }
        });
    }
}
