package mree.cloud.music.player.app.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.AuthActivity;
import mree.cloud.music.player.app.act.adapter.AccountTypeAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.report.AnswersImpl;
import mree.cloud.music.player.app.scan.impl.LocalScan;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.auth.SourceState;

/**
 * Created by eercan on 01.09.2016.
 */
public class AddAccountView {
    private static final String TAG = AddAccountView.class.getSimpleName();
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SourceInfo acc;

    public AddAccountView(Context context) {
        this.context = context;
        AdMob.prepareInfiniteAds(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AnswersImpl.addAccount(acc.getId(), acc.getType());
                AnswersImpl.nameAccount(acc.getName(), acc.getType());
                openAuthActivity();
                AdMob.requestNewInfinite();
            }
        });
        AdMob.requestNewInfinite();
        init();
    }

    private void openAuthActivity() {
        Intent auth = new Intent(context, AuthActivity.class);
        auth.putExtra(Constants.ACCOUNT_INFO, acc);
        auth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(auth);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_account_add, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        final EditText accNameView = (EditText) view.findViewById(R.id.accNameView);
        final TextView msgView = (TextView) view.findViewById(R.id.msgView);
        final Spinner accTypeSpin = (Spinner) view.findViewById(R.id.accountType);
        Button accAddBtn = (Button) view.findViewById(R.id.addAccountButton);
        List<SourceType> sourceTypes = new ArrayList<>();
        if ("0".equals(CmpDeviceService.getPreferencesService().getLocalId())) {
            sourceTypes.addAll(Arrays.asList(SourceType.values()));
        } else {
            for (SourceType st : SourceType.values()) {
                if (st != SourceType.LOCAL) {
                    sourceTypes.add(st);
                }
            }
        }
        final AccountTypeAdapter adapter = new AccountTypeAdapter(context, R.layout
                .layout_account_type_spinner, sourceTypes);
        accTypeSpin.setAdapter(adapter);

        accAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    msgView.setVisibility(View.GONE);
                    SourceType selectedItem = (SourceType) accTypeSpin.getSelectedItem();
                    if (SourceType.LOCAL == selectedItem) {
                        acc = new SourceInfo();
                        acc.setName("Local");
                        acc.setType(selectedItem);
                        acc.setState(SourceState.NOT_AUTH);
                        acc.setScannedSong(0l);
                        acc.setScanStatus(ScanStatus.INITIAL);
                        if (acc.getType() == SourceType.LOCAL) {
                            acc.setState(SourceState.AUTH);
                            acc.setName(Constants.LOCAL_NAME);
                        }
                        Long id = DbEntryService.saveAccount(acc);
                        acc.setId(id.toString());
                        CmpDeviceService.getPreferencesService().setLocalId(acc.getId());
                        LocalScan scan = new LocalScan(context, acc);
                        scan.start();
                    } else if (TextUtils.isEmpty(accNameView.getText())) {
                        msgView.setVisibility(View.VISIBLE);
                        msgView.setText(R.string.add_account_name_error);
                    } else if (accTypeSpin.getSelectedItem() == null) {
                        msgView.setVisibility(View.VISIBLE);
                        msgView.setText(R.string.add_account_spinner_error);
                    } else if (accNameView.getText().toString().length() > Constants
                            .MAX_NAME_LENGTH) {
                        msgView.setVisibility(View.VISIBLE);
                        msgView.setText(R.string.max_length_error + Constants.MAX_NAME_LENGTH);
                    } else {
                        acc = new SourceInfo();
                        acc.setName(accNameView.getText().toString());
                        acc.setType(selectedItem);
                        acc.setState(SourceState.NOT_AUTH);
                        acc.setScannedSong(0l);
                        acc.setScanStatus(ScanStatus.INITIAL);
                        if (acc.getType() == SourceType.LOCAL) {
                            acc.setState(SourceState.AUTH);
                            acc.setName(Constants.LOCAL_NAME);
                        }
                        Long id = DbEntryService.saveAccount(acc);
                        if (id != null && id > 0) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            acc.setId(id.toString());

                            if (AdMob.isInfiniteLoaded()) {
                                AdMob.showInfinite();
                            } else {
                                AnswersImpl.addAccount(acc.getId(), acc.getType());
                                AnswersImpl.nameAccount(acc.getName(), acc.getType());
                                openAuthActivity();
                            }
                        } else {
                            msgView.setVisibility(View.VISIBLE);
                            msgView.setText("There is already an account has same name for " +
                                    (selectedItem).getDesc() + "!");
                        }

                    }
                } catch (Exception e) {
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText("ERROR: " + e.getMessage());
                }
            }
        });
        accTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SourceType item = adapter.getItem(position);
                msgView.setVisibility(View.GONE);
                switch (item) {
                    case LOCAL:
                        accNameView.setVisibility(View.GONE);
                        break;
                    case ONEDRIVE:
                        accNameView.setVisibility(View.VISIBLE);
                        break;
                    case DROPBOX:
                        accNameView.setVisibility(View.VISIBLE);
                        break;
                    case GOOGLE_DRIVE:
                        accNameView.setVisibility(View.VISIBLE);
                        break;
                    case SPOTIFY:
                        accNameView.setVisibility(View.VISIBLE);
                        msgView.setVisibility(View.VISIBLE);
                        msgView.setText(context.getText(R.string.add_spotify_msg));
                        msgView.setTextColor(Color.RED);
                        break;
                    case YANDEX_DISK:
                        accNameView.setVisibility(View.VISIBLE);
                        break;
                    case BOX:
                        accNameView.setVisibility(View.VISIBLE);
                        msgView.setVisibility(View.VISIBLE);
                        msgView.setText(context.getText(R.string.add_box_msg));
                        msgView.setTextColor(Color.RED);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));

    }


    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
}
