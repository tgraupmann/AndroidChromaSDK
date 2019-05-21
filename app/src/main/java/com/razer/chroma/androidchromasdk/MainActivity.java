package com.razer.chroma.androidchromasdk;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Spinner mSpinnerAnimation = null;
    private LinearLayout mEmulatorView = null;
    private ArrayList<ArrayList<ImageView>> mEmulator = null;
    private Handler mHandler = null;
    private int mFrameId = 0;
    private AnimationBase mAnimation = null;
    private final String ITEM_1 = "Rainbow ChromaLink";
    private final String ITEM_2 = "Rainbow Headset";
    private final String ITEM_3 = "Rainbow Keyboard";
    private final String ITEM_4 = "Rainbow Mouse";
    private final String ITEM_5 = "Rainbow Mousepad";
    private int mOrientation = 0;



    String[] mAvailableAnimations = new String[]{
            ITEM_1,
            ITEM_2,
            ITEM_3,
            ITEM_4,
            ITEM_5,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinnerAnimation = (Spinner) findViewById(R.id.spinnerAnimation);
        mEmulatorView = (LinearLayout) findViewById(R.id.emulatorView);


        final List<String> animationsList = new ArrayList<>(Arrays.asList(mAvailableAnimations));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, animationsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinnerAnimation.setAdapter(spinnerArrayAdapter);

        mSpinnerAnimation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //Log.d(TAG, mAvailableAnimations[position]);
                showAnimation(mAvailableAnimations[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        detectOrientation();

        mHandler = new Handler();
        tick();
    }

    void detectOrientation() {
        // Checks the orientation of the screen
        mOrientation = getResources().getConfiguration().orientation;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Log.d(TAG, "landscape");
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Log.d(TAG, "portrait");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        detectOrientation();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayEmulator();
            }
        });
    }

    private void displayBorder() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mEmulator == null) {
                    return;
                }

                for (int r = 0; r < mEmulator.size(); ++r) {
                    ArrayList<ImageView> row = mEmulator.get(r);
                    for (int c = 0; c < row.size(); ++c) {
                        ImageView child = row.get(c);
                        child.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void tick() {
        int duration = 1000;
        if (mAnimation != null) {
            ++mFrameId;
            if (mFrameId >= mAnimation.getFrameCount()) {
                mFrameId = 0;
            }
            duration = (int)Math.floor(1000 * mAnimation.getDuration(mFrameId));
            //Log.d(TAG, "FrameId: "+mFrameId+" Duration: "+duration);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAnimation != null) {
                    mAnimation.showFrame(mEmulator, mFrameId);
                }
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, duration);
    }

    private void showAnimation(String item) {

        mFrameId = 0;

        InputStream input = null;
        switch (item) {
            case ITEM_1:
                input = this.getResources().openRawResource(R.raw.animation_rainbow_chroma_link);
                break;
            case ITEM_2:
                input = this.getResources().openRawResource(R.raw.animation_rainbow_headset);
                break;
            case ITEM_3:
                input = this.getResources().openRawResource(R.raw.animation_rainbow_keyboard);
                break;
            case ITEM_4:
                input = this.getResources().openRawResource(R.raw.animation_rainbow_mouse);
                break;
            case ITEM_5:
                input = this.getResources().openRawResource(R.raw.animation_rainbow_mousepad);
                break;
        }

        if (input == null) {
            return;
        }

        AnimationBase animation = ChromaAnimationAPI.OpenAnimation(input);
        if (animation == null) {
            Log.e(TAG, "Animation could not be loaded!");
            return;
        }
        mAnimation = animation;

        displayEmulator();
    }

    void displayEmulator() {
        Log.d(TAG, "displayEmulator");
        mEmulatorView.removeAllViews();
        switch (mAnimation.getDeviceType()) {
            case DE_1D:
            {
                Animation1D animation1D = (Animation1D)mAnimation;

                final int maxRow = 1;
                final int maxLeds = ChromaAnimationAPI.GetMaxLeds(animation1D.getDevice());

                mEmulator = new ArrayList<ArrayList<ImageView>>();
                for (int r = 0; r < maxRow; ++r) {
                    ArrayList<ImageView> row = new ArrayList<ImageView>();
                    for (int c = 0; c < maxLeds; ++c) {
                        row.add(null);
                    }
                    mEmulator.add(row);
                }

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    for (int r = 0; r < maxRow; ++r) {
                        LinearLayout row = new LinearLayout(this);
                        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        row.setLayoutParams(layout);
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        for (int led = 0; led < maxLeds; ++led) {
                            ImageView imageView = new ImageView(this);
                            layout = new LinearLayout.LayoutParams(FILL_PARENT, FILL_PARENT);
                            layout.height = 64;
                            layout.width = 64;
                            layout.leftMargin = 4;
                            layout.rightMargin = 4;
                            layout.topMargin = 4;
                            layout.bottomMargin = 4;
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
                            row.addView(imageView);
                            mEmulator.get(r).set(led, imageView);
                        }
                        mEmulatorView.addView(row);
                    }
                } else {
                    for (int led = 0; led < maxLeds; ++led) {
                        LinearLayout row = new LinearLayout(this);
                        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        row.setLayoutParams(layout);
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        for (int r = maxRow-1; r >= 0; --r) {
                            ImageView imageView = new ImageView(this);
                            layout = new LinearLayout.LayoutParams(FILL_PARENT, FILL_PARENT);
                            layout.height = 64;
                            layout.width = 64;
                            layout.leftMargin = 4;
                            layout.rightMargin = 4;
                            layout.topMargin = 4;
                            layout.bottomMargin = 4;
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
                            row.addView(imageView);
                            mEmulator.get(r).set(led, imageView);
                        }
                        mEmulatorView.addView(row);
                    }
                }
                break;
            }
            case DE_2D:
            {
                Animation2D animation2D = (Animation2D)mAnimation;

                final int maxRow = ChromaAnimationAPI.GetMaxRow(animation2D.getDevice());
                final int maxColumn = ChromaAnimationAPI.GetMaxColumn(animation2D.getDevice());

                mEmulator = new ArrayList<ArrayList<ImageView>>();
                for (int r = 0; r < maxRow; ++r) {
                    ArrayList<ImageView> row = new ArrayList<ImageView>();
                    for (int c = 0; c < maxColumn; ++c) {
                        row.add(null);
                    }
                    mEmulator.add(row);
                }

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    for (int r = 0; r < maxRow; ++r) {
                        LinearLayout row = new LinearLayout(this);
                        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        row.setLayoutParams(layout);
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        for (int c = 0; c < maxColumn; ++c) {
                            ImageView imageView = new ImageView(this);
                            layout = new LinearLayout.LayoutParams(FILL_PARENT, FILL_PARENT);
                            layout.height = 64;
                            layout.width = 64;
                            layout.leftMargin = 4;
                            layout.rightMargin = 4;
                            layout.topMargin = 4;
                            layout.bottomMargin = 4;
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
                            row.addView(imageView);
                            mEmulator.get(r).set(c, imageView);
                        }
                        mEmulatorView.addView(row);
                    }
                } else {
                    for (int c = 0; c < maxColumn; ++c) {
                        LinearLayout row = new LinearLayout(this);
                        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                        row.setLayoutParams(layout);
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        for (int r = maxRow-1; r >= 0; --r) {
                            ImageView imageView = new ImageView(this);
                            layout = new LinearLayout.LayoutParams(FILL_PARENT, FILL_PARENT);
                            layout.height = 64;
                            layout.width = 64;
                            layout.leftMargin = 4;
                            layout.rightMargin = 4;
                            layout.topMargin = 4;
                            layout.bottomMargin = 4;
                            imageView.setLayoutParams(layout);
                            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
                            row.addView(imageView);
                            mEmulator.get(r).set(c, imageView);
                        }
                        mEmulatorView.addView(row);
                    }
                }

                break;
            }
        }

        displayBorder();
    }
}
