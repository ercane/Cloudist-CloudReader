package mree.cloud.music.player.app.act;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.scan.impl.LocalScan;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.utils.Constants;

public class PlayerSettingActivity extends AppCompatActivity {

    private Switch swPlayLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setting);
        swPlayLocal = (Switch) findViewById(R.id.swPlayLocal);
        swPlayLocal.setChecked(CmpDeviceService.getPreferencesService().isLocalInclude());
        swPlayLocal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CmpDeviceService.getPreferencesService().setLocalInclude(isChecked);
                if (isChecked) {
                    checkWritePermission();
                }

            }
        });

        TextView equalizer = (TextView) findViewById(R.id.equalizer);
        equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PlayerSettingActivity.this, EqualizerActivity.class);
                //startActivity(intent);
                try {
                    if (AudioFragment.getMusicService() != null) {
                        Intent i = new Intent(AudioEffect
                                .ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                        i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioFragment.getMusicService()
                                .getPlayer().getAudioSessionId());
                        startActivityForResult(i, 11113);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkWritePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
/*            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {*/

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_PERMISSION_RESULT);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.READ_PERMISSION_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    IScan local = new LocalScan(getApplicationContext(), DbEntryService
                            .getAccountInfo(CmpDeviceService
                                    .getPreferencesService().getLocalId()));
                    local.start();
                    Toast.makeText(getApplicationContext(), "Local scan started...", Toast
                            .LENGTH_LONG).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
