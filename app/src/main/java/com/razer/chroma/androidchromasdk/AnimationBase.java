package com.razer.chroma.androidchromasdk;

import android.widget.ImageView;

import java.util.ArrayList;

public abstract class AnimationBase {
    public abstract EChromaSDKDeviceTypeEnum getDeviceType();
    public abstract int getFrameCount();
    public abstract float getDuration(int frameId);
    public abstract void showFrame(ArrayList<ArrayList<ImageView>> emulator, int frameId);
}
