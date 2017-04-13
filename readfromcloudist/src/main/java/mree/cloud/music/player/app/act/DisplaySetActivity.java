package mree.cloud.music.player.app.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.services.CmpDeviceService;

public class DisplaySetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_set);

        Switch swShow = (Switch) findViewById(R.id.swShowSuitable);
        swShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CmpDeviceService.getPreferencesService().setIsOnlyShowSuitable(true);
                } else {
                    CmpDeviceService.getPreferencesService().setIsOnlyShowSuitable(true);
                }
            }
        });
    }
}
