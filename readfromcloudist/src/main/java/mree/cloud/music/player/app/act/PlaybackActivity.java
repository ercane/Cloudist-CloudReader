package mree.cloud.music.player.app.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.bill.ad.admob.AdMob;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.utils.CountDownTimerWithPause;
import mree.cloud.music.player.app.utils.IconHelper;
import mree.cloud.music.player.app.utils.ImageUtils;
import mree.cloud.music.player.app.utils.OnSwipeTouchListener;
import mree.cloud.music.player.app.utils.ScrollTextView;
import mree.cloud.music.player.app.utils.lazylist.LazyListImageLoader;

import static mree.cloud.music.player.app.R.drawable.ic_shuffle_white;


public class PlaybackActivity extends AppCompatActivity{
    public static final String SHUFFLE_STATE = "shuffle";
    public static final String LOOP_ONE_STATE = "loop_one";
    public static final String LOOP_STATE = "shuffle";
    private static final String TAG = PlaybackActivity.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");
    private static CountDownTimerWithPause timer;
    private static Handler readyHandler;
    private static Handler refreshHandler;
    //private static service.getCurrentSong() service.getCurrentSong();
    private TextView tvSource;
    private MusicService service;
    private ScrollTextView tvName;
    private TextView tvAlbum;
    private TextView tvArtist;
    private TextView tvDuration;
    private ImageView ivCover;
    private SeekBar seekBar;
    private ImageButton downView;
    private ImageButton optView;
    private ImageButton prevView;
    private ImageButton playPauseView;
    private ImageButton nextView;
    private ImageButton randView;
    private ImageButton refreshView;
    private boolean shuffle;
    private boolean loop_one;
    private boolean loop;
    private int seconds;

    public static Handler getReadyHandler(){
        return readyHandler;
    }

    public static Handler getRefreshHandler(){
        return refreshHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        try {
            //AdView mAdView = (AdView) findViewById(R.id.adView);
            //AdMob.prepareBannerAd(getApplicationContext(), mAdView, getString(R.string
            // .banner_ad_id));

            AdView adView = (AdView) findViewById(R.id.adView);
            AdMob.prepareBannerAd(getApplicationContext(), adView, getString(R.string.banner_ad_id));

            //(service.getCurrentSong()) getIntent().getSerializableExtra(SI_PARAM);
            shuffle = getIntent().getBooleanExtra(SHUFFLE_STATE, false);
            loop = getIntent().getBooleanExtra(LOOP_STATE, false);
            loop_one = getIntent().getBooleanExtra(LOOP_ONE_STATE, false);
            service = AudioFragment.getMusicService();
            fillValues();
            refreshStates();
            readyHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg){
                    try {
                        setSeekBar(msg.what);
                        playPauseView.setImageResource(R.drawable.ic_pause_white);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                }
            };

            refreshHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg){
                    try {
                        //Bundle data = msg.getData();
                        refreshStates();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                }
            };
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }


    private void fillValues(){
        tvName = (ScrollTextView) findViewById(R.id.tvName);
        //tvName.setSelected(true);
        tvName.setText(service.getCurrentSong().getTitle());
        tvName.startScroll();

        tvArtist = (TextView) findViewById(R.id.tvArtist);
        tvArtist.setText(service.getCurrentSong().getArtist());

        tvAlbum = (TextView) findViewById(R.id.tvAlbum);
        tvAlbum.setText(service.getCurrentSong().getAlbum());

        tvSource = (TextView) findViewById(R.id.tvSource);
        //tvSource.setTypeface(Typeface.createFromAsset(getAssets(), "icomoon.ttf"));
        IconHelper.setSourceIcon(tvSource, service.getCurrentSong().getSourceType());

        ivCover = (ImageView) findViewById(R.id.ivAlbum);
        if (service.getCurrentSong().getThumbnail() != null) {
         /*       File filesDir = getFilesDir();
                String path = filesDir.getAbsolutePath() + "/" + service.getCurrentSong()
                .getThumbnail();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivCover.setImageBitmap(bitmap);*/
            try {
             /*       ImageLoaderTask lazyListImageLoader = ImageUtils.getLazyImageLoaderTask();
                    lazyListImageLoader.loadImage(service.getCurrentSong().getThumbnail(), new ImageLoaderTask
                            .ImageLoadedListener() {
                        @Override
                        public void imageLoaded(Bitmap bitmap) {
                            ivCover.setImageBitmap(bitmap);
                        }
                    });*/

                    /*LazyListImageLoader lazyListImageLoader = ImageUtils.getLazyImageLoaderTask
                            (getApplicationContext
                            ());
                    if (service.getCurrentSong().getSourceType()!= SourceType.ONEDRIVE) {
                        lazyListImageLoader.DisplayImage(service.getCurrentSong().getThumbnail(), ivCover);
                    } else {
                        lazyListImageLoader.DisplayImage(CmpDeviceService.getOneDriveRestClient
                        (service.getCurrentSong()
                                .getAccountId()), service.getCurrentSong().getAccountId(),
                                service.getCurrentSong().getId(),
                                ivCover);
                    }*/

                ImageUtils.setCoverImage(getApplicationContext(), service.getCurrentSong()
                                .getSourceType(),
                        service.getCurrentSong().getThumbnail(), ivCover);
            } catch (Exception e) {
                ivCover.setImageResource(R.drawable.default_cover);
            }
        } else {
            ivCover.setImageResource(R.drawable.default_cover);
        }
        ivCover.setOnTouchListener(new OnSwipeTouchListener(PlaybackActivity.this){
            public void onSwipeTop(){
                //Toast.makeText(PlaybackActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight(){
                //Toast.makeText(PlaybackActivity.this, "right", Toast.LENGTH_SHORT).show();
                service.playPrev();
                timer.pause();
            }

            public void onSwipeLeft(){
                //Toast.makeText(PlaybackActivity.this, "left", Toast.LENGTH_SHORT).show();
                service.playNext(true);
                timer.pause();
            }

            public void onSwipeBottom(){
                //Toast.makeText(PlaybackActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                if (fromUser) {
                    service.seek(progress * 1000);
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
                //service.pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //service.go();
                setTimer((seconds - progress) * 1000);

                if (service.isPng()) {
                    timer.create();
                }
            }
        });
        tvDuration = (TextView) findViewById(R.id.duration);
        setSeekBar(service.getDur());
        int duration = service.getPosn();
        seekBar.setProgress(duration / 1000);
        setTimer((seconds * 1000) - duration);

        prevView = (ImageButton) findViewById(R.id.prev);
        playPauseView = (ImageButton) findViewById(R.id.play);
        nextView = (ImageButton) findViewById(R.id.next);
        randView = (ImageButton) findViewById(R.id.shuffle);
        refreshView = (ImageButton) findViewById(R.id.refresh);

        prevView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                service.playPrev();
                timer.pause();
                refreshValues();
            }
        });
        nextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                service.playNext(true);
                if (timer != null) {
                    timer.pause();
                }
                refreshValues();
            }
        });

        playPauseView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (service.isPng()) {
                    service.pausePlayer();
                    playPauseView.setImageResource(R.drawable.ic_play_white);
                    if (timer != null) {
                        timer.pause();
                    }
                } else {
                    service.go();
                    playPauseView.setImageResource(R.drawable.ic_pause_white);
                    if (timer != null) {
                        if (timer.isPaused()) {
                            timer.resume();
                        } else {
                            timer.create();
                        }
                    }
                }
            }
        });


        randView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (service.isShuffle()) {
                    service.setShuffle(false);
                    randView.setImageResource(ic_shuffle_white);
                } else {
                    service.setShuffle(true);
                    randView.setImageResource(R.drawable.ic_shuffle_green);
                }
            }
        });


        refreshView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (service.isLoop_one()) {
                    service.setLoop_one(false);
                    service.setLoop(true);
                    refreshView.setImageResource(R.drawable.ic_loop_green);

                } else {
                    if (service.isLoop()) {
                        service.setLoop_one(false);
                        service.setLoop(false);
                        refreshView.setImageResource(R.drawable.ic_loop_white);
                    } else {
                        service.setLoop_one(true);
                        service.setLoop(true);
                        refreshView.setImageResource(R.drawable.ic_loop_one);
                    }
                }
            }
        });

        downView = (ImageButton) findViewById(R.id.btnDown);
        downView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    /*Intent playback = new Intent(PlaybackActivity.this, MainActivity.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim
                                    .slide_out_up, R.anim.slide_in_up).toBundle();
                    startActivity(playback, bndlanimation);*/
                finish();

            }
        });

        /*optView = (ImageButton) findViewById(R.id.btnAudioOpt);
        optView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AudioOptionView optView = new AudioOptionView(getApplicationContext(), service
                        .getCurrentSong(), true);
                AlertDialog.Builder builder = optView.getBuilder();
                AlertDialog dialog = builder.create();
                optView.setDialog(dialog);
//                dialog.show();
            }
        });*/

    }

    private void refreshStates(){
        if (service.isLoop_one()) {
            refreshView.setImageResource(R.drawable.ic_loop_one);
        } else if (service.isLoop()) {
            refreshView.setImageResource(R.drawable.ic_loop_green);
        } else {
            refreshView.setImageResource(R.drawable.ic_loop_white);
        }

        if (service.isShuffle()) {
            randView.setImageResource(R.drawable.ic_shuffle_green);
        } else {
            randView.setImageResource(R.drawable.ic_shuffle_white);
        }


        seconds = (int) Math.floor(service.getDur() / 1000);
        int current = service.getPosn();
        seekBar.setMax(seconds);
        seekBar.setProgress((int) Math.floor(current / 1000));
        setTimer((seconds * 1000) - current);

        if (service.isPng()) {
            playPauseView.setImageResource(R.drawable.ic_pause_white);
            if (!timer.isPaused()) {
                timer.create();
            } else {
                timer.resume();
            }
        } else {
            playPauseView.setImageResource(R.drawable.ic_play_white);
            timer.pause();
        }

        refreshValues();
    }

    private void refreshValues(){
        tvName.setText(service.getCurrentSong().getTitle());
        tvAlbum.setText(service.getCurrentSong().getAlbum());
        tvArtist.setText(service.getCurrentSong().getArtist());
        IconHelper.setSourceIcon(tvSource, service.getCurrentSong().getSourceType());
        if (service.getCurrentSong().getThumbnail() != null) {
/*            File filesDir = getFilesDir();
            String path = filesDir.getAbsolutePath() + "/" + service.getCurrentSong().getThumbnail();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivCover.setImageBitmap(bitmap);*/
            try {
     /*           ImageLoaderTask lazyListImageLoader = ImageUtils.getLazyImageLoaderTask();
                lazyListImageLoader.loadImage(service.getCurrentSong().getThumbnail(), new ImageLoaderTask
                        .ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Bitmap bitmap) {
                        ivCover.setImageBitmap(bitmap);
                    }
                });*/
                LazyListImageLoader lazyListImageLoader = ImageUtils.getLazyImageLoaderTask
                        (getApplicationContext
                                ());
            /*    if (service.getCurrentSong().getSourceType() != SourceType.ONEDRIVE) {
                    lazyListImageLoader.DisplayImage(service.getCurrentSong().getThumbnail(),
                    ivCover);
                } else {
                    lazyListImageLoader.DisplayImage(CmpDeviceService.getOneDriveRestClient
                    (service.getCurrentSong()
                            .getAccountId()), service.getCurrentSong().getAccountId(), service
                            .getCurrentSong().getId(), ivCover);
                }*/
                ImageUtils.setCoverImage(getApplicationContext(), service.getCurrentSong().getSourceType(),
                        service.getCurrentSong().getThumbnail(), ivCover);
            } catch (Exception e) {
                ivCover.setImageResource(R.drawable.default_cover);
            }
        } else {
            ivCover.setImageResource(R.drawable.default_cover);
        }
        //setSeekBar(0);
        //tvName.setSelected(true);
    }

    public void setSeekBar(final int duration){

        seekBar.setProgress(0);
        seconds = (duration / 1000);
        seekBar.setMax(seconds);

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimerWithPause(duration, 1000, true){ // adjust the milli seconds here

            public void onTick(long millisUntilFinished){
                tvDuration.setText(DATE_FORMAT.format(new Date(millisUntilFinished)));
                seekBar.setProgress((seekBar.getProgress() + 1));
            }

            public void onFinish(){
            }
        };
        timer.create();
        //tvName.setSelected(true);
    }

    public void setTimer(int duration){
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimerWithPause(duration, 1000, true){ // adjust the milli seconds here

            public void onTick(long millisUntilFinished){
                tvDuration.setText(DATE_FORMAT.format(new Date(millisUntilFinished)));
                seekBar.setProgress((seekBar.getProgress() + 1));
            }

            public void onFinish(){
            }
        };

        timer.create();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fillValues();
    }

}
