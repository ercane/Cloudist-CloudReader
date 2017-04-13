package mree.cloud.music.player.app.act.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.act.adapter.PlaylistListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.tasks.FillPlaylistInfo;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.app.utils.FileUtils;
import mree.cloud.music.player.app.views.AddPlaylistView;
import mree.cloud.music.player.common.model.PlaylistInfo;
import mree.cloud.music.player.common.ref.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaylistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = PlaylistFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mainView;
    private ListView playlistView;

    private PlaylistListAdapter playlistAdapter;
    private ArrayList<PlaylistInfo> playlistList;

    private PlaylistFragment.OnFragmentInteractionListener mListener;
    private Handler addHandler;
    private EditText filterView;
    private InputMethodManager imm;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        addHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                PlaylistInfo ai = (PlaylistInfo) data.getSerializable(AudioFragment.AUDIO_INFO);

                playlistAdapter.add(ai);
            }
        };


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_playlist, container, false);

        if (mainView != null) {
            playlistView = (ListView) mainView.findViewById(R.id.playlistList);
        }

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id
                .fabAddPlaylist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Add account", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                final AddPlaylistView playlistView = new AddPlaylistView(getContext());
                AlertDialog.Builder builder = playlistView.getBuilder();
                AlertDialog dialog = builder.create();
                playlistView.setDialog(dialog);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (playlistAdapter != null) {
                            playlistAdapter.add(playlistView.getPlaylistInfo());
                        }
                    }
                });
            }
        });


        filterView = (EditText) mainView.findViewById(R.id.filterView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    playlistAdapter.getFilter().filter(s.toString());
                } else {
                    playlistAdapter.clear();
                    playlistAdapter.addAll(playlistAdapter.getList());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        filterView.addTextChangedListener(textWatcher);
        playlistView.requestFocus();
        fillPlaylistList();
        AdMob.prepareBannerAd(getContext(), (AdView) mainView.findViewById(R.id.adView),
                getString(R.string.banner_ad_id));
        return mainView;
    }

    private AbsListView.MultiChoiceModeListener getMultiChoiceListener() {
        return new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                final int checkedCount = playlistView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                playlistAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.playlist_opt_context, menu);
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
                        playlistAdapter.removeSelection();
                        playlistView.clearChoices();
                        for (int i = 0; i < playlistAdapter.getList().size(); i++) {
                            playlistView.setItemChecked(i, true);
                        }
                        break;
                    case R.id.item_remove:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Remove Playlists");
                        builder.setMessage("Are you sure to remove selected playlists?");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SparseBooleanArray selected = playlistAdapter
                                                .getSelectedIds();
                                        // Captures all selected ids with a loop
                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                PlaylistInfo acc = playlistAdapter.getItem
                                                        (selected.keyAt(i));
                                                if (acc.getOfflineStatus() == 1) {
                                                    FileUtils.removeOfflinePlaylistFiles(acc
                                                            .getId());
                                                }
                                                DbEntryService.removePlaylistByPlaylist(acc.getId
                                                        ());
                                                playlistAdapter.remove(acc);
                                            }
                                        }
                                        mode.finish();
                                    }
                                });

                        builder.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        mode.finish();
                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                playlistAdapter.removeSelection();
            }
        };
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
                            filterView.clearComposingText();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void fillPlaylistList() {
        if (playlistAdapter != null) {
            playlistAdapter.clear();
        }
        ArrayList<HashMap<String, String>> allPlaylists;
        playlistList = new ArrayList<>();
        allPlaylists = DbEntryService.getAllPlaylists();
        playlistAdapter = new PlaylistListAdapter(getContext(), R.layout.layout_playlist_row);
        playlistView.setAdapter(playlistAdapter);
        playlistView.setOnItemClickListener(getClickListener());
        playlistView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        playlistView.setMultiChoiceModeListener(getMultiChoiceListener());
        AudioFragment.getThreadPool().submit(new FillPlaylistInfo(addHandler, allPlaylists));

    }

    private AdapterView.OnItemClickListener getClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    PlaylistInfo pi = playlistAdapter.getItem(position);
                    Map<String, String> values = new HashMap<>();
                    values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.PLAYLIST.getCode()
                            .toString
                                    ());
                    values.put(Constants.PLAYLIST_PARAM, pi.getId());
                    Fragment fragment = AudioFragment.newInstance(values);
                    FragmentManager fragmentManager = MainActivity.getFM();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                            .addToBackStack(Constants.PLAYLIST)
                            .commit();
                    Message m = new Message();
                    Bundle data = new Bundle();
                    data.putString(MainActivity.TITLE, pi.getName());
                    m.setData(data);
                    MainActivity.titleHandler.sendMessage(m);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + "");
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
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
