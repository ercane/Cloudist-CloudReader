package mree.cloud.music.player.app.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;

import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.common.model.SongInfo;

/**
 * Created by eercan on 25.11.2016.
 */

public class AlbumOptionView {
    private static final String TAG = AlbumOptionView.class.getSimpleName();
    private Context context;
    private List<SongInfo> songList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;


    public AlbumOptionView(Context context, List<SongInfo> songList) {
        this.context = context;
        this.songList = songList;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_album_options, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        LinearLayout addToPlaylist = (LinearLayout) view.findViewById(R.id.addToPlaylist);

        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                AddToPlaylistView addToView = new AddToPlaylistView(context, songList);
                AlertDialog.Builder builder1 = addToView.getBuilder();
                AlertDialog dialog1 = builder1.create();
                addToView.setDialog(dialog1);
                dialog1.show();
            }
        });

        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));
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
