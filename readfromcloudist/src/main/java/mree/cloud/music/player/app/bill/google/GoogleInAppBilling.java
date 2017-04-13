package mree.cloud.music.player.app.bill.google;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;


/**
 * Created by eercan on 07.12.2016.
 */

public class GoogleInAppBilling {
    public IInAppBillingService purchaseService;
    public ServiceConnection purchaseServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            purchaseService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            purchaseService = IInAppBillingService.Stub.asInterface(service);
        }
    };
}
