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
import mree.cloud.music.player.app.act.adapter.AlbumListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.tasks.FillAlbumInfo;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AlbumFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mainView;
    private GridView albumView;

    private AlbumListAdapter albumAdapter;
    private ArrayList<SongInfo> albumList;

    private OnFragmentInteractionListener mListener;
    private Handler addHandler;
    private EditText filterView;
    private InputMethodManager imm;

    public AlbumFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2){
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        addHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                Bundle data = msg.getData();
                SongInfo si = (SongInfo) data.getSerializable(AudioFragment.AUDIO_INFO);

                albumAdapter.add(si);
            }
        };


    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_album, container, false);

        if (mainView != null) {
            albumView = (GridView) mainView.findViewById(R.id.albumList);
        }

        filterView = (EditText) mainView.findViewById(R.id.filterView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    albumAdapter.getFilter().filter(s.toString());
                } else {
                    albumAdapter.clear();
                    albumAdapter.addAll(albumAdapter.getSongs());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        filterView.addTextChangedListener(textWatcher);

        albumView.requestFocus();

        fillAlbumList();
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
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    private void fillAlbumList(){
        ArrayList<HashMap<String, String>> allAudios;
        albumList = new ArrayList<>();
        allAudios = DbEntryService.getAllAudiosByAlbum();
        albumAdapter = new AlbumListAdapter(getContext(), R.layout.layout_album_grid);
        albumView.setAdapter(albumAdapter);
        albumView.setOnItemClickListener(getClickListener());
        AudioFragment.getThreadPool().submit(new FillAlbumInfo(addHandler, allAudios));

    }

    private AdapterView.OnItemClickListener getClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                SongInfo si = albumAdapter.getItem(position);
                Map<String, String> values = new HashMap<>();
                values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.ALBUM.getCode().toString());
                values.put(Constants.ALBUM_PARAM, si.getAlbum());
                values.put(Constants.ARTIST_PARAM, si.getArtist());
                Fragment fragment = AudioFragment.newInstance(values);
                FragmentManager fragmentManager = MainActivity.getFM();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                        .addToBackStack(Constants.ALBUM)
                        .commit();
                Message m = new Message();
                Bundle data = new Bundle();
                data.putString(MainActivity.TITLE, si.getAlbum());
                m.setData(data);
                MainActivity.titleHandler.sendMessage(m);
                // Highlight the selected item has been done by NavigationView
                // Set action bar title
               /* SongInfo si = albumAdapter.getItem(position);
                ArrayList<HashMap<String, String>> audiosOfAlbum = DbEntryService
                        .getAudiosOfAlbum(si.getAlbum(), si.getArtist());
                audioList = new ArrayList<>();
                audioAdapter = new AudioListAdapter(getContext(), R.layout.layout_audio_row,
                        audioList);
                HomeActivity.getThreadPool().submit(new FillAudioInfo(addHandler, finishHandler,
                        audiosOfAlbum));
                audioView.setAdapter(audioAdapter);
                albumView.setVisibility(View.GONE);
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
    public void onResume(){
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
    public interface OnFragmentInteractionListener{
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
