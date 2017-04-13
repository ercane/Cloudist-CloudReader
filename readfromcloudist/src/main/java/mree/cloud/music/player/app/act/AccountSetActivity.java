package mree.cloud.music.player.app.act;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.adapter.AccountSetAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.utils.ScrollTextView;
import mree.cloud.music.player.app.views.AccountOptionView;
import mree.cloud.music.player.app.views.AddAccountView;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaylistType;
import mree.cloud.music.player.common.ref.auth.SourceState;

public class AccountSetActivity extends AppCompatActivity {

    public static final String ACC_ID = "ACC_ID";
    private static final String TAG = AccountSetActivity.class.getSimpleName();
    private static Context context;
    private static Handler refreshHandler;
    private ListView accountListView;
    private List<SourceInfo> accounts;
    private AccountSetAdapter adapter;
    private boolean isAdd;
    private ScrollTextView tvSwipe;

    public static synchronized void removeAccount(SourceInfo sourceInfo, boolean removeAll) {
        if (sourceInfo.getType() != SourceType.LOCAL)
            removeThumbs(sourceInfo.getId());
        DbEntryService.removeFromPlaylistsByAccount(sourceInfo.getId());
        DbEntryService.removeAudioByAccount(sourceInfo.getId());
        if (removeAll) {
            DbEntryService.removeAccountByName(sourceInfo.getName());
            if (sourceInfo.getType() == SourceType.LOCAL)
                CmpDeviceService.getPreferencesService().setLocalId("0");
            if (sourceInfo.getType() == SourceType.SPOTIFY) {
                DbEntryService.removePlaylistByType(PlaylistType.SPOTIFY);
                DbEntryService.clearEmptyPlaylists();
            }
        } else {
            DbEntryService.clearEmptyPlaylists();
            DbEntryService.resetScanStatus(sourceInfo.getId());
        }
    }

    private static void removeThumbs(String id) {
        List<String> thumbs = DbEntryService.getThumbnailsByAcc(id);
        for (String f : thumbs) {
            if (f != null) {
                File filesDir = context.getFilesDir();
                File thum = new File(filesDir, f);
                if (thum.exists()) {
                    boolean delete = thum.delete();
                    Log.e(TAG, delete + "");
                }
            }
        }
    }

    public static Handler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        context = getApplicationContext();
        isAdd = getIntent().getBooleanExtra(MainActivity.ADD, false);
        if (isAdd) {
            AddAccountView view = new AddAccountView(AccountSetActivity.this);
            AlertDialog dialog = view.getBuilder().create();
            view.setDialog(dialog);
            dialog.show();
        }

        accounts = new ArrayList<>();

        accountListView = (ListView) findViewById(R.id.accountList);
        //accountListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        // accountListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* Toast.makeText(getApplicationContext(), "Swipe for actions", Toast.LENGTH_LONG)
                        .show();*/
                AccountOptionView addToView = new AccountOptionView(AccountSetActivity.this,
                        adapter,
                        adapter.getItem(position));
                AlertDialog.Builder builder = addToView.getBuilder();
                AlertDialog dialog = builder.create();
                addToView.setDialog(dialog);
                dialog.show();
            }
        });
        //getAccounts().execute();

        accountListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                final int checkedCount = accountListView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.account_set_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_select_all:
                        adapter.removeSelection();
                        accountListView.clearChoices();
                        for (int i = 0; i < adapter.getAccounts().size(); i++) {
                            accountListView.setItemChecked(i, true);
                        }
                        break;
                    case R.id.item_remove:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AccountSetActivity
                                .this);
                        builder.setTitle("Remove Accounts");
                        builder.setMessage("Are you sure to remove selected accounts ?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SparseBooleanArray selected = adapter.getSelectedIds();
                                        // Captures all selected ids with a loop
                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                SourceInfo acc = adapter.getItem(selected.keyAt(i));
                                                AccountSetActivity.removeAccount(acc, true);
                                                adapter.remove(acc);
                                            }
                                        }
                                        mode.finish();
                                    }
                                });

                        builder.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        mode.finish();
                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });

        refreshHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (adapter != null) {
                    String accId = msg.getData().getString(ACC_ID);
                    if (!TextUtils.isEmpty(accId)) {
                        adapter.setItemStatus(accId, ScanStatus.FINISHED);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };

        AdMob.prepareBannerAd(getApplicationContext(), (AdView) findViewById(R.id.adView),
                getString(R.string.banner_ad_id));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccounts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_set_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_account:
                AddAccountView view = new AddAccountView(AccountSetActivity.this);
                AlertDialog dialog = view.getBuilder().create();
                view.setDialog(dialog);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getAccounts();
                    }
                });
               /* Intent settings = new Intent(this, AuthActivity.class);
                settings.putExtra(Constants.ACCOUNT_INFO, "Account");
                settings.putExtra(Constants.SOURCE_TYPE, SourceType.ONEDRIVE.getCode());
                startActivity(settings);*/
                return true;
        }
        return true;
    }

    private AsyncTask<Void, Void, Void> getAccounts() {
        return new AsyncTask<Void, Void, Void>() {
            private ArrayList<HashMap<String, String>> allAccounts;


            @Override
            protected void onPreExecute() {
                if (adapter != null)
                    adapter.clear();
                accounts = new ArrayList<>();
                adapter = new AccountSetAdapter(AccountSetActivity.this, R.layout
                        .layout_account_set_row);
                accountListView.setAdapter(adapter);
                allAccounts = DbEntryService.getAllAccounts();
            }

            @Override
            protected Void doInBackground(Void... params) {
                for (HashMap<String, String> item : allAccounts) {
                    try {
                        SourceInfo info = new SourceInfo();
                        info.setId(item.get(DbConstants.ACCOUNT_ID));
                        info.setName(item.get(DbConstants.ACCOUNT_NAME));
                        info.setUserId(item.get(DbConstants.ACCOUNT_USER_ID));
                        info.setAccessToken(item.get(DbConstants.ACCOUNT_ACCESS_TOKEN));
                        info.setRefreshToken(item.get(DbConstants.ACCOUNT_REFRESH_TOKEN));

                        String expire = item.get(DbConstants.ACCOUNT_EXPIRED_IN);
                        if (expire != null)
                            info.setExpiredIn(Long.parseLong(expire));

                        String scan = item.get(DbConstants.ACCOUNT_SCANNED_SONG);
                        if (scan != null)
                            info.setScannedSong(Long.parseLong(scan));

                        String state = item.get(DbConstants.ACCOUNT_STATE);
                        if (state != null)
                            info.setState(SourceState.get(Integer.parseInt(state)));

                        String type = item.get(DbConstants.ACCOUNT_TYPE);
                        if (type != null)
                            info.setType(SourceType.get(Integer.parseInt(type)));

                        String scannedStatus = item.get(DbConstants.ACCOUNT_SCAN_STATUS);
                        if (scannedStatus != null)
                            info.setScanStatus(ScanStatus.get(Integer.parseInt(scannedStatus)));

                        accounts.add(info);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.addAll(accounts);
            }
        };
    }


}
