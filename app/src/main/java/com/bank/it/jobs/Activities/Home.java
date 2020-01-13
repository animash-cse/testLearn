package com.bank.it.jobs.Activities;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Gravity;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.it.jobs.Fragments.BasicFragment;
import com.bank.it.jobs.Fragments.ComputerNetworkFragment;
import com.bank.it.jobs.Fragments.DLDFragment;
import com.bank.it.jobs.Fragments.DataStructureFragment;
import com.bank.it.jobs.Fragments.DatabaseFragment;
import com.bank.it.jobs.Fragments.OperatingSystemFragment;
import com.bank.it.jobs.Fragments.OthersFragment;
import com.bank.it.jobs.Fragments.ProgrammingFragment;
import com.bank.it.jobs.Fragments.TelecommunicationFragment;
import com.bumptech.glide.Glide;
import com.bank.it.jobs.Fragments.HomeFragment;
import com.bank.it.jobs.Fragments.ProfileFragment;
import com.bank.it.jobs.Fragments.SettingsFragment;
import com.bank.it.jobs.Models.Post;
import com.bank.it.jobs.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    Dialog popAddPost ;
    ImageView popupUserImage,popupAddBtn;
    ImageButton pestQuestion, clearQuestion, pestAnswer, clearAnswer, openCamera;
    TextView popupQues,popupAns;
    Spinner popupCategory, popupChapter;
    ProgressBar popupClickProgress;
    private GoogleSignInClient mGoogleSignInClient, mGoogleApiClient;


    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Google Sign in initialisation
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // ini popup
        iniPopup();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        updateNavHeader();


        // set the home fragment as the default one
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();



    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupQues = popAddPost.findViewById(R.id.popup_title);
        popupAns = popAddPost.findViewById(R.id.popup_description);

        pestQuestion = popAddPost.findViewById(R.id.pest_question);
        pestAnswer = popAddPost.findViewById(R.id.pest_ans);
        clearQuestion = popAddPost.findViewById(R.id.clear_ques);
        clearAnswer = popAddPost.findViewById(R.id.clear_ans);

        openCamera = popAddPost.findViewById(R.id.capture_ans);

        popupCategory = popAddPost.findViewById(R.id.popup_category);
        popupChapter = popAddPost.findViewById(R.id.popup_chapter);

        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        // load Current user profile photo

        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        // Spinner data
        final String Category[] = {"Category","Basic","Programming", "Computer Network", "Data Structure", "Database", "Telecommunication",
                "Operating System", "Digital Logic Design", "Others"};
        final String Chapter[] = {"Chapter","Fundamentals", "Operating System", "Compiler","Database","Data Structure & Algorithm","Object Oriented Programming",
        "Microprocessor","Digital System","Digital Electronics","Data Communication & Networking","Telecommunication","Software Engineering",
        "Computer Security","Cloud Computing","Programming","NET Framework","Data Center","ANN","Big Data","Digital Marketing","AI",
        "Abbreviation"};

        final ArrayAdapter<String> categoryData = new ArrayAdapter<String>(Home.this, android.R.layout.simple_spinner_dropdown_item, Category);
        popupCategory.setAdapter(categoryData);
        final ArrayAdapter<String> chapterData = new ArrayAdapter<String>(Home.this, android.R.layout.simple_spinner_dropdown_item, Chapter);
        popupChapter.setAdapter(chapterData);
        // Add post click Listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image
                if(popupCategory.getSelectedItem().toString()=="Category" && popupChapter.getSelectedItem().toString()=="Chapter"){
                    showMessage("Please select Category|Chapter");
                }else {
                    if (!popupQues.getText().toString().isEmpty() && !popupAns.getText().toString().isEmpty()
                            && !popupCategory.getSelectedItem().toString().isEmpty()&& !popupChapter.getSelectedItem().toString().isEmpty()
                            ) {

                        //everything is okey no empty or null value
                        // TODO Create Post Object and add it to firebase database
                        // first we need to upload post Image
                        // access firebase storage
                        Post post = new Post(popupQues.getText().toString(),
                                popupAns.getText().toString(),
                                popupCategory.getSelectedItem().toString(),
                                popupChapter.getSelectedItem().toString(),
                                currentUser.getUid(),
                                currentUser.getPhotoUrl().toString());

                        // Add post to firebase database

                        addPost(post);

                    } else {
                        showMessage("Please verify all input fields and choose Post Image");
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popupClickProgress.setVisibility(View.INVISIBLE);

                    }
                }


            }
        });


        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pestQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipMan = (ClipboardManager)getSystemService(v.getContext().CLIPBOARD_SERVICE);
                popupQues.setText(clipMan.getText());
            }
        });
        pestAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipMan = (ClipboardManager)getSystemService(v.getContext().CLIPBOARD_SERVICE);
                popupAns.setText(clipMan.getText());
            }
        });

        clearAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAns.setText("");
            }
        });
        clearQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupQues.setText("");
            }
        });


    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });


    }


    private void showMessage(String message) {

        Toast.makeText(Home.this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

            mAuth.signOut();

            /*FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();*/
            Intent loginActivity = new Intent(getApplicationContext(),GoogleLogin.class);
            startActivity(loginActivity);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        } else if (id == R.id.nav_profile) {

            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
        } else if (id == R.id.nav_basic) {

            getSupportActionBar().setTitle("Basic");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new BasicFragment()).commit();

        } else if (id == R.id.nav_programming) {

            getSupportActionBar().setTitle("Programming");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProgrammingFragment()).commit();
        } else if (id == R.id.nav_data_structure) {

            getSupportActionBar().setTitle("Data Structure");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DataStructureFragment()).commit();
        } else if (id == R.id.nav_database) {

            getSupportActionBar().setTitle("Database");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DatabaseFragment()).commit();
        } else if (id == R.id.nav_computer_network) {

            getSupportActionBar().setTitle("Computer Network");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ComputerNetworkFragment()).commit();
        } else if (id == R.id.nav_operating_system) {

            getSupportActionBar().setTitle("Operating System");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new OperatingSystemFragment()).commit();
        } else if (id == R.id.nav_telecommunication) {

            getSupportActionBar().setTitle("Telecommunication");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new TelecommunicationFragment()).commit();
        } else if (id == R.id.nav_dld) {

            getSupportActionBar().setTitle("Digital Logic Design");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DLDFragment()).commit();
        } else if (id == R.id.nav_others) {

            getSupportActionBar().setTitle("Others");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new OthersFragment()).commit();
        } else if (id == R.id.nav_settings) {

            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();


        }
        /*else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);




    }

}
