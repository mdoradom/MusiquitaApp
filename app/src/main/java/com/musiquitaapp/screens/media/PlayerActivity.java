package com.musiquitaapp.screens.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityPlayerBinding;
import com.musiquitaapp.databinding.FragmentBottomSheetDialogBinding;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.services.BackgroundAudioService;

public class PlayerActivity extends AppCompatActivity {

    private int alpha = 200;

    private ActivityPlayerBinding binding;
    private FragmentBottomSheetDialogBinding binding2;
    private boolean isChecked = false;
    private boolean isPlaying = true;
    private boolean repeat = false;
    private AnimationDrawable animation;
    private YouTubeVideo youTubeVideo;
    private int oldTextColor;
    private Intent serviceIntent;
    private Palette.Swatch vibrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        binding2 = FragmentBottomSheetDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        YouTubeVideo song = startService();

        //binding.progressBar.setMax(song.getDuration());
        binding.progressBar.setMax(1000);
        playSong(song);

        binding.loopButton.setOnClickListener(v -> {
            if (!repeat){
                repeat = true;
                serviceIntent.setAction(BackgroundAudioService.ACTION_REPEAT_ONE);
                startService(serviceIntent);
                rotateAnimation(binding.loopButton);
                binding.loopButton.setImageResource(R.drawable.ic_baseline_repeat_one_24);
            }else{
                repeat = false;
                serviceIntent.setAction(BackgroundAudioService.ACTION_REPEAT_NONE);
                startService(serviceIntent);
                rotateAnimation(binding.loopButton);
                binding.loopButton.setImageResource(R.drawable.ic_baseline_loop_24);

            }
        });

        binding.playButton.setOnClickListener(v -> {


            if (isPlaying) {
                isPlaying = false;
                serviceIntent.setAction(BackgroundAudioService.ACTION_PAUSE);
                startService(serviceIntent);
                rotateAnimation(binding.playButton);
                binding.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            } else {
                serviceIntent.setAction(BackgroundAudioService.ACTION_RESUME);
                startService(serviceIntent);
                isPlaying = true;
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
            popupMenu.setOnMenuItemClickListener(item -> {
                System.out.println("You Clicked " + item.getTitle());
                Toast.makeText(PlayerActivity.this, "You Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
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

        binding.bgSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadAnimation();
                // change background color
                binding.mainContainer.setBackgroundColor(0);

                // change text color
                binding.songTitle.setTextColor(oldTextColor);
                binding.artistName.setTextColor(oldTextColor);
                binding.playingText.setTextColor(oldTextColor);

                // change buttons color
                binding.playButton.setColorFilter(0);
                binding.nextButton.setColorFilter(0);
                binding.previousButton.setColorFilter(0);
                binding.shuffleButton.setColorFilter(0);
                binding.loopButton.setColorFilter(0);
                binding.queueButton.setColorFilter(0);
                binding.favIcon.setColorFilter(0);
                binding.backArrowIcon.setColorFilter(0);
                binding.moreIcon.setColorFilter(0);

                // change nav bar color
                getWindow().setNavigationBarColor(0);

                // change top bar color
                getWindow().setStatusBarColor(0);

                // change seekbar color
                binding.progressBar.getThumb().setColorFilter(0, PorterDuff.Mode.SRC_IN);

                /*
                // TODO WIP 2 cambiar color dinamico del bottom sheet dialog menu
                // change bottomSheetDialog color
                setContentView(binding2.getRoot());
                binding2.mainContainer.setBackgroundColor(Color.BLACK);
                binding2.titleQueue.setTextColor(oldTextColor);
                setContentView(binding.getRoot());
                 */

            } else {
                loadImage(youTubeVideo.getThumbnailURL(), true);
            }
        });

        binding.queueButton.setOnClickListener(v -> showBottomSheetDialog());

    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_sheet_dialog);

        RelativeLayout relativeLayout = bottomSheetDialog.findViewById(R.id.mainContainer);
        TextView textView = bottomSheetDialog.findViewById(R.id.titleQueue);

        /*
        // TODO WIP 2 cambiar color dinamico del bottom sheet dialog menu
        if (vibrant != null) {
            relativeLayout.setBackgroundColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), 255));
            textView.setTextColor(vibrant.getBodyTextColor());
        }
        */

        bottomSheetDialog.show();
    }

    private void rotateAnimation(ImageView imageView) {
        RotateAnimation anim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        imageView.startAnimation(anim);
    }

    private YouTubeVideo startService() {
        Bundle bundle = getIntent().getExtras();

        youTubeVideo = (YouTubeVideo) bundle.getSerializable("song");

        System.out.println(youTubeVideo.getThumbnailURL());

        loadImage(youTubeVideo.getThumbnailURL(), true);

        binding.songTitle.setText(youTubeVideo.getTitle());
        binding.songTitle.setSelected(true);
        binding.songLenght.setText(youTubeVideo.getDuration());

        return youTubeVideo;
    }

    private void loadImage(String url, Boolean changeBackground) {
        Glide.with(PlayerActivity.this)
                .asBitmap()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        isPlaying = false;
                        // TODO fails to load song thubnail revisar
                        System.out.println("Failed to load Song Image, replacing with placeholder animation");
                        loadAnimation();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        isPlaying = true;
                        Palette palette = Palette.from(resource).generate();
                        vibrant = palette.getVibrantSwatch();

                        if (vibrant != null) {
                            oldTextColor = binding.playingText.getCurrentTextColor();

                            // change background color
                            binding.mainContainer.setBackgroundColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), alpha));

                            // change text color
                            binding.songTitle.setTextColor(vibrant.getBodyTextColor());
                            binding.artistName.setTextColor(vibrant.getBodyTextColor());
                            binding.playingText.setTextColor(vibrant.getBodyTextColor());

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
                            getWindow().setNavigationBarColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), alpha));

                            // change top bar color
                            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(vibrant.getRgb(), alpha));

                            // change seekbar color
                            binding.progressBar.getThumb().setColorFilter(vibrant.getBodyTextColor(), PorterDuff.Mode.SRC_IN);

                            // change image resource
                            if (changeBackground) {
                                binding.songImage.setImageBitmap(resource);
                            }

                            // change bottomSheetDialog color
                            //changeColorsDynamically(vibrant);
                        }

                        return true;
                    }
                })
                .load(url)
                .into(binding.songImage);
    }

    private void initializeSeekBar(YouTubeVideo song) {
        binding.progressBar.setMax(1/*song.getDuration()*/); // necesito int (es double)
    }

    private void playSong(YouTubeVideo song) {
        serviceIntent = new Intent(getApplicationContext(), BackgroundAudioService.class);

        serviceIntent.setAction(BackgroundAudioService.ACTION_PLAY);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE, ItemType.YOUTUBE_MEDIA_TYPE_VIDEO);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE_VIDEO, song);

        getApplicationContext().startService(serviceIntent);
    }

    private void loadAnimation() {
        animation = new AnimationDrawable();
        loadFrames();
        animation.setOneShot(false);
        binding.songImage.setImageDrawable(animation);
        animation.start();
    }

    private void loadFrames() {
        int randomNum = 1 + (int)(Math.random() * 5);
        String sImage;
        for (int i = 1; i <= 300; i++) {
            sImage = "animation" + randomNum + "frame";
            animation.addFrame(
                    ResourcesCompat.getDrawable(
                            getResources(), getResources().getIdentifier(
                                    sImage + i , "drawable", getPackageName()),
                            null),
                    50);
        }
    }
}