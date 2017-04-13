package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.auth.SourceState;


/**
 * Created by mree on 31.08.2016.
 */
public class AccountSetAdapter extends ArrayAdapter<SourceInfo>{

    private Context context;

    private List<SourceInfo> accounts;
    private SparseBooleanArray mSelectedItemsIds;


    public AccountSetAdapter(Context context, int resource, List<SourceInfo> accounts){
        super(context, resource);
        this.context = context;
        this.accounts = accounts;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    public AccountSetAdapter(Context context, int resource){
        super(context, resource);
        this.context = context;
        accounts = new ArrayList<>();
        mSelectedItemsIds = new SparseBooleanArray();
        AdMob.requestNewInfinite();
    }

    @Override
    public void add(@Nullable SourceInfo object){
        accounts.add(object);
        super.add(object);
    }

    @Override
    public void addAll(SourceInfo... items){
        for (SourceInfo si : items) {
            accounts.add(si);
        }
        super.addAll(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_account_set_row, parent, false);
        }

        final SourceInfo item = getItem(position);


        TextView sourceIcon = (TextView) row.findViewById(R.id.sourceIcon);
        TextView sourceName = (TextView) row.findViewById(R.id.sourceName);
        TextView sourceState = (TextView) row.findViewById(R.id.sourceState);
        TextView sourceCount = (TextView) row.findViewById(R.id.sourceCount);
        TextView scanStatus = (TextView) row.findViewById(R.id.scanStatus);
        ImageView ivSpinner = (ImageView) row.findViewById(R.id.ivSpinner);

        IconHelper.setSourceIcon(sourceIcon, item.getType());

        sourceName.setText(item.getName());

        sourceState.setText(item.getState().getDesc());
        sourceState.setTextColor(getColor(item.getState()));

        sourceCount.setText("Scanned songs: " + DbEntryService.getAccountSongsCount(item.getId()));
        sourceCount.setTextColor(Color.BLACK);


        if (ivSpinner != null) {
            ivSpinner.setBackgroundResource(R.drawable.rotate_anim);

            AnimationDrawable rocketAnimation = (AnimationDrawable) ivSpinner.getBackground();
            rocketAnimation.start();
        }

        switch (item.getState()) {
            case AUTH:
                scanStatus.setVisibility(View.VISIBLE);
                break;
            case NOT_AUTH:
                scanStatus.setVisibility(View.INVISIBLE);
                break;
            case EXPIRED:
                scanStatus.setVisibility(View.INVISIBLE);
                break;
        }

        switch (item.getScanStatus()) {
            case INITIAL:
                ivSpinner.setVisibility(View.GONE);
                scanStatus.setTextColor(Color.GREEN);
                scanStatus.setText(context.getString(R.string.initial_scan_message));
                break;
            case STARTED:
                ivSpinner.setVisibility(View.VISIBLE);
                scanStatus.setTextColor(Color.MAGENTA);
                scanStatus.setText("Scan " + item.getScanStatus().getDesc());
                break;
            case STOPPED:
                ivSpinner.setVisibility(View.GONE);
                scanStatus.setTextColor(Color.DKGRAY);
                scanStatus.setText("Scan " + item.getScanStatus().getDesc());
                break;
            case FINISHED:
                ivSpinner.setVisibility(View.GONE);
                scanStatus.setTextColor(Color.GREEN);
                scanStatus.setText("Scan " + item.getScanStatus().getDesc());
                break;
            case FAILED:
                ivSpinner.setVisibility(View.GONE);
                scanStatus.setTextColor(Color.RED);
                scanStatus.setText("Scan " + item.getScanStatus().getDesc());
                ivSpinner.setVisibility(View.GONE);
                break;
        }

        if (mSelectedItemsIds.get(position)) {
            row.setBackgroundResource(R.drawable.adapter_selected_bckgrnd);
        } else {
            row.setBackgroundColor(Color.TRANSPARENT);
        }
        return row;

    }

    private int getColor(SourceState state){
        switch (state) {
            case AUTH:
                return Color.GREEN;
            case NOT_AUTH:
                return Color.RED;
            case EXPIRED:
                return Color.YELLOW;
            default:
                return Color.RED;
        }
    }

    public void toggleSelection(int position){
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection(){
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value){
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return mSelectedItemsIds;
    }

    @Override
    public void addAll(Collection<? extends SourceInfo> collection){
        super.addAll(collection);
        this.accounts = (List<SourceInfo>) collection;
    }

    public List<SourceInfo> getAccounts(){
        return accounts;
    }

    public void setItemStatus(String accId, ScanStatus status){
        for (SourceInfo si : accounts) {
            if (si.getId() == accId) {
                si.setScanStatus(status);
                break;
            }
        }
    }
}
