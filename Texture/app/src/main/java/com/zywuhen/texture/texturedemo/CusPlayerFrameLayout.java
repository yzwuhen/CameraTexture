package com.zywuhen.texture.texturedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 *
 * 项目名称：Texture
 * 类描述：
 * 创建人：yqw
 * 创建时间：2017/6/26 17:13
 * 修改人：yqw
 * 修改时间：2017/6/26 17:13
 * 修改备注：
 * Version:  1.0.0
 */
public class CusPlayerFrameLayout extends FrameLayout {

    private Context mContext;
    private FrameLayout mFrameLayout;
    private MediaController mMediaController;


    private TextureView mTextureView;
    private MediaPlayer mMediaPlayer;
    private ImageView mImageCover;

    private boolean IS_PLAYER;

    private String mUrl;
    private String mCoverUrl;
    private Bitmap mBitmap;

    public CusPlayerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CusPlayerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CusPlayerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext =context;
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mFrameLayout,layoutParams);
    }

    public void addContriller(MediaController mediaController){
        this.mMediaController =mediaController;
        try {
            mFrameLayout.removeView(mMediaController);
        }catch (Exception e){

        }
        mMediaController.setAnchorView(this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFrameLayout.addView(mediaController,layoutParams);
    }

    //这个方法是最后调用的，按照用户习惯 start（的时候就表示播放了，实际是刚初始化
    public void start(){
        initMediaPlayer();
        initTextureView();
        addTextureView();

    }

    //设置播放视频的Url
    public void setUrl(String url){
        this.mUrl = url;
    }

    //设置封面Url
    public void setCoverUrl(String coverUrl){
        this.mCoverUrl =coverUrl;
    }


    //添加封面
    public void addCover() {

        mImageCover = new ImageView(mContext);
        try {
            mFrameLayout.removeView(mImageCover);
        }catch (Exception e){

        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mImageCover.setLayoutParams(layoutParams);
        mImageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mFrameLayout.addView(mImageCover,layoutParams);

        //设置封面照  如果没有即默认取视频的第一帧
        if (TextUtils.isEmpty(mCoverUrl)){
            if (mBitmap == null){
                mBitmap = getNetVideoBitmap(mUrl);
            }
            mImageCover.setImageBitmap(mBitmap);
        }else {
            Glide.with(mContext).load(mCoverUrl).into(mImageCover);
        }
    }


    //获取视频第一帧
    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    private void addTextureView() {
        try {
            mFrameLayout.removeView(mTextureView);
        }catch (Exception e){

        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //保证TextureView是第一个View
        mFrameLayout.addView(mTextureView,0,layoutParams);

    }

    private void initTextureView() {
        if (mTextureView == null){
            mTextureView = new TextureView(mContext);
            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

                    Log.v("sssssssssss","ssssssssssssTextUre准备好了");
                    openMedioPlayer(surface);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });
        }
    }

    private void openMedioPlayer(SurfaceTexture surface) {

        if (TextUtils.isEmpty(mUrl)){
            Toast.makeText(getContext(),"无效的地址",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
          //  String path = "https://wdl.wallstreetcn.com/41aae4d2-390a-48ff-9230-ee865552e72d";
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.setSurface(new Surface(surface));
            //异步播放==》指代medioplayer 进入准备状态，但并未播放
            mMediaPlayer.prepareAsync();
        }catch (Exception e){

        }

    }

    //这里统一设置监听，后续如果有需要再分开处理
    private void initMediaPlayer() {

        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }


    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v("ssssssssssssssssss","sssssssssssssss完成后启动");
            //准备工作完成后 这里播放
            mp.start();
        }
    };

    MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    };
    MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //还可以直接  mp.setLooping(true);无限循环 ==》就不需要在这里播放完后调用start()了，
            mp.start();
        }
    };
    MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            //我记得遇上错误的时候返回true比较好，遇上不可加载的视频的时候不会蹦=  =好像是这样的。具体的忘了。。
            return true;
        }
    };

    MediaPlayer.OnInfoListener mOnInfoListener =new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {

          //  Log.v("sssssssssssss","ssssssssssssssssss监听"+what);
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END||what ==  MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                if (!IS_PLAYER){
                    try {
                        mFrameLayout.removeView(mImageCover);
                        IS_PLAYER = true;
                    }catch (Exception e){

                    }

                }
            }
            return true;
        }
    };

    MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener =  new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //    Log.v("ssssssssssssssssss","sssssssssssssss进度"+percent);
        }
    };

    public void release() {
        if (mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
        }

    }
}
