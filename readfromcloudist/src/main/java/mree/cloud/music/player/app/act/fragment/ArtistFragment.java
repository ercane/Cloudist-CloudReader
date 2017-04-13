package mree.cloud.music.player.app.act.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.MainActivity;
import mree.cloud.music.player.app.act.adapter.ArtistListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.tasks.FillArtistInfo;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.FragmentType;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ArtistFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mainView;
    private GridView artistView;

    private ArtistListAdapter artistAdapter;
    private ArrayList<SongInfo> artistList;

    private OnFragmentInteractionListener mListener;
    private Handler addHandler;
    private EditText filterView;
    private InputMethodManager imm;

    public ArtistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
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
                SongInfo si = (SongInfo) data.getSerializable(AudioFragment.AUDIO_INFO);
                artistAdapter.add(si);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_artist, container, false);

        if (mainView != null) {
            artistView = (GridView) mainView.findViewById(R.id.artistList);
        }

        filterView = (EditText) mainView.findViewById(R.id.filterView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    artistAdapter.getFilter().filter(s.toString());
                } else {
                    artistAdapter.clear();
                    artistAdapter.addAll(artistAdapter.getSongs());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        filterView.addTextChangedListener(textWatcher);

        artistView.requestFocus();

        fillAudioList();
        AdMob.prepareBannerAd(getContext(), (AdView) mainView.findViewById(R.id.adView),
                getString(R.string.banner_ad_id));
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void fillAudioList() {
        ArrayList<HashMap<String, String>> allAudios;

        artistList = new ArrayList<>();
        allAudios = DbEntryService.getAllAudiosByArtist();

        artistAdapter = new ArtistListAdapter(getContext(), R.layout.layout_album_grid);
        artistView.setAdapter(artistAdapter);
        artistView.setOnItemClickListener(getClickListener());
        AudioFragment.getThreadPool().submit(new FillArtistInfo(addHandler, allAudios));


    }

    private AdapterView.OnItemClickListener getClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongInfo si = artistAdapter.getItem(position);
                Map<String, String> values = new HashMap<>();
                values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.ARTIST.getCode().toString());
                values.put(Constants.ARTIST_PARAM, si.getArtist());
                Fragment fragment = AudioFragment.newInstance(values);
                FragmentManager fragmentManager = MainActivity.getFM();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                        .addToBackStack(Constants.ARTIST)
                        .commit();
                Message m = new Message();
                Bundle data = new Bundle();
                data.putString(MainActivity.TITLE, si.getArtist());
                m.setData(data);
                MainActivity.titleHandler.sendMessage(m);
/*                SongInfo si = artistAdapter.getItem(position);
                ArrayList<HashMap<String, String>> audiosOfAlbum = DbEntryService
                .getAudiosOfArtist(si.getArtist());
                audioList = new ArrayList<>();
                audioAdapter = new AudioListAdapter(getContext(), R.layout.layout_audio_row,
                audioList);
                SettingsActivity.getPool().submit(new FillAudioInfo(addHandler, finishHandler,
                audiosOfAlbum));
                audioView.setAdapter(audioAdapter);
                artistView.setVisibility(View.GONE);
                audioView.setVisibility(View.VISIBLE);
                audioView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                    id) {
                        Bundle b = new Bundle();
                        List<SongInfo> songs = audioAdapter.getList();
                        AudioList listObject = new AudioList();
                        listObject.setList(songs);
                        b.putSerializable(HomeActivity.AUDIO_LIST, listObject);
                        b.putInt(HomeActivity.AUDIO_POSN, position);
                        Message m = new Message();
                        m.setData(b);
                        AudioFragment.getChangeMusicHandler().sendMessage(m);
                    }
                });*/
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
