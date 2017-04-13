package mree.cloud.music.player.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.adapter.SettingsAdapter;
import mree.cloud.music.player.app.bill.google.GoogleInAppBilling;
import mree.cloud.music.player.app.bill.google.IabHelper;
import mree.cloud.music.player.app.bill.google.IabResult;
import mree.cloud.music.player.app.bill.google.Purchase;
import mree.cloud.music.player.app.report.Firebase;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.common.ref.SettingType;


public class SettingsActivity extends AppCompatActivity{
    private static final String TAG = "SettingsActivity";
    private static final GoogleInAppBilling inAppBilling = new GoogleInAppBilling();
    static String ITEM_SKU = "remove_ads";
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener(){
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase){
            if (result.isFailure()) {
                CmpDeviceService.getPreferencesService().setAdState(false);
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                Long l = System.currentTimeMillis();
                CmpDeviceService.getPreferencesService().setAdDate(l.toString());
                Firebase.writeFirebase(l.toString());
                CmpDeviceService.getPreferencesService().setAdState(true);
                Log.e(TAG, purchase.getSku() + " success");
            }

        }
    };
    boolean isReady = false;
    private IabHelper iabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView settingsList = (ListView) findViewById(R.id.settingsList);
        final SettingsAdapter adapter = new SettingsAdapter(this, R.layout.layout_settings_row);
        adapter.addAll(SettingType.values());

        settingsList.setAdapter(adapter);
        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                SettingType settingType = adapter.getItem(position);
                switch (settingType) {
                    case ACCOUNT:
                        Intent accountSet = new Intent(SettingsActivity.this, AccountSetActivity
                                .class);
                        startActivity(accountSet);
                        //finish();
                        break;
                    case PLAYER:
                        Intent playerSet = new Intent(SettingsActivity.this,
                                PlayerSettingActivity.class);
                        startActivity(playerSet);
                        //finish();
                        break;
                    case DATA:
                        Intent dataSet = new Intent(SettingsActivity.this,
                                DataSettingActivity.class);
                        startActivity(dataSet);
                        //finish();
                        break;
                    case DISPLAY:
//                        CmpDeviceService.getPreferencesService().setDeviceToken("");
//                        Intent intent = new Intent(SettingsActivity.this, NavigationActivity
// .class);
//                        startActivity(intent);
//                        finish();
                        break;
                }
            }
        });

        Button btnPurchase = (Button) findViewById(R.id.inAppPurchaseBtn);
        btnPurchase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbReference = db.getReference("IAB");
                    dbReference.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            String key = dataSnapshot.getValue(String.class);
                            key.replace(" ", "");
                            iabHelper = new IabHelper(SettingsActivity.this, key);
                            iabHelper.enableDebugLogging(true, TAG);
                            iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
                                public void onIabSetupFinished(IabResult result){
                                    if (!result.isSuccess()) {
                                        Log.e(TAG, "In-app Billing setup failed: " + result);
                                    } else {
                                        Log.e(TAG, "In-app Billing is set up OK");
                                        isReady = true;
                                        iabHelper.launchPurchaseFlow(SettingsActivity.this, ITEM_SKU, 2001,
                                                mPurchaseFinishedListener, "mypurchasetoken" + System
                                                        .currentTimeMillis());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError error){
                            // Failed to read value
                            Log.e(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            btnPurchase.setVisibility(View.VISIBLE);
        } else {
            btnPurchase.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    Toast.makeText(getApplicationContext(), sku + "  successed", Toast
                            .LENGTH_LONG).show();
                    Long l = System.currentTimeMillis();
                    CmpDeviceService.getPreferencesService().setAdDate(l.toString());
                    Firebase.writeFirebase(l.toString());
                    CmpDeviceService.getPreferencesService().setAdState(true);
                    Log.e(TAG, sku + " success");
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage() + "");
                }
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (inAppBilling.purchaseService != null) {
            unbindService(inAppBilling.purchaseServiceConnection);
        }
        if (iabHelper != null) {
            iabHelper.dispose();
        }
        iabHelper = null;
    }
}
