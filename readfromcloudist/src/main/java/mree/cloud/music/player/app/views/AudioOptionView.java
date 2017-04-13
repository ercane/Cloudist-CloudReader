package mree.cloud.music.player.app.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.services.CmpDeviceService;
import mree.cloud.music.player.app.tasks.DownloadAudioTask;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.utils.OfflineHelper;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.FragmentType;
import mree.cloud.music.player.common.ref.audio.AudioStatus;

/**
 * Created by eercan on 23.11.2016.
 */

public class AudioOptionView{
    private static final String TAG = AudioOptionView.class.getSimpleName();
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SongInfo songInfo;
    private boolean fromOut;
    private PlaylistInfo playlistInfo;
    private ArrayAdapter<SongInfo> adapter;

    public AudioOptionView(Context context, SongInfo songInfo){
        this.context = context;
        this.songInfo = songInfo;
        this.fromOut = false;
        init();
    }

    public AudioOptionView(Context context, SongInfo songInfo, boolean fromOut){
        this.context = context;
        this.songInfo = songInfo;
        this.fromOut = fromOut;
        init();
    }

    public AudioOptionView(Context context, SongInfo songInfo, PlaylistInfo playlistInfo,
                           ArrayAdapter<SongInfo> adapter){
        this.context = context;
        this.songInfo = songInfo;
        this.playlistInfo = playlistInfo;
        this.adapter = adapter;
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_audio_options, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);


        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));

        LinearLayout addToPlaylist = (LinearLayout) view.findViewById(R.id.addToPlaylist);
        if (playlistInfo != null) {
            TextView addRemoveBtn = (TextView) view.findViewById(R.id.addToPlaylistBtn);
            addRemoveBtn.setText(context.getString(R.string.remove_from_playlist));
            addToPlaylist.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //dialog.dismiss();
                    DbEntryService.removeAudioFromPlaylist(playlistInfo.getId(), songInfo.getId());
                    adapter.remove(songInfo);
                    if (playlistInfo.getOfflineStatus() == 1) {
                        DbEntryService.updateAudioOfflineStatus(songInfo.getId(), AudioStatus
                                .ONLINE.getCode());
                        FileUtils.removeOfflineFile(songInfo.getId());
                    }
                    dialog.dismiss();
                }
            });
        } else {
            addToPlaylist.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //dialog.dismiss();
                    AddToPlaylistView addToView = new AddToPlaylistView(context, songInfo);
                    AlertDialog.Builder builder1 = addToView.getBuilder();
                    AlertDialog dialog1 = builder1.create();
                    addToView.setDialog(dialog1);
                    dialog1.show();
                }
            });
        }


        LinearLayout goToArtist = (LinearLayout) view.findViewById(R.id.goToArtist);
        goToArtist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Map<String, String> values = new HashMap<>();
                values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.ARTIST.getCode().toString());
                values.put(Constants.ARTIST_PARAM, songInfo.getArtist());
                Fragment fragment = AudioFragment.newInstance(values);
                FragmentManager fragmentManager = MainActivity.getFM();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                        .addToBackStack(Constants.AUDIO)
                        .commit();
                Message m = new Message();
                Bundle data = new Bundle();
                data.putString(MainActivity.TITLE, songInfo.getArtist());
                m.setData(data);
                MainActivity.titleHandler.sendMessage(m);
                dialog.dismiss();
            }
        });

        LinearLayout goToAlbum = (LinearLayout) view.findViewById(R.id.goToAlbum);
        goToAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Map<String, String> values = new HashMap<>();
                values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.ALBUM.getCode().toString());
                values.put(Constants.ALBUM_PARAM, songInfo.getAlbum());
                values.put(Constants.ARTIST_PARAM, songInfo.getArtist());
                Fragment fragment = AudioFragment.newInstance(values);
                FragmentManager fragmentManager = MainActivity.getFM();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                        .addToBackStack(Constants.AUDIO)
                        .commit();
                Message m = new Message();
                Bundle data = new Bundle();
                data.putString(MainActivity.TITLE, songInfo.getAlbum());
                m.setData(data);
                MainActivity.titleHandler.sendMessage(m);
                dialog.dismiss();
            }
        });

        LinearLayout downloadLayout = (LinearLayout) view.findViewById(R.id.downloadLayout);
        TextView download = (TextView) view.findViewById(R.id.tvDownload);
        if (songInfo.getStatus() == AudioStatus.OFFLINE) {
            download.setText("Make Online");
            downloadLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    OfflineHelper.changeStatus(songInfo.getId(), AudioStatus.ONLINE);
                    Toast.makeText(context, context.getString(R.string.make_online_msg), Toast
                            .LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        } else {
            download.setText("Make Offline");
            downloadLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    DownloadAudioTask task = new DownloadAudioTask(context, songInfo);
                    CmpDeviceService.getDownloadExecutor().submit(task);
                    Toast.makeText(context, context.getString(R.string.download_start_msg), Toast
                            .LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }

        switch (songInfo.getSourceType()) {
            case LOCAL:
            case SPOTIFY:
                downloadLayout.setVisibility(View.GONE);
                break;
        }

        if (fromOut) {
            goToAlbum.setVisibility(View.GONE);
            goToArtist.setVisibility(View.GONE);
        }
    }

    public AlertDialog.Builder getBuilder(){
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder){
        this.builder = builder;
    }

    public AlertDialog getDialog(){
        return dialog;
    }

    public void setDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    public SongInfo getSongInfo(){
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo){
        this.songInfo = songInfo;
    }
}
