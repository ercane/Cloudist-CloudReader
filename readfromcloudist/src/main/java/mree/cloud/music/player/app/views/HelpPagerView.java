package mree.cloud.music.player.app.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;

/**
 * Created by eercan on 02.01.2017.
 */

public class HelpPagerView {
    private static final String TAG = HelpPagerView.class.getSimpleName();
    int[] mResources = {
            R.drawable.ic_album_black,
            R.drawable.ic_artist_black,
            R.drawable.ic_audio_dark,
            R.drawable.ic_down_dark,
            R.drawable.ic_dot_black,
            R.drawable.ic_volume_dark
    };
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ViewPager viewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ImageButton leftNav, rightNav;

    public HelpPagerView(Context context) {
        this.context = context;
        init();
    }


    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_help, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        AdMob.prepareBannerAd(context, (AdView) view.findViewById(R.id.adView),
                context.getString(R.string.banner_ad_id));

        mCustomPagerAdapter = new CustomPagerAdapter(context);

        viewPager = (ViewPager) view.findViewById(R.id.help_pager);
        viewPager.setAdapter(mCustomPagerAdapter);

        leftNav = (ImageButton) view.findViewById(R.id.left_nav);
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });
        rightNav = (ImageButton) view.findViewById(R.id.right_nav);
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });
    }


    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.layout_pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.pager_item);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
