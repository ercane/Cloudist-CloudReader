package mree.cloud.music.player.app.bill.ad.admob;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.services.CmpDeviceService;

/**
 * Created by mree on 11.12.2016.
 */

public class AdMob {

    public static final String TAG = AdMob.class.getSimpleName();
    private static InterstitialAd periodicAd;
    private static InterstitialAd infiniteAd;
    private static boolean isBannerInitiliazed = false;

    public static void initiliazeForBanner(Context context, String adUnitId) {
        if (!isBannerInitiliazed) {
            MobileAds.initialize(context, adUnitId);
            isBannerInitiliazed = true;
        }
    }

    public static void prepareBannerAd(Context context, AdView mAdView, String adUnitId) {
        try {
            if (!CmpDeviceService.getPreferencesService().getAdState()) {
                initiliazeForBanner(context, adUnitId);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.e(TAG, "Closed ");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e(TAG, "Failed: " + i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        Log.e(TAG, "left ");
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        Log.e(TAG, "Opened ");
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e(TAG, "Loaded ");
                    }
                });
                mAdView.loadAd(adRequest);
            } else {
                mAdView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + " ");
        }
    }


    public static void prepareInfiniteAds(AdListener listener) {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            getInfiniteAd().setAdListener(listener);
        }
    }

    public static void requestNewInfinite() {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            getInfiniteAd().loadAd(adRequest);
        }
    }

    public static void preparePeriodicAds(AdListener listener) {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            getPeriodicAd().setAdListener(listener);
        }
    }

    public static void requestNewPeriodic() {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            getPeriodicAd().loadAd(adRequest);
        }
    }

    public static InterstitialAd getPeriodicAd() {
        if (periodicAd == null) {
            periodicAd = new InterstitialAd(CmpDeviceService.getContext());
            periodicAd.setAdUnitId(CmpDeviceService.getContext().getString(R.string
                    .interstital_periodic_ad_id));
        }
        return periodicAd;
    }


    public static InterstitialAd getInfiniteAd() {
        if (infiniteAd == null) {
            infiniteAd = new InterstitialAd(CmpDeviceService.getContext());
            infiniteAd.setAdUnitId(CmpDeviceService.getContext().getString(R.string
                    .interstital_limitless_id));
        }
        return infiniteAd;
    }

    public static boolean isPeriodicLoaded() {
        if (CmpDeviceService.getPreferencesService().getAdState()) {
            return false;
        } else {
            return getPeriodicAd().isLoaded();
        }
    }

    public static boolean isInfiniteLoaded() {
        if (CmpDeviceService.getPreferencesService().getAdState()) {
            return false;
        } else {
            return getInfiniteAd().isLoaded();
        }
    }

    public static void showPeriodic() {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            getPeriodicAd().show();
        }
    }

    public static void showInfinite() {
        if (!CmpDeviceService.getPreferencesService().getAdState()) {
            getInfiniteAd().show();
        }
    }


}
