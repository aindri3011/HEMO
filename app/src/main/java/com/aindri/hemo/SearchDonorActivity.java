package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.aindri.hemo.nav_package.NavAdapter;
import com.aindri.hemo.nav_package.Nav_Model;
import com.aindri.hemo.nav_package.NavigationControl;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchDonorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener {
    ImageView img_navi, img_menu_dot,imagesearch;
    CircleImageView img_profile;
    Spinner select_blood;

    PopupMenu popupMenu;


    String[] BloodGroup = {"Select Blood Group","A +ve", "A -ve", "B +ve", "B -ve", "AB +ve", "AB -ve", "O +ve", "O -ve"};
    String blgrp;

    DrawerLayout drawer_layout;
    ListView list_menu;
    ArrayList<Nav_Model> navList;
    NavAdapter side_menu;
    NavigationView Navi;

    ProgressDialog startprogress;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
//    String UserId;

    MyReceiver myReceiver;

    RecyclerView recycler_vw;
    CustomAdapter customAdapter;
    ArrayList<Rowitem>arrayList;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref,rootref;
    LinearLayoutManager linearLayoutManager;
    String namecheck,bloodcheck;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_donor);
        declareView();
        initNavFiels();
        prepareSideMenu();


        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);


        ArrayAdapter adapterBlood = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, BloodGroup);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_blood.setAdapter(adapterBlood);

        select_blood.setOnItemSelectedListener(this);




        img_menu_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu=new PopupMenu(SearchDonorActivity.this,v);
                popupMenu.setOnMenuItemClickListener(SearchDonorActivity.this);
                MenuInflater menuInflater=popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_dot,popupMenu.getMenu());
                popupMenu.show();
            }
        });



     progressDialog=new ProgressDialog(this);

     imagesearch.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             setDataAdapter();

         }
     });


     arrayList=new ArrayList<>();

     progressDialog.setTitle("Please Wait!");
     progressDialog.setMessage("Loading.....");
     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     progressDialog.show();
     new Thread(new Runnable() {
         @Override
         public void run() {
             try {
          Thread.sleep(5000);
             }catch (Exception e){
           e.printStackTrace();
             }
             progressDialog.dismiss();

         }
     }).start();
    }


    private void prepareSideMenu() {   //navigation
        list_menu = findViewById(R.id.list_menu);
        drawer_layout = findViewById(R.id.drawer_layout);
        Navi = findViewById(R.id.Navi);

        navList = NavigationControl.prepareSideMenu(this);
        side_menu = new NavAdapter(navList, this);
        list_menu.setAdapter(side_menu);

        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                }
                new NavigationControl(SearchDonorActivity.this, SearchDonorActivity.this).openActivity(navList.get(position).getActivity_name());

            }
        });

    }

    private void initNavFiels() { //navigation
        img_navi = findViewById(R.id.img_navi);

        img_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                } else
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

    private void declareView() {
        myReceiver=new MyReceiver(SearchDonorActivity.this,SearchDonorActivity.this);
        img_menu_dot = findViewById(R.id.img_menu_dot);
        img_profile = findViewById(R.id.img_profile);
        select_blood = findViewById(R.id.select_blood);
        recycler_vw = findViewById(R.id.recycler_vw);
        imagesearch=findViewById(R.id.imagesearch);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.select_blood:
                blgrp = (String) parent.getItemAtPosition(position);
                getAllUser();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                //Firebase getInstance signout
                SignOut();
                return true;
            default:  return false;
        }
    }


    private void SignOut(){

        mAuth.signOut();

        Intent intent=new Intent(SearchDonorActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private  void getAllUser(){
    //    final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref= rootref.child("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren()) {
                    //  UserInformationModel info=ds.getValue(UserInformationModel.class);
                    String user_id_key=  ds.getKey();

                    namecheck = ds.child(user_id_key).child("name").getValue(String.class);
                    bloodcheck = ds.child(user_id_key).child("bloodgroup").getValue(String.class);

                    if (bloodcheck.equals(blgrp)) {
                        Rowitem rowitem = new Rowitem();
                        rowitem.setKey(user_id_key);
                        rowitem.setName(namecheck);
                        rowitem.setBlood(bloodcheck);
                        arrayList.add(rowitem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


            public  void setDataAdapter(){
        if(arrayList.isEmpty()||arrayList==null){
            AlertDialog.Builder dialog1=new AlertDialog.Builder(SearchDonorActivity.this);
            dialog1.setTitle("RESULT");
            dialog1.setIcon(R.drawable.error);
            dialog1.setMessage(Html.fromHtml("<font color='#FFFFFF'>Oops! No data found</font>"));
            dialog1.setCancelable(false);
            dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog1.show();
            customAdapter=new CustomAdapter(arrayList,this);
            linearLayoutManager=new LinearLayoutManager(getApplicationContext());
            recycler_vw.setLayoutManager(linearLayoutManager);
            recycler_vw.setItemAnimator(new DefaultItemAnimator());
            recycler_vw.setAdapter(customAdapter);
        }
        else
        {
            customAdapter=new CustomAdapter(arrayList,this);
            linearLayoutManager=new LinearLayoutManager(getApplicationContext());
            recycler_vw.setLayoutManager(linearLayoutManager);
            recycler_vw.setItemAnimator(new DefaultItemAnimator());
            recycler_vw.setAdapter(customAdapter);
            progressDialog.setMessage("Please wait!Donors list is loading");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                  try{
                      Thread.sleep(5000);
                  }catch (Exception e){
                      e.printStackTrace();
                  }
                  progressDialog.dismiss();
                }
            }).start();
        }

}


}