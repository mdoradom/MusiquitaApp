package com.musiquitaapp.screens.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.musiquitaapp.R;
import com.musiquitaapp.adapters.SearchAdapter;
import com.musiquitaapp.databinding.ActivityPlayerBinding;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.services.BackgroundAudioService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TransferQueue;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private Handler handler = new Handler();
    private boolean isChecked = false;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        YouTubeVideo song = startService();

        //binding.progressBar.setMax(song.getDuration());
        binding.progressBar.setMax(1000);

        binding.playButton.setOnClickListener(v -> {
            PlayerActivity.this.playSong(song);
            if (isPlaying) {
                rotateAnimation(binding.playButton);
                binding.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            } else {
                binding.playButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            }
        });

        binding.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.progressBar.setProgress(0 /*current time player*/);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.backArrowIcon.setOnClickListener(v -> onBackPressed());

        binding.moreIcon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(PlayerActivity.this, binding.moreIcon);
            popupMenu.getMenuInflater().inflate(R.menu.player_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    System.out.println("You Clicked " + item.getTitle());
                    Toast.makeText(PlayerActivity.this, "You Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            popupMenu.show();
        });

        binding.favIcon.setOnClickListener(v -> {
            if (isChecked) {
                binding.favIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                isChecked = false;
            } else {
                binding.favIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
                isChecked = true;
            }
        });

    }

    private void rotateAnimation(ImageView imageView) {
        RotateAnimation anim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        imageView.startAnimation(anim);
    }

    private YouTubeVideo startService() {
        Bundle bundle = getIntent().getExtras();

        YouTubeVideo youTubeVideo = (YouTubeVideo) bundle.getSerializable("song");

        Glide.with(PlayerActivity.this)
                .asBitmap()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        isPlaying = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        isPlaying = true;
                        Palette palette = Palette.from(resource).generate();
                        Palette.Swatch vibrant = palette.getVibrantSwatch();

                        if (vibrant != null) {
                            // change background color
                            binding.mainContainer.setBackgroundColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), 150));

                            // change text color
                            binding.songTitle.setTextColor(vibrant.getBodyTextColor());
                            binding.artistName.setTextColor(vibrant.getBodyTextColor());
                            binding.playingText.setTextColor(vibrant.getBodyTextColor());
                            binding.lyricsButton.setTextColor(vibrant.getBodyTextColor());

                            // change buttons color
                            binding.playButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.nextButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.previousButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.shuffleButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.loopButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.queueButton.setColorFilter(vibrant.getBodyTextColor());
                            binding.favIcon.setColorFilter(vibrant.getBodyTextColor());
                            binding.backArrowIcon.setColorFilter(vibrant.getBodyTextColor());
                            binding.moreIcon.setColorFilter(vibrant.getBodyTextColor());

                            // change nav bar color
                            getWindow().setNavigationBarColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), 150));

                            // change seekbar color
                            binding.progressBar.getThumb().setColorFilter(vibrant.getBodyTextColor(), PorterDuff.Mode.SRC_IN);

                            // change image resource
                            binding.songImage.setImageBitmap(resource);
                        }

                        return true;
                    }
                })
                .load(youTubeVideo.getThumbnailURL())
                .into(binding.songImage);

        binding.songTitle.setText(youTubeVideo.getTitle());
        binding.songTitle.setSelected(true);
        binding.songLenght.setText(youTubeVideo.getDuration());

        return youTubeVideo;
    }

    private void initializeSeekBar(YouTubeVideo song) {
        binding.progressBar.setMax(1/*song.getDuration()*/); // necesito int (es double)
    }

    private void playSong(YouTubeVideo song) {
        Intent serviceIntent = new Intent(getApplicationContext(), BackgroundAudioService.class);

        serviceIntent.setAction(BackgroundAudioService.ACTION_PLAY);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE, ItemType.YOUTUBE_MEDIA_TYPE_VIDEO);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE_VIDEO, song);

        getApplicationContext().startService(serviceIntent);
    }
}