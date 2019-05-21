package com.razer.chroma.androidchromasdk;

import android.graphics.Color;
import android.widget.ImageView;

import java.util.ArrayList;

public class Animation1D extends AnimationBase {
    public EChromaSDKDeviceTypeEnum getDeviceType(){
        return EChromaSDKDeviceTypeEnum.DE_1D;
    }
    public void setDevice(EChromaSDKDevice1DEnum device) {
        mDevice = device;
    }
    public EChromaSDKDevice1DEnum getDevice() {
        return mDevice;
    }
    public ArrayList<FChromaSDKColorFrame1D> getFrames() {
        return mFrames;
    }
    public int getFrameCount()
    {
        return mFrames.size();
    }
    public float getDuration(int frameId) {
        if (frameId >= 0 && frameId < mFrames.size()) {
            FChromaSDKColorFrame1D frame = mFrames.get(frameId);
            return frame.getDuration();
        }
        return 0.033f;
    }
    public void showFrame(ArrayList<ArrayList<ImageView>> emulator, int frameId) {
        if (frameId < 0 || frameId >= mFrames.size()) {
            return;
        }
        final int maxLeds = ChromaAnimationAPI.GetMaxLeds(getDevice());
        FChromaSDKColorFrame1D frame = mFrames.get(frameId);
        int[] colors = frame.getColors();
        for (int led = 0; led < maxLeds; ++led) {
            ImageView imageView = emulator.get(0).get(led);
            int color = colors[led];
            int red = ChromaAnimationAPI.GetRed(color);
            int green = ChromaAnimationAPI.GetGreen(color);
            int blue = ChromaAnimationAPI.GetBlue(color);
            //Log.d(TAG, "red: "+red+" green: "+green+" blue: "+blue);
            imageView.setBackgroundColor(Color.rgb(red, green, blue));
        }
    }
    private EChromaSDKDevice1DEnum mDevice = EChromaSDKDevice1DEnum.DE_ChromaLink;
    private ArrayList<FChromaSDKColorFrame1D> mFrames = new ArrayList<FChromaSDKColorFrame1D>();
}
