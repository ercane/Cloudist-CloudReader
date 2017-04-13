package mree.cloud.music.player.app.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.adapter.PlaylistListAdapter;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.common.model.PlaylistInfo;

/**
 * Created by eercan on 27.03.2017.
 */

public class PlaylistOptionView extends AbstractView {

    private PlaylistListAdapter adapter;
    private PlaylistInfo playlistInfo;

    public PlaylistOptionView(Context context, PlaylistListAdapter adapter, PlaylistInfo
            playlistInfo) {
        super(context);
        this.adapter = adapter;
        this.playlistInfo = playlistInfo;
        init();
    }

    @Override
    void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_playlist_options, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);


        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));

        LinearLayout downloadLayout = (LinearLayout) view.findViewById(R.id.downloadLayout);
        TextView addRemoveBtn = (TextView) view.findViewById(R.id.tvDownload);
        if (playlistInfo.getOfflineStatus() == 0) {
            addRemoveBtn.setText(context.getString(R.string.make_offline));
            downloadLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioFragment.changeOfflineStatus(true, playlistInfo.getId(), DbEntryService
                            .getAudiosOfPlaylist(playlistInfo.getId()));
                    playlistInfo.setOfflineStatus(1);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });
        } else {
            addRemoveBtn.setText(context.getString(R.string.make_online));
            downloadLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioFragment.changeOfflineStatus(false, playlistInfo.getId(), DbEntryService
                            .getAudiosOfPlaylist(playlistInfo.getId()));
                    playlistInfo.setOfflineStatus(0);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });
        }


        LinearLayout removeLayout = (LinearLayout) view.findViewById(R.id.removeLayout);
        removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove Playlist");
                builder.setMessage("Are you sure to remove '" + playlistInfo.getName() + "' ?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                if (playlistInfo.getOfflineStatus() == 1) {
                                    FileUtils.removeOfflinePlaylistFiles(playlistInfo.getId());
                                }
                                DbEntryService.removePlaylistByPlaylist(playlistInfo.getId());
                                if (adapter != null) {
                                    adapter.remove(playlistInfo);
                                }
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

    }
}
