package com.example.rhkdg.sharethetrip;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
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
import java.math.BigInteger;

public class ChargeethActivity extends AppCompatActivity implements View.OnClickListener {

    //Take data from database
    private TextView mAddressView;
    private TextView mescrowaddress;
    private TextView metGasPrice2;
    private TextView metGasLimit2;
    private TextView metNonce2;
    private TextView mtvMgs2;
    private TextView mPriceView;
    private Button escrowmanagerbtn1;
    private Button mainbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargeeth);

        mAddressView = findViewById(R.id.address2);
        mescrowaddress = findViewById(R.id.escrowaddress1);
        metGasPrice2 = findViewById(R.id.et_gas_priceg2);
        metGasLimit2 = findViewById(R.id.et_gas_limit2);
        metNonce2 = findViewById(R.id.et_nonce2);
        mtvMgs2 = findViewById(R.id.tv_mgs2);
        mPriceView = findViewById(R.id.postPrice_a2);

        final Button btnaddress1 = (Button) findViewById(R.id.btnaddress1);
        btnaddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(ChargeethActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        mescrowaddress.setText("cab01855ed706bdbe64141b698a38c46c74431bc");
        metGasPrice2.setText("180000");
        metGasLimit2.setText("900000");
        metNonce2.setText("4");

        //Button
        escrowmanagerbtn1 = findViewById(R.id.escrowmanagerbtn1);
        escrowmanagerbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTrasaction();
            }
        });

        mescrowaddress.addTextChangedListener(new TextWatcher() {
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
        mainbtn = findViewById(R.id.mainbtn);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(ChargeethActivity.this, Main3Activity.class);
                ChargeethActivity.this.startActivity(Intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddressView.setText(data.getStringExtra("address"));
    }

    private void sendTrasaction() {

        String from = mescrowaddress.getText().toString();
        String to = mAddressView.getText().toString();
        String value = mPriceView.getText().toString();
        String gasPrice = metGasPrice2.getText().toString();
        String gasLimit = metGasLimit2.getText().toString();
        String nonce = metNonce2.getText().toString();
        Observable
                .create((ObservableOnSubscribe<EthSendTransaction>) e -> {
                    Web3j web3j = Web3JService.getInstance();

                    String hexValue = KeyStoreUtils.signedTransactionData(from, to, nonce, gasPrice, gasLimit, value+"000000000000000000");
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
                        mtvMgs2.setText(respon.getError().getMessage() + "");
                        Log.e("transaction", respon.getError().getMessage() + "");
                    }


                }, throwable -> throwable.printStackTrace());

    }

    private void sendTrasactionSucceed(String result) {
        Log.e("transaction", result + "");
        mtvMgs2.setText(result + "");
        Toast.makeText(this, "발송 성공", Toast.LENGTH_SHORT).show();
        mescrowaddress.setText("");
        mAddressView.setText("");
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
                    metNonce2.setText(bigInteger.toString());
                }, throwable -> {
                    throwable.printStackTrace();
                });

    }

    @Override
    public void onClick(View v) {

    }
}
