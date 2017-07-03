package com.zywuhen.texture.texturedemo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;

/**
 *
 * 项目名称：Texture
 * 类描述：Texture+Camera录制视频
 * 创建人：yqw
 * 创建时间：2017/7/3 11:36
 * 修改人：yqw
 * 修改时间：2017/7/3 11:36
 * 修改备注：
 * Version:  1.0.0
 */
public class RecordCamera extends Activity implements TextureView.SurfaceTextureListener{
    private TextureView mTextureView;
    private Camera mCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        setContentView(mTextureView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(previewSize.width,previewSize.height,Gravity.CENTER));

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();

        }
        mCamera.startPreview();
        mTextureView.setAlpha(1.0f);
        mTextureView.setRotation(45.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
