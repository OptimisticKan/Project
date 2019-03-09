package com.example.rhkdg.sharethetrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SatisfactionActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SatisfactionActivity";
    public static final String EXTRA_POST_KEY = "post_key";

    private Button finaltransactionbtn;
    private Button rejectbtn;
    private DatabaseReference mDataReference;
    private String mPostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mDataReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);

        finaltransactionbtn = findViewById(R.id.finaltransactionbtn);
        finaltransactionbtn.setOnClickListener(new View.OnClickListener() {
            final String postKey = mDataReference.getKey();
            @Override
            public void onClick(View v) {
                // Launch PostDetailActivity
                Intent intent = new Intent(SatisfactionActivity.this, FinaltransactionActivity.class);
                intent.putExtra(SatisfactionActivity.EXTRA_POST_KEY, postKey);
                startActivity(intent);
            }
        });

        rejectbtn = findViewById(R.id.rejectbtn);
        rejectbtn.setOnClickListener(new View.OnClickListener() {
            final String postKey = mDataReference.getKey();
            @Override
            public void onClick(View v) {
                // Launch PostDetailActivity
                Intent intent = new Intent(SatisfactionActivity.this, FinalsatisfactionActivity.class);
                intent.putExtra(SatisfactionActivity.EXTRA_POST_KEY, postKey);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
