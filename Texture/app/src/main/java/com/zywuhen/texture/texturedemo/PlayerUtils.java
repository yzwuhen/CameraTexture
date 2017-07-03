package com.zywuhen.texture.texturedemo;

/**
 *
 * 项目名称：Texture
 * 类描述：
 * 创建人：yqw
 * 创建时间：2017/6/30 17:26
 * 修改人：yqw
 * 修改时间：2017/6/30 17:26
 * 修改备注：
 * Version:  1.0.0
 */
public class PlayerUtils {

    private  CusPlayerFrameLayout mCusPlayerFrameLayout;
    private static PlayerUtils playerUtils;

    public static synchronized PlayerUtils getInstance(){
        if (playerUtils == null){
            playerUtils = new PlayerUtils();
        }
        return playerUtils;
    }

    public CusPlayerFrameLayout getCusPlayerFrameLayout() {
        return mCusPlayerFrameLayout;
    }

    public void setCusPlayerFrameLayout(CusPlayerFrameLayout cusPlayerFrameLayout) {
        mCusPlayerFrameLayout = cusPlayerFrameLayout;
    }

    public PlayerUtils setPlayerUrl(String playerUrl){

        return this;
    }

    public void releasePlayer(){

        if (mCusPlayerFrameLayout!=null){
            mCusPlayerFrameLayout.release();
            mCusPlayerFrameLayout = null;
        }
    }
}
