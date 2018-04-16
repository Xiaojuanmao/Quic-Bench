package com.smilehacker.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.smilehacker.quicdroid.QUICDroid;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QUICDroid.INSTANCE.init(this);
    }
}
