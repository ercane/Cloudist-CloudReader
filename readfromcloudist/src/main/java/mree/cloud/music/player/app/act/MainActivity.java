package mree.cloud.music.player.app.act;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.fragment.AccountFragment;
import mree.cloud.music.player.app.act.fragment.AlbumFragment;
import mree.cloud.music.player.app.act.fragment.ArtistFragment;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.act.fragment.PlaylistFragment;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.receivers.AudioFocusReceiver;
import mree.cloud.music.player.app.receivers.HeadsetPlugReceiver;
import mree.cloud.music.player.app.scan.IScan;
import mree.cloud.music.player.app.scan.impl.LocalScan;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.FragmentType;
import mree.cloud.music.player.common.ref.ScanStatus;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaybackState;
import mree.cloud.music.player.common.ref.auth.SourceState;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TITLE = "TITLE";
    public static final String ADD = "ADD";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static ArrayList<String> loads = new ArrayList<String>();
    public static ArrayList<Long> loadsByte = new ArrayList<Long>();
    public static Handler titleHandler;
    private static View toolbar;
    private static FragmentManager fragmentManager;
    private static AudioFocusReceiver audioFocusReceiver;
    private static HeadsetPlugReceiver receiver;
    private Fragment fragment;

    public static View getToolbar() {
        return toolbar;
    }

    public static FragmentManager getFM() {
        return fragmentManager;
    }

    @Override
    public void setTitle(CharSequence title) {
        String str = "<font color=\"" + "#FFFFFF" + "\">" + title.toString() +
                "</font>";

        super.setTitle(Html.fromHtml(str));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setSupportActionBar(actionBar);

        /*Appszoom.fetchAd(null, new OnAdFetchedListener() {
            @Override
            public void onAdFetched() {
                Appszoom.showAd(MainActivity.this);
            }
        });*/
        //mInterstitialAd.show();
       /* Database db = new Database(this);
        DbTableService.sqoh = db;
        DbEntryService.sqoh = db;

        if (!CmpDeviceService.running) {
            startService(new Intent(this, CmpDeviceService.class));
        }
*/
        audioFocusReceiver = new AudioFocusReceiver(getApplicationContext());
        audioFocusReceiver.requestFocus();

        try {
            if (CmpDeviceService.getPreferencesService().isLocalInclude()) {
                checkReadPermission();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }

        AdMob.requestNewPeriodic();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, actionBar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        prepareBannerAd();
        toolbar = findViewById(R.id.musicController);
        //toolbar.setVisibility(View.GONE);

        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        Map<String, String> values = new HashMap<>();
        values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.AUDIO.getCode().toString());
        fragment = AudioFragment.newInstance(values);
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle(R.string.title_activity_main);
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

        receiver = new HeadsetPlugReceiver();
        registerReceiver(receiver, receiverFilter);

        titleHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                setTitle(data.getString(TITLE, ""));
            }
        };


        localCheck();
    }

    private void localCheck() {
        try {
            if ("0".equals(CmpDeviceService
                    .getPreferencesService().getLocalId()) &&
                    CmpDeviceService.getPreferencesService().isLocalInclude()) {
                SourceInfo accountInfo = new SourceInfo();
                accountInfo.setUserId("0");
                accountInfo.setName(SourceType.LOCAL.getDesc());
                accountInfo.setScanStatus(ScanStatus.INITIAL);
                accountInfo.setState(SourceState.AUTH);
                accountInfo.setType(SourceType.LOCAL);
                Long account = DbEntryService.saveAccount(accountInfo);
                if (account != null && account > 0) {
                    CmpDeviceService.getPreferencesService().setLocalId(account.toString());
                }
                accountInfo.setId(account.toString());
                LocalScan scan = new LocalScan(getApplicationContext(), accountInfo);
                scan.start();
            }
        } catch (Exception e) {

        }
    }

    private void checkReadPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
         /*   if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.READ_PERMISSION_RESULT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_PERMISSION_RESULT);
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

    private void prepareBannerAd() {
        try {
            View mainView = getLayoutInflater().inflate(R.layout.nav_header_main, null, false);
            //AdView mAdView = (AdView) mainView.findViewById(R.id.adView);
            //AdMob.prepareBannerAd(getApplicationContext(), mAdView, getString(R.string
            //                 .banner_ad_id));


           /*NativeExpressAdView adView = (NativeExpressAdView) mainView.findViewById(R.id.adView);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adView.loadAd(request);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            //super.onBackPressed();
            try {
                moveTaskToBack(false);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    if (fragment.getTag() != null && !"".equals(fragment.getTag())) {
                        setTitle(fragment.getTag());
                    } else {
                        setTitle(getResources().getString(R.string.app_name));
                    }
                    break;
                }
            }
        }
        MusicService musicSrv = AudioFragment.getMusicService();
        if (musicSrv != null) {
            if (MusicService.playbackState != PlaybackState.INITIAL) {
                AudioFragment.getToolbar().show();
            } else {
                AudioFragment.getToolbar().hide();
            }
        }

        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String backTag = "";

        Map<String, String> values = new HashMap<>();
        if (id == R.id.audios) {
            values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.AUDIO.getCode().toString());
            fragment = AudioFragment.newInstance(values);
            backTag = Constants.AUDIO;
        } else if (id == R.id.albums) {
            fragment = AlbumFragment.newInstance("", "");
            backTag = Constants.ALBUM;
        } else if (id == R.id.artists) {
            fragment = ArtistFragment.newInstance("", "");
            backTag = Constants.ARTIST;
        } else if (id == R.id.accounts) {
            fragment = AccountFragment.newInstance("", "");
            backTag = Constants.ACCOUNT;
        } else if (id == R.id.playlists) {
            fragment = PlaylistFragment.newInstance("", "");
            backTag = Constants.PLAYLIST;
        } else if (id == R.id.settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.help) {
            startHelp();
        }

        if (fragment != null) {

            if (getFM().getBackStackEntryCount() > 0) {
                for (int i = 0; i < getFM().getBackStackEntryCount(); ++i) {
                    getFM().popBackStack();
                }
            }

            AdMob.preparePeriodicAds(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    AdMob.requestNewPeriodic();
                }
            });

            getFM().beginTransaction().replace(R.id.flContent, fragment).commit();
            item.setChecked(true);
            setTitle(item.getTitle());

            if (AdMob.isPeriodicLoaded()) {
                AdMob.showPeriodic();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startHelp() {
        /*HelpPagerView view = new HelpPagerView(MainActivity.this);
        AlertDialog dialog = view.getBuilder().create();
        view.setDialog(dialog);
        dialog.show();*/
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mree.cloud.music.player.app.act/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mree.cloud.music.player.app.act/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            audioFocusReceiver.abandonFocus();
            if (AudioFragment.getMusicConnection() != null) {
                unbindService(AudioFragment.getMusicConnection());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
        try {
            unregisterReceiver(receiver);
            //unregisterReceiver(hardButtonReceiver);
            stopService(new Intent(this, CmpDeviceService.class));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
}
