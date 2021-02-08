package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.drm.ProcessedData;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class  EditProfileActivity extends AppCompatActivity implements  View.OnClickListener,PopupMenu.OnMenuItemClickListener,AdapterView.OnItemSelectedListener {
    ImageView img_navi, img_menu_dot;
    CircleImageView img_profile, profilepicture;
    EditText type_name, type_phone;
    TextView type_email, text_verifycontact;
    Button bt_edit, bt_save;
    Spinner spinner_gender, spinner_city, spinner_bloodgroup, spinner_agegroup;
    FirebaseAuth mAuth;
    MyReceiver myReceiver;
    PopupMenu popupMenu;


    String[] BloodGroup = {"Select Blood Group", "A +ve", "A -ve", "B +ve", "B -ve", "AB +ve", "AB -ve", "O +ve", "O -ve"};
    String[] City = {"Select Nearest City", "Agartala", "Aizawl", "Banglore", "Bhubneshwar", "Bhopal", "Chandigarh", "Chennai", "Darjeeling", "Delhi",
            "Dehradun", "GandhiNagar", "Gangtok", "Hyderabad", "Imphal", "Itanagar", "Kolkata", "jaipur", "kohima", "Lucknow", "Mumbai",
            "Panji", "Patna", "Raipur", "Ranchi", "Shimla", "Shillong", "Srinagar", "Trivanthpuram"};
    String[] Gender = {"Select Gender", "Male", "Female", "Others"};

    String[] AgeGroup = {"Select AgeGroup", "less than 18", "18-30", "30-50", "50-65", "greater than 65"};
    String city, gender, bloodgroup, agegroup, image;
    String name, phone, email, password;


    DrawerLayout drawer_layout;   //for navigation
    ListView list_menu;
    ArrayList<Nav_Model> navList;
    NavAdapter side_menu;
    NavigationView Navi;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
//    String UserId;

    FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    Uri downloadUri;
    ProgressDialog progressDialog;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        declareView();

        initNavFiels();
        prepareSideMenu();
        initListner();

        ArrayAdapter adapterCity = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, City);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapterCity);

        ArrayAdapter adapterGender = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, Gender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        ArrayAdapter adapterBlood = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, BloodGroup);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bloodgroup.setAdapter(adapterBlood);

        ArrayAdapter adapterAgeGroup = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, AgeGroup);
        adapterAgeGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_agegroup.setAdapter(adapterAgeGroup);

        unclickable();


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver, filter);


        Editdata();


        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

//        profilepicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("image/*");           //application//doc//audio//video//what kind of data being send
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
//            }
//        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==PICK_IMAGE && requestCode==RESULT_OK ){
//            progressDialog.setMessage("Image Uploading");
//            progressDialog.show();
//            final Uri uri=data.getData();
//            final StorageReference imageReference=storageReference.child("Photos").child(uri.getLastPathSegment());
//            imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            downloadUri=uri;
//                            image=downloadUri.toString();
//                            Picasso.get().load(downloadUri).fit().centerCrop().into(profilepicture);
//                            Toast.makeText(EditProfileActivity.this,"Upload Done",Toast.LENGTH_SHORT).show();
//                           progressDialog.dismiss();
//                        }
//                    });
//
//                }
//            });
//
////
//      }
//
//}




    private void declareView(){
        myReceiver=new MyReceiver(EditProfileActivity.this,EditProfileActivity.this);
        img_menu_dot=findViewById(R.id.img_menu_dot);
        type_email=findViewById(R.id.type_email);
        type_name=findViewById(R.id.type_name);
        type_phone=findViewById(R.id.type_phone);
        img_profile=(CircleImageView) findViewById(R.id.img_profile);
        profilepicture=(CircleImageView) findViewById(R.id.profilepicture);
        bt_edit=findViewById(R.id.bt_edit);
        bt_save=findViewById(R.id.bt_save);
        spinner_bloodgroup=findViewById(R.id.spinner_bloodgroup);
        spinner_city=findViewById(R.id.spinner_city);
        spinner_gender=findViewById(R.id.spinner_gender);
        spinner_agegroup=findViewById(R.id.spinner_agegroup);
        text_verifycontact=findViewById(R.id.text_verifycontact);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

    }


    private void initListner(){
        img_menu_dot.setOnClickListener(this);
      //  bt_save.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        spinner_city.setOnItemSelectedListener(this);
        spinner_gender.setOnItemSelectedListener(this);

        text_verifycontact.setOnClickListener(this);


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
                new NavigationControl(EditProfileActivity.this,EditProfileActivity.this).openActivity(navList.get(position).getActivity_name());

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
        switch(item.getItemId()){
            case R.id.logout:
        //Firebase getInstance signout
                SignOut();
                return true;
            default:   return false;

        }
    }
//    public void Upload(){
    //UserId//Images//Profile pic
//        UploadTask uploadTask=imageReference.putFile(imagePath);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(EditProfileActivity.this,"Upload Complete",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(EditProfileActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }



    private void Editdata() {
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
      final   DatabaseReference reference=firebaseDatabase.getReference(mAuth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final UserInformationModel information=snapshot.getValue(UserInformationModel.class);
//
//                if(image.equals("")){
//                    profilepicture.setImageResource(R.drawable.prof_pic);
//                    img_profile.setImageResource(R.drawable.prof_pic);
//                }
//                else
//                {
//                    imagePath=Uri.parse(image);
//                    Picasso.get().load(imagePath).fit().centerCrop().into(profilepicture);
//                    Picasso.get().load(imagePath).fit().centerCrop().into(img_profile);
//                }
//

                type_name.setText(information.name);
                type_email.setText(information.email);
                type_phone.setText(information.phone);

                ArrayAdapter myAdap1=(ArrayAdapter)spinner_city.getAdapter();
                int spinnerPosition1=myAdap1.getPosition(city);
                spinner_city.setSelection(spinnerPosition1);

                ArrayAdapter myAdap2=(ArrayAdapter)spinner_bloodgroup.getAdapter();
                int spinnerPosition2=myAdap2.getPosition(bloodgroup);
                spinner_bloodgroup.setSelection(spinnerPosition2);

                ArrayAdapter myAdap3=(ArrayAdapter)spinner_gender.getAdapter();
                int spinnerPosition3=myAdap3.getPosition(gender);
                spinner_gender.setSelection(spinnerPosition3);

                ArrayAdapter  myAdap4=(ArrayAdapter)spinner_agegroup.getAdapter();
                int spinnerPosition4=myAdap4.getPosition(agegroup);
                spinner_agegroup.setSelection(spinnerPosition4);
//                unclickable();
//                bt_save.setVisibility(View.INVISIBLE);



                bt_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     name=type_name.getText().toString();
                     phone=type_phone.getText().toString();
                     email=type_email.getText().toString();
                     password=information.password;
                     city=spinner_city.getSelectedItem().toString();
                     bloodgroup=spinner_bloodgroup.getSelectedItem().toString();
                     gender=spinner_gender.getSelectedItem().toString();
                     agegroup=spinner_agegroup.getSelectedItem().toString();




                       UserInformationModel info=new UserInformationModel(name,email,password,phone,city,gender,bloodgroup,agegroup);
                       reference.setValue(info);

                       startActivity(new Intent(EditProfileActivity.this,MyProfileActivity.class));
                       finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SignOut(){
        mAuth.signOut();
        Intent intent=new Intent(EditProfileActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void setClickable(){
        type_name.setEnabled(true);
        type_phone.setEnabled(true);
        type_email.setEnabled(true);
        spinner_city.setEnabled(true);
        spinner_bloodgroup.setEnabled(true);
        spinner_gender.setEnabled(true);
        spinner_agegroup.setEnabled(true);
        profilepicture.setEnabled(true);

    }

    public void unclickable(){
        type_name.setEnabled(false);
        type_phone.setEnabled(false);
        type_email.setEnabled(false);
        spinner_city.setEnabled(false);
        spinner_bloodgroup.setEnabled(false);
        spinner_gender.setEnabled(false);
        spinner_agegroup.setEnabled(false);
        profilepicture.setEnabled(false);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
 switch (parent.getId()){
     case R.id.spinner_city:
     city=(String)parent.getItemAtPosition(position);
     break;

     case R.id.spinner_bloodgroup:
         bloodgroup=(String)parent.getItemAtPosition(position);
         break;
     case  R.id.spinner_gender:
         gender=(String)parent.getItemAtPosition(position);
         break;

 }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_edit:
                bt_save.setVisibility(View.VISIBLE);
                setClickable();
                break;
            case R.id.img_menu_dot:
                popupMenu=new PopupMenu(EditProfileActivity.this,v);
                popupMenu.setOnMenuItemClickListener(EditProfileActivity.this);
                MenuInflater menuInflater=popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_dot,popupMenu.getMenu());
                popupMenu.show();
                break;

            case R.id.text_verifycontact:
                startActivity(new Intent(EditProfileActivity.this,VerifyDetails.class));
                finish();
                break;

        }

    }
}