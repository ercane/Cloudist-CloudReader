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
import mree.cloud.music.player.app.act.adapter.AccountListAdapter;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.app.tasks.FillAccountInfo;
import mree.cloud.music.player.app.utils.Constants;
import mree.cloud.music.player.common.model.SourceInfo;
import mree.cloud.music.player.common.ref.FragmentType;
import mree.cloud.music.player.common.ref.auth.SourceState;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AccountFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mainView;
    private GridView accountView;

    private AccountListAdapter accountAdapter;
    private ArrayList<SourceInfo> accountList;

    private AccountFragment.OnFragmentInteractionListener mListener;
    private Handler addHandler;
    private EditText filterView;
    private InputMethodManager imm;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
                SourceInfo ai = (SourceInfo) data.getSerializable(AudioFragment.AUDIO_INFO);

                accountAdapter.add(ai);
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
        mainView = inflater.inflate(R.layout.fragmant_account, container, false);

        if (mainView != null) {
            accountView = (GridView) mainView.findViewById(R.id.accountList);
        }

        filterView = (EditText) mainView.findViewById(R.id.filterView);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    accountAdapter.getFilter().filter(s.toString());
                } else {
                    accountAdapter.clear();
                    accountAdapter.addAll(accountAdapter.getAccounts());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        filterView.addTextChangedListener(textWatcher);

        accountView.requestFocus();
        try {
            fillAccountList();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void fillAccountList() {
        ArrayList<HashMap<String, String>> allAccounts;
        accountList = new ArrayList<>();
        allAccounts = DbEntryService.getAllAccountsByState(SourceState.AUTH);
        accountAdapter = new AccountListAdapter(getContext(), R.layout.layout_album_grid);
        accountView.setAdapter(accountAdapter);
        accountView.setOnItemClickListener(getClickListener());
        AudioFragment.getThreadPool().submit(new FillAccountInfo(addHandler, allAccounts));

    }

    private AdapterView.OnItemClickListener getClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SourceInfo ai = accountAdapter.getItem(position);
                Map<String, String> values = new HashMap<>();
                values.put(Constants.FRAGMENT_TYPE_PARAM, FragmentType.ACCOUNT.getCode().toString
                        ());
                values.put(Constants.SOURCE_PARAM, ai.getId());
                Fragment fragment = AudioFragment.newInstance(values);
                FragmentManager fragmentManager = MainActivity.getFM();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
                        .addToBackStack(Constants.ACCOUNT)
                        .commit();

                Message m = new Message();
                Bundle data = new Bundle();
                data.putString(MainActivity.TITLE, ai.getName());
                m.setData(data);
                MainActivity.titleHandler.sendMessage(m);
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
