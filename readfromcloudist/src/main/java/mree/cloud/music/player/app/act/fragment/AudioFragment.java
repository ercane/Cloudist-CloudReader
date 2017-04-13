package mree.cloud.music.player.app.act.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Switch;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.AccountSetActivity;
import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.act.adapter.AudioListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.player.PlaybackToolbar;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.tasks.DownloadAudioTask;
import mree.cloud.music.player.app.tasks.FillAudioInfo;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.views.AlbumOptionView;
import mree.cloud.music.player.app.views.PlaylistOptionView;
import mree.cloud.music.player.common.model.AudioList;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.FragmentType;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;
import mree.cloud.music.player.common.ref.audio.PlaybackState;
import mree.cloud.music.player.common.ref.audio.PlaylistType;

import static mree.cloud.music.player.app.act.MainActivity.ADD;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment implements MediaController.MediaPlayerControl {
    public static final String AUDIO_INFO = "AUDIO_INFO";
    private static final String TAG = AudioFragment.class.getSimpleName();
    public static String AUDIO_LIST = "audio_list";
    public static String AUDIO_POSN = "audio_posn";
    private static Context context;
    private static View mainView;
    private static AudioListAdapter adapter;
    private static ArrayList<SongInfo> audioList;
    private static Handler addHandler, finishHandler, networkHandler;
    private static ThreadPoolExecutor threadPoolExecutor;
    private static ThreadPoolExecutor thumbThreadPoolExecutor;
    private static MusicService musicSrv;
    private static Handler refreshListHandler, changeMusicHandler;
    private static PlaybackToolbar toolbar;
    private static Intent playIntent;
    private static ServiceConnection musicConnection;
    private static boolean initialState = true;
    private static FragmentType type;
    private static String source;
    private static String playlist;
    private static String album;
    private static String artist;
    private static ListView listView;
    private static boolean musicBound = false;
    private OnFragmentInteractionListener mListener;
    private EditText filterView;
    private InputMethodManager imm;
    private Switch swOffline;

    public AudioFragment() {
        // Required empty public constructor
    }

    public static PlaybackToolbar getToolbar() {
        if (toolbar == null) {
            if (MainActivity.getToolbar() != null) {
                toolbar = new PlaybackToolbar(context, MainActivity.getToolbar());
            }
        }
        return toolbar;
    }

    public static MusicService getMusicService() {
        return musicSrv;
    }

    public static Handler getRefreshListHandler() {
        return refreshListHandler;
    }

    public static Handler getChangeMusicHandler() {
        return changeMusicHandler;
    }

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPoolExecutor == null) {
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            int corePoolSize = 10;
            int maximumPoolSize = 10;
            LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
            threadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,       // Initial pool size
                    maximumPoolSize,       // Max pool size
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    workQueue);
        }
        return threadPoolExecutor;
    }

    public static AudioListAdapter getAdapter() {
        return adapter;
    }

    public static ThreadPoolExecutor getThumbThreadPoolExecutor() {
        if (thumbThreadPoolExecutor == null) {
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            int corePoolSize = 10;
            int maximumPoolSize = 10;
            LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
            thumbThreadPoolExecutor = new ThreadPoolExecutor(
                    corePoolSize,       // Initial pool size
                    maximumPoolSize,       // Max pool size
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    workQueue);
        }
        return thumbThreadPoolExecutor;
    }

    public static AudioFragment newInstance(Map<String, String> values) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_TYPE_PARAM, values.get(Constants.FRAGMENT_TYPE_PARAM));
        args.putString(Constants.SOURCE_PARAM, values.get(Constants.SOURCE_PARAM));
        args.putString(Constants.ALBUM_PARAM, values.get(Constants.ALBUM_PARAM));
        args.putString(Constants.ARTIST_PARAM, values.get(Constants.ARTIST_PARAM));
        args.putString(Constants.PLAYLIST_PARAM, values.get(Constants.PLAYLIST_PARAM));
        fragment.setArguments(args);

        return fragment;
    }

    public static Handler getNetworkHandler() {
        return networkHandler;
    }

    public static ServiceConnection getMusicConnection() {
        return musicConnection;
    }

    public static SongInfo getSongInfo(HashMap<String, String> audio) {
        SongInfo si = new SongInfo();
        si.setId(audio.get(DbConstants.AUDIO_ID));
        si.setAccountId(audio.get(DbConstants.AUDIO_ACCOUNT_ID));
        String code = audio.get(DbConstants.AUDIO_SOURCE_TYPE);
        if (!TextUtils.isEmpty(code)) {
            si.setSourceType(SourceType.get(Integer.parseInt(code)));
        }

        code = audio.get(DbConstants.AUDIO_STATUS);
        if (!TextUtils.isEmpty(code)) {
            si.setStatus(
                    AudioStatus.get(Integer.parseInt(code)));
        }
        si.setPath(audio.get(DbConstants.AUDIO_PATH));
        code = audio.get(DbConstants.AUDIO_STATUS);
        if (!TextUtils.isEmpty(code)) {
            si.setStatus(AudioStatus.get(Integer.parseInt(code)));
        }
        si.setTitle(audio.get(DbConstants.AUDIO_TITLE));
        return si;

    }

    public static void refresh() {
        if (adapter != null) {
            adapter.clear();
        }
        fillAudioList();
    }

    private static void fillAudioList() {
        ArrayList<HashMap<String, String>> allAudios = new ArrayList<>();
        switch (type) {
            case AUDIO:
                allAudios = DbEntryService.getAllAudios();
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, null);
                break;
            case ALBUM:
                allAudios = DbEntryService.getAudiosOfAlbum(album, artist);
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, album);
                break;
            case ARTIST:
                allAudios = DbEntryService.getAudiosOfArtist(artist);
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, artist);
                break;
            case ACCOUNT:
                allAudios = DbEntryService.getAudiosOfAccount(source);
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, source);
                break;
            case PLAYLIST:
                allAudios = DbEntryService.getAudiosOfPlaylist(playlist);
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, playlist);
                break;
            case NONE:
                adapter = new AudioListAdapter(context, R.layout.layout_audio_row, type, null);
                break;
        }


        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(getMultiChoiceListener());
        //listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.isEnabled(position)) {
                    if (playIntent == null && !MusicService.running && initialState) {
                        AudioList listObj = new AudioList();
                        playIntent = new Intent(context, MusicService.class);
                        musicConnection = getMusicConnection(listObj.getList());
                        context.bindService(playIntent, musicConnection, Context
                                .BIND_ADJUST_WITH_ACTIVITY);
                        context.startService(playIntent);
                        initialState = false;
                    }

                    Bundle b = new Bundle();
                    List<SongInfo> songs = new ArrayList<SongInfo>();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        songs.add(adapter.getItem(i));
                    }
                    AudioList listObject = new AudioList();
                    listObject.setList(songs);
                    b.putSerializable(AUDIO_LIST, listObject);
                    b.putInt(AUDIO_POSN, position);
                    final Message changeMsg = new Message();
                    changeMsg.setData(b);

                    AdMob.preparePeriodicAds(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            if (changeMsg != null) {
                                getChangeMusicHandler().sendMessage(changeMsg);
                                MusicService.playbackState = PlaybackState.STARTED;
                            }
                            AdMob.requestNewPeriodic();
                        }
                    });

                    if (AdMob.isPeriodicLoaded() && listObject.getList().get(position)
                            .getSourceType() != SourceType.LOCAL) {
                        AdMob.showPeriodic();
                    } else {
                        getChangeMusicHandler().sendMessage(changeMsg);
                        MusicService.playbackState = PlaybackState.STARTED;
                    }
                } else {
                    listView.getChildAt(position).setEnabled(false);
                }

            }
        });
        //listView.setOnItemClickListener(downloadListener());

        getThreadPool().submit(new FillAudioInfo(addHandler, finishHandler, allAudios));
    }

    private static ServiceConnection getMusicConnection(final List<SongInfo> audioList) {
        if (musicConnection == null) {
            musicConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                    //get service
                    musicSrv = binder.getService();
                    //pass listView
                    musicSrv.setList(audioList);
                    musicBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    musicBound = false;
                }
            };
        } else {
            musicSrv.setList(audioList);
            musicBound = true;
        }

        return musicConnection;
    }

    public static void changeOfflineStatus(boolean isChecked, String playlistId,
                                           ArrayList<HashMap<String, String>> audiosOfPlaylist) {
        try {
            if (isChecked) {
                DbEntryService.updatePlaylistOfflineStatus(playlistId, 1);
                for (HashMap<String, String> audio : audiosOfPlaylist) {
                    SongInfo songInfo = getSongInfo(audio);
                    if (songInfo.getSourceType() != SourceType.LOCAL
                            && songInfo.getSourceType() != SourceType.SPOTIFY
                            ) {
                        DownloadAudioTask task = new DownloadAudioTask(context, songInfo);
                        Future<?> submit = CmpDeviceService.getDownloadExecutor().submit(task);
                    }
                }
            } else {
                DbEntryService.updatePlaylistOfflineStatus(playlistId, 0);
                for (final HashMap<String, String> audio : audiosOfPlaylist) {
                    FileFilter fileFilter = new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().startsWith(audio.get(DbConstants.AUDIO_ID));
                        }
                    };

                    File[] files = FileUtils.OFFLINE_ROOT.listFiles(fileFilter);
                    if (files != null) {
                        for (File f : files) {
                            f.delete();
                        }
                    }
                    DbEntryService.updateAudioOfflineStatus(audio.get(DbConstants.AUDIO_ID),
                            AudioStatus.ONLINE.getCode());
                    DbEntryService.removeAudioFromDownloadAudios(audio.get(DbConstants.AUDIO_ID));

                }
            }
            refresh();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private static AbsListView.MultiChoiceModeListener getMultiChoiceListener() {
        return new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.audio_set_context, menu);
                if (type == FragmentType.PLAYLIST) {
                    mode.getMenu().add(Menu.NONE, R.id.item_remove, Menu.NONE, "Remove");
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                SparseBooleanArray selected;
                switch (item.getItemId()) {
                    case R.id.item_select_all:
                        adapter.removeSelection();
                        listView.clearChoices();
                        for (int i = 0; i < adapter.getSongs().size(); i++) {
                            listView.setItemChecked(i, true);
                        }
                        break;
                    case R.id.item_playlist:
                        selected = adapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        List<SongInfo> selectedList = new ArrayList<>();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                selectedList.add(adapter.getItem(selected.keyAt(i)));
                            }
                        }
                        AlbumOptionView addToView = new AlbumOptionView(context, selectedList);
                        AlertDialog.Builder builder = addToView.getBuilder();
                        AlertDialog dialog = builder.create();
                        addToView.setDialog(dialog);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mode.finish();
                            }
                        });
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        };
    }

    private void setController() {
/*        if (controller == null) {
            controller = new MusicController(context);
        }
        //set previous and next button listener
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and prepareNotification
        controller.setMediaPlayer(this);
        controller.setEnabled(true);*/
    }

    private void playNext() {
        musicSrv.playNext(true);
    }

    private void playPrev() {
        musicSrv.playPrev();
    }

    public void songPicked(Integer position) {
        if (musicSrv != null) {
            getToolbar().show();
            musicSrv.setSong(position);
            musicSrv.playSong();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        audioList = new ArrayList<>();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            source = getArguments().getString(Constants.SOURCE_PARAM);
            playlist = getArguments().getString(Constants.PLAYLIST_PARAM);
            album = getArguments().getString(Constants.ALBUM_PARAM);
            artist = getArguments().getString(Constants.ARTIST_PARAM);
            String typeCode = getArguments().getString(Constants.FRAGMENT_TYPE_PARAM);
            type = FragmentType.get(Integer.parseInt(typeCode));
        }

        addHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                SongInfo si = (SongInfo) data.getSerializable(AUDIO_INFO);
                adapter.add(si);
            }
        };

        finishHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

/*
                adapter.sort(new Comparator<SongInfo>() {
                    @Override
                    public int compare(SongInfo a, SongInfo b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
*/

                Bundle b = new Bundle();
                List<SongInfo> songs = adapter.getSongs();
                AudioList listObject = new AudioList();
                listObject.setList(songs);
                b.putSerializable(AUDIO_LIST, listObject);
                Message m = new Message();
                m.setData(b);
                //getRefreshListHandler().sendMessage(m);
            }
        };

        refreshListHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                AudioList listObj = (AudioList) data.getSerializable(AUDIO_LIST);

/*
                if (playIntent == null && !musicSrv.running) {
                    playIntent = new Intent(context, MusicService.class);
                    musicConnection = getMusicConnection(listObj.getList());
                    context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                    context.startService(playIntent);
                }
*/

            }
        };

        changeMusicHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                AudioList listObj = (AudioList) data.getSerializable(AUDIO_LIST);
                Integer pos = data.getInt(AUDIO_POSN);

                if (musicSrv != null) {
                    musicSrv.setList(listObj.getList());
                }
                songPicked(pos);
            }
        };

        networkHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_audio, container, false);

        listView = (ListView) mainView.findViewById(R.id.audioList);
        boolean isFirst = CmpDeviceService.getPreferencesService().isFirst();
        final LinearLayout firstLayout = (LinearLayout) mainView.findViewById(R.id.firstLayout);
        if (isFirst) {
            firstLayout.setVisibility(View.VISIBLE);
            CmpDeviceService.getPreferencesService().setFirst(false);
            CmpDeviceService.getPreferencesService().setFirstOpen(System.currentTimeMillis());
        } else {
            firstLayout.setVisibility(View.GONE);
        }

        if (0 == CmpDeviceService.getPreferencesService().getFirstOpen()) {
            CmpDeviceService.getPreferencesService().setFirstOpen(System.currentTimeMillis());
        }
        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fabAddAcc);
        switch (type) {
            case AUDIO:
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                /*Snackbar.make(view, "Add account", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                        Intent accSet = new Intent(getContext(), AccountSetActivity.class);
                        accSet.putExtra(ADD, true);
                        startActivity(accSet);
                        if (firstLayout != null) {
                            firstLayout.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case ALBUM:
                fab.setImageResource(R.drawable.ic_dot_white);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumOptionView addToView = new AlbumOptionView(context, adapter.getSongs
                                ());
                        AlertDialog.Builder builder = addToView.getBuilder();
                        AlertDialog dialog = builder.create();
                        addToView.setDialog(dialog);
                        dialog.show();
                    }
                });
                break;
            case ARTIST:
                fab.setImageResource(R.drawable.ic_dot_white);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumOptionView addToView = new AlbumOptionView(context, adapter.getSongs
                                ());
                        AlertDialog.Builder builder = addToView.getBuilder();
                        AlertDialog dialog = builder.create();
                        addToView.setDialog(dialog);
                        dialog.show();
                    }
                });
                break;
            case ACCOUNT:
                fab.setImageResource(R.drawable.ic_dot_white);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumOptionView addToView = new AlbumOptionView(context, adapter.getSongs
                                ());
                        AlertDialog.Builder builder = addToView.getBuilder();
                        AlertDialog dialog = builder.create();
                        addToView.setDialog(dialog);
                        dialog.show();
                    }
                });
                break;
            case PLAYLIST:
                final PlaylistInfo playlistInfo = DbEntryService.getPlaylistInfo(playlist);

                fab.setImageResource(R.drawable.ic_dot_white);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlaylistOptionView optView = new PlaylistOptionView(context, null,
                                playlistInfo);
                        AlertDialog.Builder builder = optView.getBuilder();
                        AlertDialog dialog = builder.create();
                        optView.setDialog(dialog);
                        dialog.show();
                    }
                });

                if (playlistInfo != null) {
                    swOffline = (Switch) mainView.findViewById(R.id.swOffline);

                    if (playlistInfo.getOfflineStatus() == 1) {
                        swOffline.setChecked(true);
                    }

                    swOffline.setOnCheckedChangeListener(new CompoundButton
                            .OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            changeOfflineStatus(isChecked, playlist, DbEntryService
                                    .getAudiosOfPlaylist
                                            (playlist));
                        }
                    });

                    if (PlaylistType.SPOTIFY != playlistInfo.getType()) {
                        swOffline.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case NONE:
                break;
        }


        filterView = (EditText) mainView.findViewById(R.id.filterView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    adapter.getFilter().filter(s.toString());
                } else {
                    adapter.clear();
                    adapter.addAll(adapter.getSongs());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        filterView.addTextChangedListener(textWatcher);
        listView.requestFocus();
        //fillAudioList();
        //prepareInterstitialAds();
        //prepareBannerAd();
        AdMob.prepareBannerAd(context, (AdView) mainView.findViewById(R.id.adView), getString(R
                .string.banner_ad_id));
        AdMob.requestNewPeriodic();
        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (filterView != null) {
                    int visibility = filterView.getVisibility();
                    switch (visibility) {
                        case View.VISIBLE:
                            filterView.setText("");
                            filterView.setVisibility(View.GONE);
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                            break;
                        case View.GONE:
                            filterView.setVisibility(View.VISIBLE);
                            filterView.requestFocus();
                            imm.showSoftInput(filterView, 0);
                            break;
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
/*        if (musicSrv != null && musicSrv.getPlayer().getCurrentPosition() != 0) {
            if (musicSrv.isPng()) {
                getToolbar().show();
            } else {
                getToolbar().hide();
            }
        }*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng()) {
            return musicSrv.getPosn();
        } else {
            return 0;
        }
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng()) {
            return musicSrv.getDur();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound) {
            return musicSrv.isPng();
        }
        return false;
    }

    @Override
    public void pause() {
        initialState = true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
