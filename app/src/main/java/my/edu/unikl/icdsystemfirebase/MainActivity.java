package my.edu.unikl.icdsystemfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import my.edu.unikl.icdsystemfirebase.Firebase.UserInformation;
import my.edu.unikl.icdsystemfirebase.other.CircleTransform;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ProgressDialog pDialog;

    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private View navHeader;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    private String uPhotoUrl;

    boolean doubleBackToExitPressedOnce = false;

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    //Firebase database reference
    private FirebaseDatabase myDB ;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
         user = firebaseAuth.getCurrentUser();

        //Toast.makeText(this, "Welcome, "+user.getEmail(), Toast.LENGTH_LONG).show();



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        imgNavHeaderBg.setOnClickListener(this);

        loadNavHeader();

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);



    }


     /**Load navigation menu header information
     like background image, profile image
      */
      private void loadNavHeader() {

        // name, website
        txtName.setText(user.getDisplayName());
        txtWebsite.setText(user.getEmail());
          Toast.makeText(this, "Get Display name" +user.getDisplayName(), Toast.LENGTH_LONG).show();

          // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

         // Toast.makeText(this, user.getPhotoUrl().toString() , Toast.LENGTH_SHORT).show();
        // Loading profile image
          loadUserPhoto();


    }

    private void loadUserPhoto(){

        DatabaseReference databaseReference = myDB.getInstance().getReference();
        Query queryUser = databaseReference.child("Users").orderByChild(user.getUid());
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                   // Log.d("User key", child.getKey());
                    Log.d("User val", child.child("uPhotoUrl").getValue().toString());

                    Glide.with(MainActivity.this).load( child.child("uPhotoUrl").getValue().toString())
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(MainActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

            Intent gSetting = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(gSetting);
            return true;
        }

        if (id == R.id.action_admin) {

            Intent gAdmin = new Intent(MainActivity.this, AdminHome.class);
            startActivity(gAdmin );
            return true;
        }

        if (id == R.id.action_logout) {

            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));

            Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_SHORT).show();

            return true;
        }

        if (id == R.id.action_test) {

            Intent gTest = new Intent(this, TestActivity.class);
            startActivity(gTest );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_lvl3:
                fragment = new Lvl3Fragment();
                break;
            case R.id.nav_surau:
                fragment = new SurauFragment();
                break;
            case R.id.nav_lps:
                fragment = new LpsFragment();
                break;
            case R.id.nav_lvl25:
                fragment = new Lvl25Fragment();
                break;
            case R.id.nav_chatroom:

                startActivity(new Intent(this, Lvl3Chatroom.class));

                break;

            case R.id.nav_setting:
                Intent gSettings = new Intent(this, SettingsActivity.class);
                startActivity(gSettings);
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top);
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());

        return true;
    }





    @Override
    public void onClick(View v) {

        if( v == imgNavHeaderBg){

            startActivity(new Intent(this, ProfileActivity.class));

        }
    }
}
