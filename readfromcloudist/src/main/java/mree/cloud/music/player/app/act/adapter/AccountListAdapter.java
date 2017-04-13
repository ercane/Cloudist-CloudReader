package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.common.model.SourceInfo;

/**
 * Created by eercan on 11.11.2016.
 */

public class AccountListAdapter extends ArrayAdapter<SourceInfo> {

    private int layoutResource;
    private LayoutInflater inflater;
    private ListView listView;
    private SparseBooleanArray mSelectedItemsIds;
    private Context context;
    private List<SourceInfo> accounts;
    private List<SourceInfo> filteredAccounts;

    public AccountListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.accounts = new ArrayList<>();
    }

    @Override
    public void add(SourceInfo object) {
        super.add(object);
        accounts.add(object);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_album_grid, parent, false);
        }


        SourceInfo account = getItem(position);
        ImageView ivThumb = (ImageView) row.findViewById(R.id.ivAlbum);
        TextView title = (TextView) row.findViewById(R.id.tvAudioTitle);

        if (account.getName() != null) {
            try {
                title.setText(account.getName() + " (" + DbEntryService.getAccountSongsCount(account
                        .getId()) + ")");
            } catch (Exception e) {
                title.setText(account.getName());
            }
        }


        if (account.getType() != null) {
            switch (account.getType()) {
                case ONEDRIVE:
                    ivThumb.setImageResource(R.drawable.ic_onedrive);
                    break;
                case DROPBOX:
                    ivThumb.setImageResource(R.drawable.ic_dropbox);
                    break;
                case GOOGLE_DRIVE:
                    ivThumb.setImageResource(R.drawable.ic_drive);
                    break;
                case YANDEX_DISK:
                    ivThumb.setImageResource(R.drawable.ic_yandex);
                    break;
                case SPOTIFY:
                    ivThumb.setImageResource(R.drawable.ic_spotify);
                    break;
                case BOX:
                    ivThumb.setImageResource(R.drawable.ic_box);
                    break;
                default:
                    ivThumb.setImageResource(R.drawable.default_cover);
                    break;
            }
        } else {
            ivThumb.setImageResource(R.drawable.default_cover);
            //ivThumb.setPadding(0, 5, 0, 5);
            //ivThumb.setBackgroundResource(R.drawable.blue_border);
        }


        return row;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                AccountListAdapter.this.clear();
                filteredAccounts = (ArrayList<SourceInfo>) results.values;
                AccountListAdapter.this.addAll(filteredAccounts);
                AccountListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SourceInfo> FilteredArrayNames = new ArrayList<SourceInfo>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < accounts.size(); i++) {
                    String dataNames = accounts.get(i).getName();
                    if (!TextUtils.isEmpty(dataNames) && dataNames.toLowerCase().contains
                            (constraint.toString())) {
                        FilteredArrayNames.add(accounts.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

    public List<SourceInfo> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SourceInfo> accounts) {
        this.accounts = accounts;
    }
}