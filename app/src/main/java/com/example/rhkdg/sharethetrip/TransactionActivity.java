package com.example.rhkdg.sharethetrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdg.sharethetrip.Model.Web3JService;
import com.example.rhkdg.sharethetrip.models.Post;
import com.example.rhkdg.sharethetrip.utils.KeyStoreUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransactionActivity extends BaseActivity implements View.OnClickListener {

    //Take profile image
    Bitmap bitmap;

    //Take data from database
    private static final String TAG = "TransactionActivity";
    public static final String EXTRA_POST_KEY = "post_key";
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private DatabaseReference mDataReference;
    private String mPostKey;
    //basic
    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mAddressField;
    private TextView metGasPrice1;
    private TextView metGasLimit1;
    private TextView metNonce1;
    private TextView mescrowaddress;
    private TextView mtvMgs1;
    //edittext
    private TextView mNationView;
    private TextView mDateView;
    private TextView mPriceView;
    private TextView mAddressView;
    //Button
    private Button escrowmanagerbtn;
    private Button satisfactionbtn;

    private TextView textviewHtmlDocument1;
    private String htmlContentInStringFormat1;
    private TextView address;
    private String address1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Take data
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mDataReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);

        // Initialize Views
        mAuthorView = findViewById(R.id.taauthor);
        mTitleView = findViewById(R.id.taTitle);
        mAddressField = findViewById(R.id.postAddress_a);

        //transaction
        metGasPrice1 = findViewById(R.id.et_gas_priceg1);
        metGasLimit1 = findViewById(R.id.et_gas_limit1);
        metNonce1 = findViewById(R.id.et_nonce1);
        mescrowaddress = findViewById(R.id.escrowaddress);
        mtvMgs1 = findViewById(R.id.tv_mgs1);
        // Post_detail edittext
        mNationView = findViewById(R.id.postNation_a);
        mDateView = findViewById(R.id.postDate_a);
        mPriceView = findViewById(R.id.postPrice_a);
        mAddressView = findViewById(R.id.postAddress_b);

        textviewHtmlDocument1 = (TextView)findViewById(R.id.checkvalue);

        //Button
        escrowmanagerbtn = findViewById(R.id.escrowmanagerbtn);
        escrowmanagerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTrasaction();
            }
        });

        satisfactionbtn = findViewById(R.id.satisfactionbtn);
        satisfactionbtn.setOnClickListener(new View.OnClickListener() {
            final String postKey = mDataReference.getKey();
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(TransactionActivity.this, SatisfactionActivity.class);
                Intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                startActivity(Intent);
            }
        });

        final Button btn_select_from1 = (Button) findViewById(R.id.btn_select_from1);
        btn_select_from1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(TransactionActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        metGasPrice1.setText("180000");
        metGasLimit1.setText("900000");
        metNonce1.setText("3");
        mescrowaddress.setText("93ecdf68466f305cefe98a2edb3d3645ca04d42c");

        mAddressField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    getTansactionNonce(s.toString());
            }
        });

        Button htmlTitleButton1 = (Button)findViewById(R.id.checkvaluebtn);
        htmlTitleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceAsyncTask jsoupAsyncTask1 = new BalanceAsyncTask();
                jsoupAsyncTask1.execute();

            }
        });
    }

    public void onStart() {
        super.onStart();

        //Take Information
        ImageView imageView_p;
        TextView textView_e;

        //Take database Infomation
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);

                // Post_detail edittext
                mNationView.setText(post.nation);
                mDateView.setText(post.date);
                mPriceView.setText(post.price);
                mAddressView.setText(post.address);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(TransactionActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }

        };

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String email = profile.getEmail();
                textView_e = (TextView) findViewById(R.id.profileemail_a);
                textView_e.setText(email);
                imageView_p = findViewById(R.id.profileimage_a);
                Thread mThread= new Thread(){

                    @Override

                    public void run() {
                        try{
                            URL url = new URL(user.getPhotoUrl().toString());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                        } catch (MalformedURLException ee) {
                            ee.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                };
                mThread.start();
                try{
                    mThread.join();
                    imageView_p.setImageBitmap(bitmap);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        //return true;
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]
        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddressField.setText(data.getStringExtra("address"));
        address1 = "0x"+data.getStringExtra("address");
    }

    @Override
    public void onClick(View v) {

    }

    private void sendTrasaction() {

        String from = mAddressField.getText().toString();
        String to = mescrowaddress.getText().toString();
        String value = mPriceView.getText().toString();
        String gasPrice = metGasPrice1.getText().toString();
        String gasLimit = metGasLimit1.getText().toString();
        String nonce = metNonce1.getText().toString();
        Observable
                .create((ObservableOnSubscribe<EthSendTransaction>) e -> {
                    Web3j web3j = Web3JService.getInstance();

                    String hexValue = KeyStoreUtils.signedTransactionData(from, to, nonce, gasPrice, gasLimit, value);
                    EthSendTransaction send = web3j.ethSendRawTransaction(hexValue).send();
                    e.onNext(send);
                    e.onComplete();

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respon -> {
                    String result = respon.getResult();
                    if (respon.getError() == null) {
                        sendTrasactionSucceed(result);

                    } else {
                        mtvMgs1.setText(respon.getError().getMessage() + "");
                        Log.e("transaction", respon.getError().getMessage() + "");
                    }


                }, throwable -> throwable.printStackTrace());

    }

    private void sendTrasactionSucceed(String result) {
        Log.e("transaction", result + "");
        mtvMgs1.setText(result + "");
        Toast.makeText(this, "발송 성공", Toast.LENGTH_SHORT).show();
        mAddressField.setText("");
        mescrowaddress.setText("");
        mPriceView.setText("");
    }


    public void getTansactionNonce(String address) {
        Observable.create((ObservableOnSubscribe<EthGetTransactionCount>) e -> {
            EthGetTransactionCount count = Web3JService.getInstance().ethGetTransactionCount("0x" + address, () -> "latest").send();
            if (count.getError() == null) {
                e.onNext(count);
                e.onComplete();
            } else {
                e.onError(new Throwable(count.getError().getMessage()));
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ethGetTransactionCount -> {
                    BigInteger bigInteger = Numeric.decodeQuantity(ethGetTransactionCount.getResult());
                    metNonce1.setText(bigInteger.toString());
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    private class BalanceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlPageUrl = "https://api-ropsten.etherscan.io/api?module=account&action=balance&address="+address1+"&tag=latest&apikey=6TKJDAXV1239MCA6ZRQNUSVPCNMIG1B93C";
                Document doc = Jsoup.connect(htmlPageUrl).ignoreContentType(true).get();
                Elements links = doc.select("body");

                for (Element link : links) {
                    htmlContentInStringFormat1 += (link.attr("pre#text")
                            + "("+link.text().trim() + ")\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String result_wei = htmlContentInStringFormat1.substring(44,htmlContentInStringFormat1.length()-4);
            String result_1 = htmlContentInStringFormat1.substring(44,htmlContentInStringFormat1.length()-22);
            String result_2 = htmlContentInStringFormat1.substring(45,htmlContentInStringFormat1.length()-20);
            textviewHtmlDocument1.setText(result_1+"."+result_2+ " Eth\n" +" ( Wei : "+result_wei+" )");
        }
    }
}
