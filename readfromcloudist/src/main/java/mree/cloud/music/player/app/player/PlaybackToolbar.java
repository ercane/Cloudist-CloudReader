package mree.cloud.music.player.app.player;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mree.cloud.music.player.app.R;
import mree.cloud.music.player.app.act.PlaybackActivity;
import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.services.MusicService;
import mree.cloud.music.player.app.utils.CountDownTimerWithPause;
import mree.cloud.music.player.common.model.SongInfo;
import mree.cloud.music.player.common.ref.SourceType;

import static mree.cloud.music.player.app.R.id.shuffle;

/**
 * Created by eercan on 13.10.2016.
 */
public class PlaybackToolbar {

    public static final String SI_PARAM = "SI_PARAM";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");
    private static CountDownTimerWithPause timer;
    private Context context;
    private MusicService service;
    private View mainView;
    private LinearLayout toolbar;
    //private TextView tvSource;
    private TextView tvDuration;
    //private ImageView ivCover;
    private SeekBar seekBar;
    private ImageButton prevView;
    private ImageButton playPauseView;
    private ImageButton nextView;
    private ImageButton randView;
    private ImageButton refreshView;
    private ImageButton btnUp;
    private int seconds = 0;


    public PlaybackToolbar(Context context, View mainView) {
        this.context = context;
        this.service = AudioFragment.getMusicService();
        this.mainView = mainView;
        init();
    }

    private void init() {
        //toolbar = (LinearLayout) mainView.findViewById(R.id.musicController);
        //toolbar.setVisibility(View.GONE);
        toolbar = (LinearLayout) mainView;
        //tvSource = (TextView) mainView.findViewById(tvSource);
        //tvSource.setTypeface(Typeface.createFromAsset(context.getAssets(), "icomoon.ttf"));

        //ivCover = (ImageView) mainView.findViewById(R.id.ivAlbum);
        btnUp = (ImageButton) mainView.findViewById(R.id.up);
        seekBar = (SeekBar) mainView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    service.seek(progress * 1000);
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //service.pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //service.go();
                setTimer((seconds - progress) * 1000);
                if (service.isPng())
                    timer.create();
            }
        });
        tvDuration = (TextView) mainView.findViewById(R.id.duration);

        prevView = (ImageButton) mainView.findViewById(R.id.prev);
        playPauseView = (ImageButton) mainView.findViewById(R.id.play);
        nextView = (ImageButton) mainView.findViewById(R.id.next);
        randView = (ImageButton) mainView.findViewById(shuffle);
        refreshView = (ImageButton) mainView.findViewById(R.id.refresh);

        prevView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.playPrev();
                timer.pause();
            }
        });
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.playNext(true);
                if (timer != null) {
                    timer.pause();
                }
            }
        });
        playPauseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service.isPng()) {
                    service.pausePlayer();
                    playPauseView.setImageResource(R.drawable.ic_play_white);
                    if (timer != null)
                        timer.pause();
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

        randView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service.isShuffle()) {
                    service.setShuffle(false);
                    randView.setImageResource(R.drawable.ic_shuffle_white);
                } else {
                    service.setShuffle(true);
                    randView.setImageResource(R.drawable.ic_shuffle_green);
                }
            }
        });

        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEnabled(boolean state) {
        if (toolbar != null) {
            seekBar.setEnabled(state);
            prevView.setEnabled(state);
            playPauseView.setEnabled(state);
            nextView.setEnabled(state);
            randView.setEnabled(state);
            refreshView.setEnabled(state);
        }
    }

    public void setCover(SourceType sourceType, String thumbnail) {
       /* if (sourceType != null || tvSource != null) {
            IconHelper.setSourceIcon(tvSource, sourceType);
        }

        if (ivCover != null) {
            if (thumbnail != null) {
                *//*File filesDir = context.getFilesDir();
                String path = filesDir.getAbsolutePath() + "/" + thumbnail;
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivCover.setImageBitmap(bitmap);*//*
                try {
                    *//*ImageLoaderTask lazyListImageLoader = ImageUtils.getLazyImageLoaderTask();
                    lazyListImageLoader.loadImage(thumbnail, new ImageLoaderTask
                    .ImageLoadedListener() {
                        @Override
                        public void imageLoaded(Bitmap bitmap) {
                            ivCover.setImageBitmap(bitmap);
                        }
                    });*//*
*//*                    LazyListImageLoader lazyListImageLoader = ImageUtils.getLazyImageLoaderTask
                            (context);
                    lazyListImageLoader.DisplayImage(thumbnail, ivCover);*//*
                    ImageUtils.setCoverImage(context, sourceType,
                            thumbnail, ivCover);
                } catch (Exception e) {
                    ivCover.setImageResource(R.drawable.default_cover);
                }
            } else {
                ivCover.setImageResource(R.drawable.default_cover);
            }

        }*/
    }

    public void setSeekBar(final int duration) {

        seekBar.setProgress(0);
        seconds = (duration / 1000);
        seekBar.setMax(seconds);

        if (timer != null)
            timer.cancel();

        timer = new CountDownTimerWithPause(duration, 1000, true) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvDuration.setText(DATE_FORMAT.format(new Date(millisUntilFinished)));
                seekBar.setProgress((seekBar.getProgress() + 1));
            }

            public void onFinish() {
            }
        };
        timer.create();
        playPauseView.setImageResource(R.drawable.ic_pause_white);
    }

    public void setTimer(int duration) {
        if (timer != null)
            timer.cancel();

        timer = new CountDownTimerWithPause(duration, 1000, true) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvDuration.setText(DATE_FORMAT.format(new Date(millisUntilFinished)));
                seekBar.setProgress((seekBar.getProgress() + 1));
            }

            public void onFinish() {
            }
        };

    }

    public void show() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            refreshStates();
        }
    }

    private void refreshStates() {
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
            timer.create();
        } else {
            playPauseView.setImageResource(R.drawable.ic_play_white);
        }
    }

    public void hide() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    public void pause() {
        if (toolbar != null) {
            if (timer != null && playPauseView != null) {
                timer.pause();
                playPauseView.setImageResource(R.drawable.ic_play_white);
            }
        }
    }

    public void refreshListener(final SongInfo si) {
        if (toolbar != null && btnUp != null) {
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent playback = new Intent(context, PlaybackActivity.class);
                    playback.putExtra(SI_PARAM, si);
                    playback.putExtra(PlaybackActivity.SHUFFLE_STATE, service.isShuffle());
                    playback.putExtra(PlaybackActivity.LOOP_ONE_STATE, service.isLoop_one());
                    playback.putExtra(PlaybackActivity.LOOP_STATE, service.isLoop());
                    playback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_up, R
                                    .anim.slide_out_up).toBundle();
                    context.startActivity(playback, bndlanimation);
                }
            });
        }
    }
}
