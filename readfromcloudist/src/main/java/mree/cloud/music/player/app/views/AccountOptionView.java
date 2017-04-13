package mree.cloud.music.player.app.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.act.AuthActivity;
import mree.cloud.music.player.app.act.adapter.AccountSetAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;

import static mree.cloud.music.player.app.services.CmpDeviceService.getScan;

/**
 * Created by eercan on 16.03.2017.
 */

public class AccountOptionView extends AbstractView {

    private AccountSetAdapter adapter;
    private SourceInfo sourceInfo;

    public AccountOptionView(Context context, AccountSetAdapter adapter, SourceInfo sourceInfo) {
        super(context);
        this.adapter = adapter;
        this.sourceInfo = sourceInfo;
        AdMob.prepareInfiniteAds(new AdListener() {
            @Override
            public void onAdClosed() {
                openAuthActivity();
                dialog.dismiss();
            }
        });
        init();
    }

    @Override
    void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_account_option, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        LinearLayout scanLayout = (LinearLayout) view.findViewById(R.id.scanLayout);

        scanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        LinearLayout authLayout = (LinearLayout) view.findViewById(R.id.authLayout);

        authLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AdMob.isInfiniteLoaded()) {
                    openAuthActivity();
                    dialog.dismiss();
                } else {
                    AdMob.showInfinite();
                }
            }
        });

        LinearLayout editLayout = (LinearLayout) view.findViewById(R.id.editLayout);

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout removeLayout = (LinearLayout) view.findViewById(R.id.removeLayout);

        removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder
                        (context);
                builder.setTitle("Remove Account");
                builder.setMessage("Are you sure to remove '" + sourceInfo.getName() + "' ?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AccountSetActivity.removeAccount(sourceInfo, true);
                                adapter.remove(sourceInfo);
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.app.AlertDialog alert11 = builder.create();
                alert11.show();
                dialog.dismiss();
            }
        });

        ImageView ivAuth = (ImageView) view.findViewById(R.id.ivAuth);
        switch (sourceInfo.getState()) {
            case AUTH:
                authLayout.setVisibility(View.GONE);
                scanLayout.setVisibility(View.VISIBLE);
                break;
            case NOT_AUTH:
            case EXPIRED:
                authLayout.setVisibility(View.VISIBLE);
                scanLayout.setVisibility(View.GONE);
                IconHelper.setSourceIcon(ivAuth, sourceInfo.getType());
                break;
        }

        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));
    }

    private void startScan() {
        final IScan scan = getScan(sourceInfo);
        final ScanStatus accountScanStatus = DbEntryService.getAccountScanStatus(sourceInfo.getId
                ());
        if (accountScanStatus == ScanStatus.INITIAL) {
            scan.start();
            sourceInfo.setScanStatus(ScanStatus.STARTED);
        } else if (accountScanStatus == ScanStatus.STARTED) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Scan");
            builder.setMessage("Scan is already started for '" + sourceInfo.getName() + "'" +
                    ".\n" +
                    "Do you want to stop?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                scan.stop();
                                sourceInfo.setScanStatus(ScanStatus.STOPPED);
                                DbEntryService.updateAccountScanStatus(sourceInfo.getId(),
                                        ScanStatus.STOPPED);
                            } catch (Exception e) {
                                sourceInfo.setScanStatus(ScanStatus.FAILED);
                                DbEntryService.updateAccountScanStatus(sourceInfo.getId(),
                                        ScanStatus.FAILED);
                            }
                            adapter.notifyDataSetChanged();
                            //scan.start();
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            android.app.AlertDialog alert11 = builder.create();
            alert11.show();
        } else if (accountScanStatus == ScanStatus.STOPPED) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Scan");
            builder.setMessage("Scan is already stopped for '" + sourceInfo.getName() + "'" +
                    ".\n" +
                    "Do you want to resume?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                scan.resume();
                                sourceInfo.setScanStatus(ScanStatus.STARTED);
                                DbEntryService.updateAccountScanStatus(sourceInfo.getId(),
                                        ScanStatus.STARTED);
                            } catch (Exception e) {
                                sourceInfo.setScanStatus(ScanStatus.FAILED);
                                DbEntryService.updateAccountScanStatus(sourceInfo.getId(),
                                        ScanStatus.FAILED);
                            }
                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            android.app.AlertDialog alert11 = builder.create();
            alert11.show();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Scan");

            builder.setMessage("'" + sourceInfo.getName() + "' is already scanned." +
                    "Do you want to scan restart or continue");

            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Restart",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AccountSetActivity.removeAccount(sourceInfo, false);
                            scan.start();
                            Toast.makeText(context
                                    , R.string.scan_started, Toast
                                            .LENGTH_LONG).show();
                            sourceInfo.setScanStatus(ScanStatus.STARTED);
                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "Continue",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            scan.start();
                            sourceInfo.setScanStatus(ScanStatus.STARTED);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, R.string.scan_started, Toast
                                    .LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    });

            android.app.AlertDialog alert11 = builder.create();
            alert11.show();
        }

    }

    private void openAuthActivity() {
        Intent auth = new Intent(context, AuthActivity.class);
        auth.putExtra(Constants.ACCOUNT_INFO, sourceInfo);
        auth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(auth);
    }
}
