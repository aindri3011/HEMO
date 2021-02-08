package com.aindri.hemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.aindri.hemo.nav_package.NavAdapter;
import com.aindri.hemo.nav_package.Nav_Model;
import com.aindri.hemo.nav_package.NavigationControl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, OnMapReadyCallback {
    ImageView img_navi,img_menu_dot;
    CircleImageView img_profile;
    PopupMenu popMenu;

    DrawerLayout drawer_layout;
    ListView list_menu;
    ArrayList<Nav_Model> navList;
    NavAdapter side_menu;
    NavigationView Navi;

    FirebaseAuth mAuth;

//    SharedPreferences preferences;
//    String UserId;
//    SharedPreferences.Editor editor;

    MyReceiver myReceiver;

    GoogleMap map;
    SupportMapFragment mapFragment;
    ArrayList<LatLng> arrayList=new ArrayList<LatLng>();
    LatLng abc=new LatLng(2.7311,75.8893);
    LatLng abc1=new LatLng(23.7311,76.8893);
    LatLng abc2=new LatLng(22.7311,75.8893);
    LatLng abc3=new LatLng(20.7311,75.8893);
    LatLng abc4=new LatLng(17.7311,75.0093);
    LatLng abc5=new LatLng(12.7311,74.8893);
    LatLng abc6=new LatLng(22.7311,65.8893);
    LatLng abc7=new LatLng(16.7655,55.6543);
    LatLng abc8=new LatLng(16.6754,54.5634);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);


        mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        arrayList.add(abc);
        arrayList.add(abc1);
        arrayList.add(abc2);
        arrayList.add(abc3);
        arrayList.add(abc4);
        arrayList.add(abc5);
        arrayList.add(abc6);
        arrayList.add(abc7);
        arrayList.add(abc8);



        img_profile=(CircleImageView)findViewById(R.id.img_profile);
        img_menu_dot=findViewById(R.id.img_menu_dot);
        mAuth=FirebaseAuth.getInstance();

        initNavFiels();
        prepareSideMenu();

//        preferences=getSharedPreferences("user",MODE_PRIVATE);
//        UserId=preferences.getString("GoogleUId",null);

        //code for implementing broadcastReceiver
        myReceiver=new MyReceiver(HomeActivity.this,HomeActivity.this);
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);


        img_menu_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu=new PopupMenu(HomeActivity.this,v);
                popMenu.setOnMenuItemClickListener(HomeActivity.this);
                MenuInflater menuInflater=popMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_dot,popMenu.getMenu());
                popMenu.show();
            }
        });

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
    new NavigationControl(HomeActivity.this,HomeActivity.this).openActivity(navList.get(position).getActivity_name());

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



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:   //FirebaseAuth.getInstance.SignOut()
        SignOut();
            return true;
            default: return false;

        }
    }

    private void SignOut(){

        mAuth.signOut();
        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        int i;
        for(i=0;i<arrayList.size();i++){
            map.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
            map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            map.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
        }
    }
}