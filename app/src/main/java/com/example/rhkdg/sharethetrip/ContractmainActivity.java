package com.example.rhkdg.sharethetrip;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhkdg.sharethetrip.Model.Web3JService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ContractmainActivity extends AppCompatActivity {

    private TextView mAddressField1;
    private TextView textviewHtmlDocument1;
    private String htmlContentInStringFormat1;
    private String address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractmain);
        verifyStoragePermissions(this);
        String s = Environment.getDataDirectory().toString();

        textviewHtmlDocument1 = (TextView)findViewById(R.id.checkvalue);

        Log.e("dir", "main:" + WalletUtils.getMainnetKeyDirectory());
        Log.e("dir", "def:" + WalletUtils.getDefaultKeyDirectory());
        Log.e("dir", "data:" + s);
        Log.e("dir", "getfiles:" + getFilesDir().getAbsolutePath());

        final Button btnaddress1 = (Button) findViewById(R.id.btnaddress1);
        btnaddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(ContractmainActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        final Button backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(ContractmainActivity.this, Main3Activity.class);
                ContractmainActivity.this.startActivity(Intent);
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

        mAddressField1 = findViewById(R.id.addressed1);
    }


    public void transation(View view) {
        startActivity(new Intent(this, SendActivity.class));
    }

    public void charge(View view) {
        startActivity(new Intent(this, ChargeethActivity.class));
    }

    public void generateWallet(View view) {

        startActivity(new Intent(this, GenerateWallet.class));

    }

    public void switchWallet(View view) {

        startActivity(new Intent(this, SwitchWalletActivity.class));
    }

    public void sampleContract(View view) {

        startActivity(new Intent(this,SampleContractActivity.class));
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"

    };


    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testNetwork(View view) {
        Observable.create((ObservableOnSubscribe<Web3ClientVersion>) e -> {
            Web3ClientVersion send = Web3JService.getInstance().web3ClientVersion().send();
            e.onNext(send);
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(web3ClientVersion -> {
                    String version = web3ClientVersion.getWeb3ClientVersion();
                    Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
                    Log.i("web3j",version);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddressField1.setText(data.getStringExtra("address"));
        address1 = "0x"+data.getStringExtra("address");
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
