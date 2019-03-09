package com.example.rhkdg.sharethetrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TextView boardbtn = (TextView) findViewById(R.id.boardbtn);

        boardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, MainActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });

        final TextView makeplanbtn = (TextView) findViewById(R.id.makeplanbtn);

        makeplanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, BuytripActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });

        final TextView transactionlist_btn = (TextView) findViewById(R.id.transactionlist_btn);

        transactionlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, TransactionlistActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });

        /*final TextView button5 = (TextView) findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, HomeActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });*/

        final TextView findstorebtn = (TextView) findViewById(R.id.findstorebtn);

        findstorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, FindstoreActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });
        final TextView viewguidebtn = (TextView) findViewById(R.id.viewguidebtn);

        viewguidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(Main3Activity.this, MenuguideActivity.class);
                Main3Activity.this.startActivity(Intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        ImageView imageView_p;
        TextView textView_n;
        TextView textView_e;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                // String providerId = profile.getProviderId();

                // UID specific to the provider
                // String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                textView_n = (TextView) findViewById(R.id.textView_n);
                textView_n.setText(name);

                String email = profile.getEmail();
                textView_e = (TextView) findViewById(R.id.textView_e);
                textView_e.setText(email);

                imageView_p = findViewById(R.id.imageView_p);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Main3Activity.this, ContractmainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Main3Activity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Main3Activity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else if (id == R.id.nav_signout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
