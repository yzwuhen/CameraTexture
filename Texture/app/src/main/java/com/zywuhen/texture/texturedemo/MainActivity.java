package com.zywuhen.texture.texturedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CusPlayerFrameLayout mCusPlayerFrameLayout;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCusPlayerFrameLayout = (CusPlayerFrameLayout) findViewById(R.id.cus_player);
        mCusPlayerFrameLayout.setUrl("https://wdl.wallstreetcn.com/41aae4d2-390a-48ff-9230-ee865552e72d");
        mCusPlayerFrameLayout.start();

        mTextView = (TextView) findViewById(R.id.textView);

        mTextView.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCusPlayerFrameLayout!=null){
            mCusPlayerFrameLayout.release();
            mCusPlayerFrameLayout= null;
        }
    }

    @Override
    public void onClick(View v) {
        Intent mintent = new Intent(this,RecordCamera.class);
        startActivity(mintent);
    }
}
