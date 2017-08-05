package sungshin.ac.kr.smartwindow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sungshin.ac.kr.smartwindow.AlarmUtils;

/**
 * Created by gominju on 2017. 7. 29..
 */

public class AlarmBraodCastReciever extends BroadcastReceiver {
    public static boolean isLaunched = false;   // AlarmManager가 한번만 실행 되도록 하기 위한 플래그 값

    @Override
    public void onReceive(Context context, Intent intent) {
        isLaunched = true;

        AlarmUtils.getInstance().startOneMinuteAlram(context);
    }
}