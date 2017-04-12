package com.peos.springselectviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peos.springselect.SpringSelectListener;
import com.peos.springselect.SpringSelectView;

public class MainActivity extends AppCompatActivity {

    private SpringSelectView select1;
    private SpringSelectView select2;
    private SpringSelectView select3;

    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select1 = (SpringSelectView)findViewById(R.id.view_select_1);
        select2 = (SpringSelectView)findViewById(R.id.view_select_2);
        select3 = (SpringSelectView)findViewById(R.id.view_select_3);

        textInfo = (TextView)findViewById(R.id.text_info);

        select1.setSpringSelectListener(new SpringSelectListener() {
            @Override
            public void onSpringSelectClick(boolean checked) {
                textInfo.setText("select1 :" + checked);
            }
        });

        select2.setSpringSelectListener(new SpringSelectListener() {
            @Override
            public void onSpringSelectClick(boolean checked) {
                textInfo.setText("select2 :" + checked);
            }
        });

        select3.setSpringSelectListener(new SpringSelectListener() {
            @Override
            public void onSpringSelectClick(boolean checked) {
                textInfo.setText("select3 :" + checked);
            }
        });

        ((Button)findViewById(R.id.btn_test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }
}
