package com.example.rhkdg.sharethetrip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;

import androidx.appcompat.app.AppCompatActivity;


public class TransactionlistActivity extends AppCompatActivity {

    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat;
    private TextView textviewHtmlDocument1;
    private String htmlContentInStringFormat1;
    private TextView address;
    private String address1;

    // add
    private TextView textviewHtmlDocument_2;
    private TextView textviewHtmlDocument_3;
    private TextView textviewHtmlDocument_4;
    private TextView textviewHtmlDocument_5;
    private TextView textviewHtmlDocument_6;
    private TextView textviewHtmlDocument_7;
    private String ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactionlist);

        address = findViewById(R.id.addressed);

        textviewHtmlDocument = (TextView)findViewById(R.id.transaction_text);
        textviewHtmlDocument1 = (TextView)findViewById(R.id.balance_text);
// add
        textviewHtmlDocument_2 = (TextView)findViewById(R.id.transaction_text2);
        textviewHtmlDocument_3 = (TextView)findViewById(R.id.transaction_text3);
        textviewHtmlDocument_4 = (TextView)findViewById(R.id.transaction_text4);
        textviewHtmlDocument_5 = (TextView)findViewById(R.id.transaction_text5);
        textviewHtmlDocument_6 = (TextView)findViewById(R.id.transaction_text6);
        textviewHtmlDocument_7 = (TextView)findViewById(R.id.transaction_text7);




        final Button btnaddress = (Button) findViewById(R.id.btnaddress);
        btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(TransactionlistActivity.this, SwitchWalletActivity.class);
                Intent.putExtra("SwitchMode", true);
                startActivityForResult(Intent, SwitchWalletActivity.FROM_ADDRESS);
            }
        });

        Button htmlTitleButton = (Button)findViewById(R.id.transaction_chk);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionAsyncTask jsoupAsyncTask = new TransactionAsyncTask();
                jsoupAsyncTask.execute();
            }
        });

        Button htmlTitleButton1 = (Button)findViewById(R.id.balance_chk);
        htmlTitleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceAsyncTask jsoupAsyncTask1 = new BalanceAsyncTask();
                jsoupAsyncTask1.execute();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        address.setText(data.getStringExtra("address"));
        address1 = "0x"+data.getStringExtra("address");
    }

    private class TransactionAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlPageUrl = "https://api-ropsten.etherscan.io/api?module=account&action=txlist&address="+address1+"&startblock=0&endblock=99999999&sort=asc&apikey=6TKJDAXV1239MCA6ZRQNUSVPCNMIG1B93C/";
                Document doc = Jsoup.connect(htmlPageUrl).ignoreContentType(true).get();
                Elements links = doc.select("body");

                for (Element link : links) {
                    htmlContentInStringFormat += (link.attr("pre#text")
                            + "("+link.text().trim() + ")\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            textviewHtmlDocument.setText(htmlContentInStringFormat);
            String[] array1 = htmlContentInStringFormat.split(",");
//            for(int i=1;i<array1.length;i++) textviewHtmlDocument.setText(array1[i]);
            ar=array1[2];
            textviewHtmlDocument.setText(array1[2].substring(11,ar.length()));
            textviewHtmlDocument_2.setText(array1[22]);
            textviewHtmlDocument_3.setText(array1[26]);
            textviewHtmlDocument_4.setText(array1[27]);
            textviewHtmlDocument_5.setText(array1[28]);
            textviewHtmlDocument_6.setText(array1[21]);
            textviewHtmlDocument_7.setText(array1[36]);


        }
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