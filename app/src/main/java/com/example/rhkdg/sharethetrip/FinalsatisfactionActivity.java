package com.example.rhkdg.sharethetrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalsatisfactionActivity extends AppCompatActivity {

    private Button rejectbtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalsatisfaction);

        rejectbtn1 = findViewById(R.id.rejectbtn1);
        rejectbtn1.setOnClickListener(new View.OnClickListener() {
            //final String postKey = mDataReference.getKey();
            @Override
            public void onClick(View v) {
                // Launch PostDetailActivity
                Intent intent = new Intent(FinalsatisfactionActivity.this, AdmintransactionActivity.class);
                //intent.putExtra(SatisfactionActivity.EXTRA_POST_KEY, postKey);
                startActivity(intent);
            }
        });
    }
}
