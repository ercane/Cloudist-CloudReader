package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.ConnectivityHelper;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.app.utils.ImageUtils;
import mree.cloud.music.player.app.utils.lazylist.LazyListImageLoader;
import mree.cloud.music.player.app.views.AudioOptionView;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.FragmentType;

/**
 * Created by mree on 24.01.2016.
 */
public class AudioListAdapter extends ArrayAdapter<SongInfo> implements Filterable{


    //public ImageLoaderTask lazyListImageLoader= ImageUtils.getLazyImageLoaderTask();
    public LazyListImageLoader lazyListImageLoader;
    private int layoutResource;
    private LayoutInflater inflater;
    private ListView listView;
    private SparseBooleanArray mSelectedItemsIds;
    private Context context;
    private FragmentType type;
    private String typeValue;
    private List<SongInfo> songs;
    private List<SongInfo> filteredSongs;


    public AudioListAdapter(Context context, int resource, FragmentType type, String typeValue){
        super(context, resource);
        this.context = context;
        this.type = type;
        this.typeValue = typeValue;
        this.songs = new ArrayList<>();
        lazyListImageLoader = ImageUtils.getLazyImageLoaderTask(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void add(SongInfo object){
        super.add(object);
        songs.add(object);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_audio_row, parent, false);
        }


        final SongInfo song = getItem(position);
        final ImageView ivThumb = (ImageView) row.findViewById(R.id.ivAlbum);
        TextView title = (TextView) row.findViewById(R.id.tvAudioTitle);
        TextView album = (TextView) row.findViewById(R.id.tvAlbum);
        TextView artist = (TextView) row.findViewById(R.id.tvArtist);
        TextView sourceType = (TextView) row.findViewById(R.id.tvSource);
        TextView status = (TextView) row.findViewById(R.id.tvStatus);
        ImageButton optButton = (ImageButton) row.findViewById(R.id.opsButton);

        if (song.getTitle() != null) {
            title.setText(song.getTitle());
        }

        if (song.getAlbum() != null) {
            album.setText(song.getAlbum());
        } else {
            album.setText(Constants.DEFAULT_ALBUM);
        }

        if (song.getArtist() != null) {
            artist.setText(song.getArtist());
        } else {
            artist.setText(Constants.DEFAULT_ARTIST);
        }

        if (song.getSourceType() != null) {
            IconHelper.setSourceIcon(sourceType, song.getSourceType());
        }

        if (song.getStatus() != null) {
            switch (song.getStatus()) {
                case ONLINE:
                    status.setVisibility(View.INVISIBLE);
                    break;
                case OFFLINE:
                    status.setVisibility(View.VISIBLE);
                    break;
                case CACHED:
                    status.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        if (song.getThumbnail() != null) {
            //Bitmap bitmap = BitmapFactory.decodeByteArray(song.getThumbnail(), 0, song
            // .getThumbnail().length);
     /*       File filesDir = context.getFilesDir();
            String path = filesDir.getAbsolutePath() + "/" + song.getThumbnail();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivThumb.setImageBitmap(bitmap);*/
            try {
           /*     lazyListImageLoader.loadImage(song.getThumbnail(), new ImageLoaderTask
           .ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Bitmap imageBitmap) {
                        ivThumb.setImageBitmap(imageBitmap);
                    }
                });*/
            /*    if (song.getSourceType() == SourceType.ONEDRIVE) {
                    lazyListImageLoader.DisplayImage(CmpDeviceService.getOneDriveRestClient(song
                            .getAccountId()), song.getAccountId(), song.getId(), ivThumb);
                } else {
                    lazyListImageLoader.DisplayImage(song.getThumbnail(), ivThumb);
                }*/
                ImageUtils.setCoverImage(context, song.getSourceType(),
                        song.getThumbnail(), ivThumb);
            } catch (Exception e) {
                ivThumb.setImageResource(R.drawable.default_cover);
            }
        } else {
            ivThumb.setImageResource(R.drawable.default_cover);
            //ivThumb.setPadding(0, 5, 0, 5);
            //ivThumb.setBackgroundResource(R.drawable.blue_border);
        }

        optButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AudioOptionView optView;
                if (type != FragmentType.PLAYLIST) {
                    optView = new AudioOptionView(context, song);
                } else {
                    PlaylistInfo playlistInfo = DbEntryService.getPlaylistInfo(typeValue);
                    optView = new AudioOptionView(context, song, playlistInfo, AudioListAdapter
                            .this);
                }
                AlertDialog.Builder builder = optView.getBuilder();
                AlertDialog dialog = builder.create();
                optView.setDialog(dialog);
                dialog.show();
            }
        });
        row.setBackgroundResource(R.drawable.audio_row_bckgrnd);
        if (!ConnectivityHelper.isConnected(context)) {
            switch (song.getStatus()) {
                case ONLINE:
                    row.setAlpha(.5f);
                    break;
                case OFFLINE:
                case CACHED:
                    row.setAlpha(1);
                    break;
            }
        } else {
            row.setAlpha(1);
        }

        if (mSelectedItemsIds.get(position)) {
            row.setBackgroundResource(R.drawable.adapter_selected_bckgrnd);
        } else {
            row.setBackgroundColor(Color.TRANSPARENT);
        }
        return row;
    }

    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

    public List<SongInfo> getSongs(){
        return songs;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    @Override
    public Filter getFilter(){

        Filter filter = new Filter(){

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results){
                AudioListAdapter.this.clear();
                filteredSongs = (ArrayList<SongInfo>) results.values;
                AudioListAdapter.this.addAll(filteredSongs);
                AudioListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint){

                FilterResults results = new FilterResults();
                ArrayList<SongInfo> FilteredArrayNames = new ArrayList<SongInfo>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < songs.size(); i++) {
                    String dataNames = songs.get(i).getTitle();
                    if (!TextUtils.isEmpty(dataNames) &&
                            dataNames.toLowerCase().contains(constraint.toString())) {
                        FilteredArrayNames.add(songs.get(i));
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

    @Override
    public boolean isEnabled(int position){
        SongInfo song = getItem(position);
        if (!ConnectivityHelper.isConnected(context)) {
            switch (song.getStatus()) {
                case ONLINE:
                    return false;
                case OFFLINE:
                    return true;
                case CACHED:
                    return true;
                default:
                    return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void clear(){
        super.clear();
        songs.clear();
    }
}
