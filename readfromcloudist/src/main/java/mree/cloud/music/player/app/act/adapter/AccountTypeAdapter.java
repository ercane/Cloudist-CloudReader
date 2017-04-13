package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by eercan on 01.09.2016.
 */
public class AccountTypeAdapter extends ArrayAdapter<SourceType> {

    private Context context;

    public AccountTypeAdapter(Context context, int resource, List<SourceType> objects) {
        super(context, resource);
        this.context = context;
        addAll(objects);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_account_type_spinner, parent, false);
        }

        SourceType item = getItem(position);

        TextView sourceIcon = (TextView) row.findViewById(R.id.sourceIcon);
        TextView sourceDesc = (TextView) row.findViewById(R.id.sourceDesc);

        //sourceIcon.setTypeface(Typeface.createFromAsset(context.getAssets(), "icomoon.ttf"));
        IconHelper.setSourceIcon(sourceIcon, item);
        //sourceIcon.setText(String.valueOf((char) AuthHelper.getIcon(item)));
        //sourceIcon.setTextColor(Color.BLUE);

        sourceDesc.setText(item.getDesc());

        return row;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
