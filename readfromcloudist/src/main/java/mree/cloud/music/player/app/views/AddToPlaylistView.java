package mree.cloud.music.player.app.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.adapter.PlaylistListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.tasks.DownloadAudioTask;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.audio.PlaylistType;


/**
 * Created by eercan on 24.11.2016.
 */

public class AddToPlaylistView {
    private static final String TAG = AddPlaylistView.class.getSimpleName();
    private Context context;
    private List<SongInfo> songList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SongInfo songInfo;
    private ListView playlistView;
    private List<PlaylistInfo> playlists;
    private PlaylistListAdapter playlistAdapter;
    private View view;
    private ProgressBar progressBar;

    public AddToPlaylistView(Context context, SongInfo songInfo) {
        this.context = context;
        this.songInfo = songInfo;
        init();
    }

    public AddToPlaylistView(Context context, List<SongInfo> songList) {
        this.context = context;
        this.songList = songList;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_add_to_playlist, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        playlistView = (ListView) view.findViewById(R.id.playlistList);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        playlistView.setVisibility(View.GONE);

        final AsyncTask<Void, Void, Boolean> fillList = getFillListTask();
        Button addToNew = (Button) view.findViewById(R.id.addToNewBtn);
        addToNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlaylistView addToView;
                if (songList == null) {
                    addToView = new AddPlaylistView(context, songInfo.getId());
                } else {
                    addToView = new AddPlaylistView(context, songList);
                }
                AlertDialog.Builder builder1 = addToView.getBuilder();
                AlertDialog dialog1 = builder1.create();
                addToView.setDialog(dialog1);
                dialog1.show();
                dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getFillListTask().execute();
                    }
                });
            }
        });
        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));
        fillList.execute();
    }

    private AsyncTask<Void, Void, Boolean> getFillListTask() {
        return new AsyncTask<Void, Void, Boolean>() {
            ArrayList<HashMap<String, String>> allPlaylists;

            @Override
            protected void onPreExecute() {
                playlists = new ArrayList<>();
                //  PlaylistInfo first = new PlaylistInfo();
                // first.setId("-255");
                // first.setName(context.getString(R.string.add_to_playlist_create_msg));
                // first.setType(PlaylistType.NONE);
                // playlists.add(first);
                allPlaylists = DbEntryService.getAllPlaylistsByType(PlaylistType.LOCAL.getCode());
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    for (Map<String, String> item : allPlaylists) {
                        PlaylistInfo pi = new PlaylistInfo();
                        pi.setId(item.get(DbConstants.PLAYLIST_ID));
                        pi.setType(PlaylistType.get(Integer.parseInt(item.get(DbConstants
                                .PLAYLIST_TYPE))));
                        pi.setName(item.get(DbConstants.PLAYLIST_NAME));
                        pi.setOfflineStatus(Integer.parseInt(item.get(DbConstants
                                .PLAYLIST_OFFLINE_STATUS)));
                        playlists.add(pi);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean state) {
                progressBar.setVisibility(View.GONE);
                if (state) {

                    playlistAdapter = new PlaylistListAdapter(context, R
                            .layout.layout_add_to_playlist_row);
                    playlistAdapter.addAll(playlists);
                    playlistView.setAdapter(playlistAdapter);
                    playlistView.setOnItemClickListener(getItemClickListener());
                }
                playlistView.setVisibility(View.VISIBLE);
            }
        };
    }

    private AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playlistAdapter != null) {
                    PlaylistInfo pi = playlistAdapter.getItem(position);
                    if (songList == null) {
                        if (!DbEntryService.isAudioAddedToPlaylist(pi.getId(), songInfo.getId())) {
                            Long paId = DbEntryService.saveAudioToPlaylist(pi.getId(),
                                    songInfo.getId());
                            if (paId != null && paId > 0) {
                                String msg = context.getString(R.string.add_to_playlist_added_msg) +
                                        " ";
                                msg += pi.getName();
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                if (pi.getOfflineStatus() == 1) {
                                    DownloadAudioTask task = new DownloadAudioTask(context,
                                            songInfo);
                                    CmpDeviceService.getDownloadExecutor().submit(task);
                                }
                            } else {
                                Toast.makeText(context, "An error occured!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(context, "Already added!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        List<Long> ids = new ArrayList<>();
                        for (SongInfo si : songList) {
                            if (!DbEntryService.isAudioAddedToPlaylist(pi.getId(), si.getId())) {
                                Long paId = DbEntryService.saveAudioToPlaylist(si.getAccountId(),
                                        si.getId());
                                if (paId != null && paId > 0) {
                                    ids.add(paId);
                                    if (pi.getOfflineStatus() == 1) {
                                        DownloadAudioTask task = new DownloadAudioTask(context, si);
                                        CmpDeviceService.getDownloadExecutor().submit(task);
                                    }
                                }
                            }
                        }

                        if (ids.size() == songList.size()) {
                            String msg = context.getString(R.string.add_to_playlist_added_msg) +
                                    " ";
                            msg += pi.getName();
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "An error occured!", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        };
    }


    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
}
