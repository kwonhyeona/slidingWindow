package sungshin.ac.kr.smartwindow.receiver;

import android.content.Context;
import android.content.Intent;

import sungshin.ac.kr.smartwindow.AlarmUtils;

/**
 * Created by gominju on 2017. 7. 29..
 */

public class AlarmOneMinuteBroadcastReceiver extends AlarmBraodCastReciever {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AlarmUtils.getInstance().startOneMinuteAlram(context);
    }
}
