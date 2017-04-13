package mree.cloud.music.player.app.report;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by eercan on 17.02.2017.
 */

public class Firebase{
    private static final String TAG = Firebase.class.getSimpleName();
    private static final String ADD_ACCOUNT_ID = "add_account";
    private static final String AUTH_ACCOUNT_ID = "auth_account";
    private static final String SCAN_ACCOUNT_ID = "scan_account";
    private static FirebaseAnalytics firebaseAnalytics;

    private static FirebaseDatabase database;
    private static DatabaseReference dbRef;

    public static FirebaseAnalytics getFirebaseAnalytics(){
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(CmpDeviceService.getContext());
        }
        return firebaseAnalytics;
    }

    public static FirebaseDatabase getFirebaseDatabase(){
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getDatabaseReference(){
        if (dbRef == null) {
            dbRef = getFirebaseDatabase().getReference(Build.ID + "__" + CmpDeviceService
                    .getPreferencesService().getFirstOpen());
        }
        return dbRef;
    }


    public static void addAccountTypeLog(SourceType sourceType){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ADD_ACCOUNT_ID);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, sourceType.getDesc());
        getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void addAccountNameLog(String name){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ADD_ACCOUNT_ID);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, name);
        getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    public static void authAccountLog(String name, SourceType sourceType){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, AUTH_ACCOUNT_ID);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, sourceType.getDesc());
        getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        /*Bundle bundle = new Bundle();
        bundle.putString("Name", name);
        bundle.putString("Type", sourceType.getDesc());
        getFirebaseAnalytics().logEvent("auth_account", bundle);*/
    }

    public static void scanAccountLog(String name, SourceType sourceType){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, SCAN_ACCOUNT_ID);
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, name);
        bundle.putString(FirebaseAnalytics.Param.ORIGIN, sourceType.getDesc());
        getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
    }

    public static void playSongLog(String name, SourceType sourceType){
        Bundle bundle = new Bundle();
        bundle.putString("Name", name);
        bundle.putString("Type", sourceType.getDesc());
        getFirebaseAnalytics().logEvent("play_song", bundle);
    }

    public static void writeFirebase(String msg){
        try {
            getDatabaseReference().setValue(msg);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public static void check(){
        try {
            getDatabaseReference().addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    String value = dataSnapshot.getValue(String.class);
                    Log.e(TAG, "Value:" + value);
                    Log.e(TAG, "AdDate:" + CmpDeviceService.getPreferencesService().getAdDate());
                    if (value != null && value.equals(CmpDeviceService.getPreferencesService().getAdDate())) {
                        CmpDeviceService.getPreferencesService().setAdState(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error){
                    // Failed to read value
                    Log.e(TAG, "Failed to read value.", error.toException());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }


}
