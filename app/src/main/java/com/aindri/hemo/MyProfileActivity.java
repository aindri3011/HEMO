package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aindri.hemo.nav_package.NavAdapter;
import com.aindri.hemo.nav_package.Nav_Model;
import com.aindri.hemo.nav_package.NavigationControl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageView img_navi,img_menu_dot;
    CircleImageView img_profile,profilepicture;
    FirebaseAuth mAuth;
    TextView type_name,type_email,type_phone,typecity,sayblood,gen,age;



    DrawerLayout drawer_layout;
    ListView list_menu;
    ArrayList<Nav_Model> navList;
    NavAdapter side_menu;
    NavigationView Navi;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
//    String UserId;

    MyReceiver myReceiver;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    PopupMenu popupMenu;

    Uri imagePath;
    String image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_profile);


     mAuth=FirebaseAuth.getInstance();
     type_name=findViewById(R.id.type_name);
     type_email=findViewById(R.id.type_email);
     type_phone=findViewById(R.id.type_phone);
     typecity=findViewById(R.id.typecity);
     sayblood=findViewById(R.id.sayblood);
     gen=findViewById(R.id.gen);
     age=findViewById(R.id.age);
     img_menu_dot=findViewById(R.id.img_menu_dot);
     profilepicture=findViewById(R.id.profilepicture);
     img_profile=findViewById(R.id.img_profile);



        myReceiver=new MyReceiver(MyProfileActivity.this,MyProfileActivity.this);
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);

        initNavFiels();
        prepareSideMenu();
        Showinfo();

//
        img_menu_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu=new PopupMenu(MyProfileActivity.this,v);
                popupMenu.setOnMenuItemClickListener(MyProfileActivity.this);
                MenuInflater menuInflater=popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_dot,popupMenu.getMenu());
                popupMenu.show();

            }
        });


    }



    private void Showinfo() {
   firebaseDatabase=FirebaseDatabase.getInstance();
   reference=firebaseDatabase.getReference(mAuth.getUid());




   reference.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {
           UserInformationModel info=snapshot.getValue(UserInformationModel.class);
//           if(image.equals("")){
//               profilepicture.setImageResource(R.drawable.prof_pic);
//               img_profile.setImageResource(R.drawable.prof_pic);
//           }
//           else
//           {
//               imagePath=Uri.parse(image);
//               Picasso.get().load(imagePath).fit().centerCrop().into(profilepicture);
//               Picasso.get().load(imagePath).fit().centerCrop().into(img_profile);
//           }

           type_name.setText(info.name);
           type_email.setText(info.email);
           type_phone.setText(info.phone);
           typecity.setText(info.city);
           sayblood.setText(info.bloodgroup);
           gen.setText(info.gender);
           age.setText(info.agegroup);



       }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {

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
                new NavigationControl(MyProfileActivity.this,MyProfileActivity.this).openActivity(navList.get(position).getActivity_name());

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

        mAuth.signOut();
        Intent intent=new Intent(MyProfileActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SignOut();
                finish();
                return true;
            default:return false;
        }
    }


}