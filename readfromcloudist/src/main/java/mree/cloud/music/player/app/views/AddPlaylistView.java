package mree.cloud.music.player.app.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.RandomStringUtils;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.audio.PlaylistType;

/**
 * Created by eercan on 16.11.2016.
 */

public class AddPlaylistView {
    private static final String TAG = AddPlaylistView.class.getSimpleName();
    private Context context;
    private List<SongInfo> songList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String songId;
    private PlaylistInfo playlistInfo;

    public AddPlaylistView(Context context) {
        this.context = context;
        init();
    }

    public AddPlaylistView(Context context, String songId) {
        this.context = context;
        this.songId = songId;
        init();
    }

    public AddPlaylistView(Context context, List<SongInfo> songList) {
        this.context = context;
        this.songList = songList;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_playlist_add, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setRecycleOnMeasureEnabled(false);

        final EditText accNameView = (EditText) view.findViewById(R.id.accNameView);
        final TextView msgView = (TextView) view.findViewById(R.id.msgView);
        Button piAddBtn = (Button) view.findViewById(R.id.addPlaylistButton);

        piAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accNameView.getText().toString().isEmpty() || "".equals(accNameView.getText()
                        .toString())) {
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText(R.string.add_account_name_error);
                } else if (DbEntryService.isPlaylistExist(accNameView.getText().toString())) {
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText(R.string.add_playlist_name_exist_error);
                } else if (accNameView.getText().toString().length() > Constants.MAX_NAME_LENGTH) {
                    msgView.setVisibility(View.VISIBLE);
                    msgView.setText(R.string.max_length_error + Constants.MAX_NAME_LENGTH);
                } else {
                    if (dialog != null)
                        dialog.dismiss();

                    playlistInfo = new PlaylistInfo();
                    playlistInfo.setName(accNameView.getText().toString());
                    playlistInfo.setType(PlaylistType.LOCAL);
                    playlistInfo.setCount(0);
                    playlistInfo.setOfflineStatus(0);
                    playlistInfo.setSongs(new ArrayList<SongInfo>());
                    playlistInfo.setCreatedDate(new Date());
                    playlistInfo.setId(RandomStringUtils.random(20));

                    if (songList == null) {
                        DbEntryService.savePlaylist(playlistInfo);
                        if (songId != null) {
                            Long paId = DbEntryService.saveAudioToPlaylist(playlistInfo.getId(),
                                    songId);
                            if (paId != null) {
                                String msg = context.getString(R.string.add_to_playlist_added_msg);
                                msg += " " + playlistInfo.getName();
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else if (songList != null) {
                        playlistInfo.setSongs(songList);
                        DbEntryService.savePlaylist(playlistInfo);
                        String msg = context.getString(R.string.add_to_playlist_added_msg);
                        msg += " " + playlistInfo.getName();
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                }
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

    public PlaylistInfo getPlaylistInfo() {
        return playlistInfo;
    }
}
