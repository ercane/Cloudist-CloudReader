package mree.cloud.music.player.app.act;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.utils.Constants;

public class DataSettingActivity extends AppCompatActivity {

    private static final String TAG = DataSettingActivity.class.getSimpleName();
    private LinearLayout clearCacheLayout;
    private Switch swMobileAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_setting);
        clearCacheLayout = (LinearLayout) findViewById(R.id.clearCacheLayout);
        clearCacheLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWritePermission();
               /* File root = getCacheDir();
                long totalByte = 0l;
                for (File f : root.listFiles()) {
                    totalByte += f.length();
                    boolean delete = f.delete();
                    if (delete) {
                        Log.i(TAG, f.getName() + " removed");
                    } else {
                        Log.e(TAG, f.getName() + " cannot be removed");
                    }

                }
                Log.e(TAG, "Total: " + totalByte / (1024 * 1024) + " mb removed from cache");

                root = getFilesDir();
                totalByte = 0l;
                for (File f : root.listFiles()) {
                    if (!"onedrive".equals(f.getName())) {
                        totalByte += f.length();
                        boolean delete = f.delete();
                        if (delete) {
                            Log.i(TAG, f.getName() + " removed");
                        } else {
                            Log.e(TAG, f.getName() + " cannot be removed");
                        }
                    } else {
                        Log.e(TAG, "Onedrive total size: " + (f.length() / (1024 * 1024)) + " kb");
                    }

                }*/

                File fileCache = new File(getCacheDir(), "file_cache");
                long totalByte = 0l;
                if (fileCache.exists()) {
                    for (File f : fileCache.listFiles()) {
                        totalByte += f.length();
                        boolean delete = f.delete();
                        if (delete) {
                            Log.i(TAG, f.getName() + " removed");
                        } else {
                            Log.e(TAG, f.getName() + " cannot be removed");
                        }
                    }
                }
                Log.e(TAG, "Total: " + totalByte / (1024 * 1024) + " mb removed from file cache");
                Toast.makeText(getApplicationContext(), "Cache is cleaned", Toast.LENGTH_LONG)
                        .show();
            }
        });

        swMobileAllowed = (Switch) findViewById(R.id.swMobileAllowed);
        swMobileAllowed.setSelected(CmpDeviceService.getPreferencesService().isMobileDataAllowed());
        swMobileAllowed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CmpDeviceService.getPreferencesService().setIsMobileDataAllowed(isChecked);
            }
        });
    }

    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.READ_PERMISSION_RESULT);
        }
    }
}
