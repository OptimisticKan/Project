package com.example.rhkdg.sharethetrip;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MenuguideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuguide);

        final TextView button2 = (TextView) findViewById(R.id.buttong);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buttonIntent = new Intent(MenuguideActivity.this, VIewguideActivity.class);
                MenuguideActivity.this.startActivity(buttonIntent);
            }
        });
        final TextView button3 = (TextView) findViewById(R.id.buttong2);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buttonIntent = new Intent(MenuguideActivity.this, VIewguideActivity.class);
                MenuguideActivity.this.startActivity(buttonIntent);
            }
        });
        final TextView button4 = (TextView) findViewById(R.id.buttong3);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buttonIntent = new Intent(MenuguideActivity.this, VIewguideActivity.class);
                MenuguideActivity.this.startActivity(buttonIntent);
            }
        });
        final TextView button5 = (TextView) findViewById(R.id.buttong4);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buttonIntent = new Intent(MenuguideActivity.this, VIewguideActivity.class);
                MenuguideActivity.this.startActivity(buttonIntent);
            }
        });
    }
}
