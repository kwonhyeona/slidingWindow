//package sungshin.ac.kr.smartwindow;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
//import com.amazonaws.mobileconnectors.cognito.Dataset;
//import com.amazonaws.mobileconnectors.cognito.SyncConflict;
//import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
//import com.amazonaws.regions.Regions;
//
//import java.util.List;
//
//public class LoginActivity extends AppCompatActivity {
//    private Button btn_login;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        btn_login = (Button) findViewById(R.id.btn_login);
//
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "ap-northeast-2:a242f46c-3299-47ff-8dd9-b5958a06b24f", // 자격 증명 풀 ID
//                Regions.AP_NORTHEAST_2 // 리전
//        );
//        credentialsProvider.clearCredentials();
//        credentialsProvider.clear();
//
//        ClientConfiguration clientConfiguration = new ClientConfiguration();
//        String poolId = "ap-northeast-2_J8kRZVCM6";
//        String clientId =  "6u2728o351o4agq4iqpr71819m";
//        String clientSecret = "p6v7qr2pmaq1sgoor8g7f1nkh0st20j5aapvdv8sgi8ub1a4d3s";
//        CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), poolId, clientId, clientSecret, clientConfiguration);
//
//
//
//        // Cognito Sync 클라이언트를 초기화합니다
//        final CognitoSyncManager syncClient = new CognitoSyncManager(
//                getApplicationContext(),
//                Regions.AP_NORTHEAST_2, // 리전
//                credentialsProvider);
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 데이터 세트에서 레코드를 만들고 서버와 동기화합니다
//                Dataset dataset = syncClient.openOrCreateDataset("myDataset");
//                dataset.put("user_id", "ggmmjj1@gmail.com");
//                dataset.put("user_pwd", "11111111");
//                dataset.synchronize(new Dataset.SyncCallback() {
//                    @Override
//                    public boolean onConflict(Dataset dataset, List<SyncConflict> list) {
//                        Log.i("mytag", "onConflict");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onDatasetDeleted(Dataset dataset, String s) {
//                        Log.i("mytag", "onDatasetDeleted");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onDatasetsMerged(Dataset dataset, List<String> list) {
//                        Log.i("mytag", "onDatasetsMerged");
//                        return false;
//                    }
//
//                    @Override
//                    public void onFailure(DataStorageException e) {
//                        Log.i("mytag", "onFailure");
//                    }
//
//                    @Override
//                    public void onSuccess(Dataset dataset, List newRecords) {
//                        //핸들러 코드는 다음과 같습니다
//                        Log.i("mytag", "onSuccess");
//                    }
//                });
//
//            }
//        });
//
//    }
//}
