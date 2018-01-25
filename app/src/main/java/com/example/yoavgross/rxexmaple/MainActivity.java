package com.example.yoavgross.rxexmaple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_debounce).setOnClickListener(this);
        findViewById(R.id.btn_form).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_form:
                startActivity(new Intent(this, FormActivity.class));
                break;
            case R.id.btn_debounce:
                startActivity(new Intent(this, DebounceActivity.class));
                break;
        }
    }
}
