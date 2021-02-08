package com.aindri.hemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;

import com.aindri.hemo.nav_package.NavAdapter;
import com.aindri.hemo.nav_package.Nav_Model;
import com.aindri.hemo.nav_package.NavigationControl;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageView img_navi,img_menu_dot;
    CircleImageView img_profile;
    RatingBar ratingBar1;
    EditText edit_feedback;
    Button bt_submit;
    FirebaseAuth mAuth;
    PopupMenu popupMenu;

    DrawerLayout drawer_layout;
    ListView list_menu;
    ArrayList<Nav_Model> navList;
    NavAdapter side_menu;
    NavigationView Navi;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
//    String UserId;

    MyReceiver myReceiver;

    float ratingNumber;
    String feedback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feedback);
        declareView();
        initNavFiels();
        prepareSideMenu();


        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);

//        preferences=getSharedPreferences("user",MODE_PRIVATE);
//        UserId=preferences.getString("GoogleUId",null);


        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating=ratingBar.getRating();
                ratingNumber=rating;


            }
        });

        img_menu_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu=new PopupMenu(FeedbackActivity.this,v);
                popupMenu.setOnMenuItemClickListener(FeedbackActivity.this);
                MenuInflater menuInflater=popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_dot,popupMenu.getMenu());
                popupMenu.show();
            }
        });

    }
    private void declareView(){
        myReceiver=new MyReceiver(FeedbackActivity.this,FeedbackActivity.this);
        img_menu_dot=findViewById(R.id.img_menu_dot);
        img_profile=findViewById(R.id.img_profile);
        ratingBar1=(RatingBar) findViewById(R.id.ratingBar1);//initiate rating bar
        edit_feedback=findViewById(R.id.edit_feedback);
        bt_submit=findViewById(R.id.bt_submit);
        mAuth=FirebaseAuth.getInstance();
    }

    public void check(String feedback){
        if(feedback==null){
            System.out.println(1);
        }else {
            edit_feedback.setText("You have already submitted your back");
            edit_feedback.setEnabled(false);
            ratingBar1.setEnabled(false);
            bt_submit.setEnabled(false);
            ratingBar1.setRating(1);
        }
    }


    private void prepareSideMenu(){
        list_menu=findViewById(R.id.list_menu);
        drawer_layout=findViewById(R.id.drawer_layout);
        Navi=findViewById(R.id.Navi);

        navList= NavigationControl.prepareSideMenu(this);
        side_menu=new NavAdapter(navList,this);
        list_menu.setAdapter(side_menu);

        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(drawer_layout.isDrawerOpen(GravityCompat.START)){
                    drawer_layout.closeDrawer(GravityCompat.START);
                }
                new NavigationControl(FeedbackActivity.this,FeedbackActivity.this).openActivity(navList.get(position).getActivity_name());

            }
        });

    }
    private void initNavFiels(){
        img_navi=findViewById(R.id.img_navi);

       img_navi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(drawer_layout.isDrawerOpen(GravityCompat.START)){
                   drawer_layout.closeDrawer(GravityCompat.START);
               }else
                   drawer_layout.openDrawer(GravityCompat.START);
           }
       });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    private void SignOut(){
//
        mAuth.signOut();
        Intent intent=new Intent(FeedbackActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.logout:   //FirebaseAuth.getInstance.SignOut()
                SignOut();
                return true;
            default:
                return false;
        }
    }


}