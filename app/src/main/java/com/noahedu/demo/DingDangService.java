package com.noahedu.demo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DingDangService extends Service {
    private static final String TAG = "DingDangService";
    private static final String ANSWER_BROAST_ACTION = "com.robosys.dingdang.all.answer";

    private static AnswerBroatcast answerBroatcast = null;

    public DingDangService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "start service");
        answerBroatcast = new AnswerBroatcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANSWER_BROAST_ACTION);
        registerReceiver(answerBroatcast, filter);

        return super.onStartCommand(intent, flags, startId);
    }

    public class AnswerBroatcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            String value = intent.getStringExtra("value");

            Log.i(TAG, "key:" + key);
            Log.i(TAG, "value:" + value);
            Toast.makeText(getApplicationContext(), "key="+key+", value="+value, Toast.LENGTH_SHORT).show();
        }
    }
}
