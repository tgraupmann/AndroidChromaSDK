package com.razer.chroma.androidchromasdk;

import android.graphics.Color;
import android.widget.ImageView;

import java.util.ArrayList;

public class Animation2D extends AnimationBase {
    public EChromaSDKDeviceTypeEnum getDeviceType(){
        return EChromaSDKDeviceTypeEnum.DE_2D;
    }
    public void setDevice(EChromaSDKDevice2DEnum device) {
        mDevice = device;
    }
    public EChromaSDKDevice2DEnum getDevice() {
        return mDevice;
    }
    public ArrayList<FChromaSDKColorFrame2D> getFrames() {
        return mFrames;
    }
    public int getFrameCount()
    {
        return mFrames.size();
    }
    public float getDuration(int frameId) {
        if (frameId >= 0 && frameId < mFrames.size()) {
            FChromaSDKColorFrame2D frame = mFrames.get(frameId);
            return frame.getDuration();
        }
        return 0.033f;
    }
    public void showFrame(ArrayList<ArrayList<ImageView>> emulator, int frameId) {
        if (frameId < 0 || frameId >= mFrames.size()) {
            return;
        }
        final int maxRow = ChromaAnimationAPI.GetMaxRow(getDevice());
        final int maxColumn = ChromaAnimationAPI.GetMaxColumn(getDevice());
        FChromaSDKColorFrame2D frame = mFrames.get(frameId);
        int[][] colors = frame.getColors();
        for (int r = 0; r < maxRow; ++r) {
            int[] row = colors[r];
            for (int c = 0; c < maxColumn; ++c) {
                ImageView imageView = emulator.get(r).get(c);
                int color = row[c];
                int red = ChromaAnimationAPI.GetRed(color);
                int green = ChromaAnimationAPI.GetGreen(color);
                int blue = ChromaAnimationAPI.GetBlue(color);
                //Log.d(TAG, "red: "+red+" green: "+green+" blue: "+blue);
                imageView.setBackgroundColor(Color.rgb(red, green, blue));
            }
        }
    }
    private EChromaSDKDevice2DEnum mDevice = EChromaSDKDevice2DEnum.DE_Keyboard;
    private ArrayList<FChromaSDKColorFrame2D> mFrames = new ArrayList<FChromaSDKColorFrame2D>();
}
