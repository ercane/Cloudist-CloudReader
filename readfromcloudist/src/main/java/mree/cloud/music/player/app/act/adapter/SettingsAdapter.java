package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.common.ref.SettingType;

/**
 * Created by mree on 31.08.2016.
 */
public class SettingsAdapter extends ArrayAdapter<SettingType> {


    private SettingType[] settingsTypes;

    public SettingsAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_settings_row, parent, false);
        }

        TextView setButton = (TextView) row.findViewById(R.id.setButton);
        setButton.setText(getItem(position).getDesc());

        return row;
    }

    public SettingType[] getSettingsTypes() {
        return settingsTypes;
    }
}
