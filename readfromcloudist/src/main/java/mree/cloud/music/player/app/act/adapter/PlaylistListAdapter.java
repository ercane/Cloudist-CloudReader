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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.app.views.PlaylistOptionView;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.ref.SourceType;
import mree.cloud.music.player.common.ref.audio.PlaylistType;

/**
 * Created by eercan on 11.11.2016.
 */

public class PlaylistListAdapter extends ArrayAdapter<PlaylistInfo> implements Filterable {

    private Context context;
    private int resource;
    private SparseBooleanArray mSelectedItemsIds;
    private List<PlaylistInfo> list;
    private List<PlaylistInfo> filteredLists;


    public PlaylistListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        list = new ArrayList<>();
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public void add(PlaylistInfo object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public void addAll(Collection<? extends PlaylistInfo> collection) {
        super.addAll(collection);
        // list.addAll(collection);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);
        }


        try {
            final PlaylistInfo pi = getItem(position);
            ImageView ivThumb = (ImageView) row.findViewById(R.id.ivAlbum);
            TextView sourceType = (TextView) row.findViewById(R.id.tvSource);
            TextView title = (TextView) row.findViewById(R.id.tvAudioTitle);
            TextView count = (TextView) row.findViewById(R.id.tvCount);
            TextView status = (TextView) row.findViewById(R.id.tvStatus);
            ImageButton optButton = (ImageButton) row.findViewById(R.id.opsButton);

            if (pi.getName() != null) {
                title.setText(pi.getName());
            }

            count.setText("Total: " + DbEntryService.getPlaylistCount(pi.getId()));


            if (pi.getType() != null && pi.getType() == PlaylistType.SPOTIFY) {
                IconHelper.setSourceIcon(sourceType, SourceType.SPOTIFY);
                sourceType.setVisibility(View.VISIBLE);
            } else {
                sourceType.setVisibility(View.GONE);
            }

            if (pi.getOfflineStatus() == 1) {
                status.setVisibility(View.VISIBLE);
            } else {
                status.setVisibility(View.INVISIBLE);
            }

            ivThumb.setImageResource(R.drawable.ic_playlist_dark);

            optButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaylistOptionView optView = new PlaylistOptionView(context,
                            PlaylistListAdapter.this, pi);
                    AlertDialog.Builder builder = optView.getBuilder();
                    AlertDialog dialog = builder.create();
                    optView.setDialog(dialog);
                    dialog.show();
                }
            });

         /*   if (removeBtn != null) {
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Remove Playlist");
                        builder.setMessage("Are you sure to remove '" + pi.getName() + "' ?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DbEntryService.removePlaylistByPlaylist(pi.getId());
                                        remove(pi);
                                        dialog.cancel();
                                    }
                                });

                        builder.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                });
            }*/

/*        if (pi.getThumbnail() != null) {
            //Bitmap bitmap = BitmapFactory.decodeByteArray(song.getThumbnail(), 0, song
            // .getThumbnail().length);
            File filesDir = context.getFilesDir();
            String path = filesDir.getAbsolutePath() + "/" + pi.getThumbnail();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivThumb.setImageBitmap(bitmap);
        } else {
            ivThumb.setImageResource(R.drawable.default_cover);
            //ivThumb.setPadding(0, 5, 0, 5);
            //ivThumb.setBackgroundResource(R.drawable.blue_border);
        }*/

            //ivThumb.setImageResource(R.drawable.default_cover);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSelectedItemsIds.get(position)) {
            row.setBackgroundResource(R.drawable.adapter_selected_bckgrnd);
        } else {
            row.setBackgroundColor(Color.TRANSPARENT);
        }
        return row;
    }


    public List<PlaylistInfo> getList() {
        return list;
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
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                PlaylistListAdapter.this.clear();
                filteredLists = (ArrayList<PlaylistInfo>) results.values;
                if (filteredLists == null)
                    filteredLists = new ArrayList<>();
                PlaylistListAdapter.this.addAll(filteredLists);
                PlaylistListAdapter.this.notifyDataSetChanged();
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<PlaylistInfo> FilteredArrayNames = new ArrayList<PlaylistInfo>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < list.size(); i++) {
                    String dataNames = list.get(i).getName();
                    if (!TextUtils.isEmpty(dataNames) && dataNames.toLowerCase().contains
                            (constraint.toString())) {
                        FilteredArrayNames.add(list.get(i));
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
}