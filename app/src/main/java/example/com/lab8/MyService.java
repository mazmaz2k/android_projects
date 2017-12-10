package example.com.lab8;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

public class MyService extends IntentService {

    private boolean flag = false;

    public MyService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        flag = true;
        while(flag) {
            try {
                Thread.sleep(5000);
                sendSMSTo(intent.getStringExtra("number"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        flag = false;
        super.onDestroy();
    }

    private void sendSMSTo(String number) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, "You got a new message!", null, null);
    }

}
