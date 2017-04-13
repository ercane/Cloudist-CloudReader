package mree.cloud.music.player.app.act.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.utils.ImageUtils;
import mree.cloud.music.player.app.utils.lazylist.LazyListImageLoader;
import mree.cloud.music.player.common.model.SongInfo;

/**
 * Created by mree on 07.02.2016.
 */
public class AlbumListAdapter extends ArrayAdapter<SongInfo> implements Filterable {

    private Context context;
    private List<SongInfo> songs;
    private List<SongInfo> filteredSongs;
    //private ImageLoaderTask lazyListImageLoader = ImageUtils.getLazyImageLoaderTask();
    private LazyListImageLoader lazyListImageLoader;

    public AlbumListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.songs = new ArrayList<>();
        lazyListImageLoader = ImageUtils.getLazyImageLoaderTask(context);
    }

    @Override
    public void add(SongInfo object) {
        super.add(object);
        songs.add(object);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_album_grid, parent, false);
        }


        SongInfo song = getItem(position);
        final ImageView ivThumb = (ImageView) row.findViewById(R.id.ivAlbum);
        TextView title = (TextView) row.findViewById(R.id.tvAudioTitle);

        if (song.getAlbum() != null) {
            title.setText(song.getAlbum());
        }


        if (song.getThumbnail() != null) {
            //Bitmap bitmap = BitmapFactory.decodeByteArray(song.getThumbnail(), 0, song
            // .getThumbnail().length);
            /*File filesDir = context.getFilesDir();
            String path = filesDir.getAbsolutePath() + "/" + song.getThumbnail();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivThumb.setImageBitmap(bitmap);*/
            try {
            /*    lazyListImageLoader.loadImage(song.getThumbnail(), new ImageLoaderTask
            .ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Bitmap imageBitmap) {
                        ivThumb.setImageBitmap(imageBitmap);
                    }
                });*/
            /*    if (song.getSourceType()!= SourceType.ONEDRIVE) {
                    lazyListImageLoader.DisplayImage(song.getThumbnail(), ivThumb);
                } else {
                    lazyListImageLoader.DisplayImage(CmpDeviceService.getOneDriveRestClient(song
                            .getAccountId()), song.getAccountId(), song.getId(), ivThumb);
                }*/
                ImageUtils.setCoverImage(context, song.getSourceType(), song.getThumbnail(),
                        ivThumb);
            } catch (Exception e) {
                ivThumb.setImageResource(R.drawable.default_cover);
            }
        } else {
            ivThumb.setImageResource(R.drawable.default_cover);
            //ivThumb.setPadding(0, 5, 0, 5);
            //ivThumb.setBackgroundResource(R.drawable.blue_border);
        }


        return row;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                AlbumListAdapter.this.clear();
                filteredSongs = (ArrayList<SongInfo>) results.values;
                AlbumListAdapter.this.addAll(filteredSongs);
                AlbumListAdapter.this.notifyDataSetChanged();

            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<SongInfo> FilteredArrayNames = new ArrayList<SongInfo>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < songs.size(); i++) {
                    String dataNames = songs.get(i).getAlbum();
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

    public List<SongInfo> getSongs() {
        return songs;
    }
}