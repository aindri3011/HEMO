package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    NestedScrollView nestedScrollView;
    TextInputLayout textInputLayoutName,textInputLayoutPhone,textInputLayoutEmail,textInputLayoutPasswordregister,textInputLayoutConfirmPasswordregister;
    TextInputEditText name_register,phone_register,registerEmail, registerPassword, confirm_register_Password;
    Spinner spinner_city,spinner_gender,spinner_bloodgroup,spinner_agegroup;
    TextView dob;
    AppCompatButton bt_register;
    AppCompatTextView text_memberlogin;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
 //   DatabaseReference databaseReference;
    String name,phone,email,password;
    String Donor;   //for database checkbx


//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    MyReceiver myReceiver;



    InputValidation inputValidation;
    int flag=0;

    String [] City={"Select Nearest City","Agartala","Aizawl","Banglore","Bhubneshwar","Bhopal","Chandigarh","Chennai","Delhi",
            "Dehradun","GandhiNagar","Gangtok","Hyderabad","Imphal","Itanagar","Kolkata","jaipur","kohima","Lucknow","Mumbai",
            "Panji","Patna","Raipur","Ranchi","Shimla","Shillong","Srinagar","Darjeeling","Trivanthpuram"};

    String [] Gender={"Select Gender","Male","Female","Others"};
    String [] BloodGroup={"Select Blood Group","A +ve","A -ve","B +ve","B -ve","AB +ve","AB -ve","O +ve","O -ve"};
    String [] AgeGroup={"Select AgeGroup","less than 18","18-30","30-50","50-65","greater than 65"};

    String city,gender,bloodgroup,agegroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        declareView();
        initListener();


        ArrayAdapter adapterCity=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,City);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapterCity);

        ArrayAdapter adapterGender=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,Gender);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        ArrayAdapter adapterAgeGroup=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,AgeGroup);
        adapterAgeGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_agegroup.setAdapter(adapterAgeGroup);

        ArrayAdapter adapterBlood=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,BloodGroup);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bloodgroup.setAdapter(adapterBlood);


//        preferences=getSharedPreferences("user",MODE_PRIVATE);
//        editor=preferences.edit();     ////setting value of user id in first shared preference


        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);


    }


    private void declareView(){
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        myReceiver=new MyReceiver(RegisterActivity.this,RegisterActivity.this);
        inputValidation = new InputValidation(this);

        nestedScrollView=(NestedScrollView)findViewById(R.id.nestedScrollView);
        textInputLayoutName=(TextInputLayout)findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail=(TextInputLayout)findViewById(R.id.textInputLayoutEmaiL);
        textInputLayoutPhone=(TextInputLayout)findViewById(R.id.textInputLayoutPhone);
        textInputLayoutPasswordregister=(TextInputLayout)findViewById(R.id.textInputLayoutPasswordregister);
        textInputLayoutConfirmPasswordregister=(TextInputLayout)findViewById(R.id.textInputLayoutConfirmPasswordregister);
        phone_register=(TextInputEditText) findViewById(R.id.phone_register);
        registerEmail=(TextInputEditText) findViewById(R.id.registerEmail);
        name_register=(TextInputEditText) findViewById(R.id.name_register);
        registerPassword=(TextInputEditText) findViewById(R.id.registerPassword);
       confirm_register_Password=(TextInputEditText) findViewById(R.id.confirm_register_Password);
       spinner_agegroup=findViewById(R.id.spinner_agegroup);
       spinner_bloodgroup=findViewById(R.id.spinner_bloodgroup);
       spinner_city =findViewById(R.id.spinner_city);
       spinner_gender=findViewById(R.id.spinner_gender);
        dob=findViewById(R.id.dob);
        bt_register=findViewById(R.id.bt_register);
        text_memberlogin=findViewById(R.id.text_memberlogin);





    }
    private void initListener(){
        text_memberlogin.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        spinner_city.setOnItemSelectedListener(this);
        spinner_agegroup.setOnItemSelectedListener(this);
        spinner_bloodgroup.setOnItemSelectedListener(this);
        spinner_gender.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//

          case R.id.bt_register:
                name=name_register.getText().toString().trim();
                email=registerEmail.getText().toString().trim();
                password=confirm_register_Password.getText().toString().trim();
                phone="+91"+phone_register.getText().toString().trim();
                city=spinner_city.getSelectedItem().toString();
                gender=spinner_gender.getSelectedItem().toString();
                bloodgroup=spinner_bloodgroup.getSelectedItem().toString();
                agegroup=spinner_agegroup.getSelectedItem().toString();
//              if(flag==0){
//                   ValidationRule();
//             }
//              else
                  registerUser(email,password);
               break;
            case R.id.text_memberlogin:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void registerUser( final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Snackbar.make(getCurrentFocus(), "Account Created Successfully", Snackbar.LENGTH_LONG).show();

                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Please Wait....");
                            progressDialog.show();

                            SendUserData();

                            progressDialog.dismiss();

                            Intent intent = new Intent(RegisterActivity.this, VerifyDetails.class);
                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }

                    }

//})
// .addOnFailureListener(new OnFailureListener() {
//     @Override
//     public void onFailure(@NonNull Exception e) {
//         progressDialog.dismiss();
//         Snackbar.make(getCurrentFocus(),"SignIn Unsuccessful! Email id exists",Snackbar.LENGTH_LONG).show();
//   }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void SendUserData() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference(mAuth.getUid());

        UserInformationModel info=new UserInformationModel(name,email,password,phone,city,gender,bloodgroup,agegroup);
        reference.setValue(info);
    }

    private void ValidationRule(){
        flag=0;
        if(!inputValidation.isInputEditTextFilled( name_register,textInputLayoutName,"Enter Full Name")){
            flag=1;
            return;
        }
        if(!inputValidation.isInputEditTextFilled( registerEmail,textInputLayoutEmail,"Enter Valid Email")){
           flag=1;
           return;
        }
        if(!inputValidation.isInputEditTextEmail(registerEmail,textInputLayoutEmail,"Enter Valid Email")){
            flag=1;
            return;
        }
        if(!inputValidation.isInputEditTextFilled(registerPassword,textInputLayoutPasswordregister,"Enter Password")){
            flag=1;
            return;
        }
        if(!inputValidation.isPasswordLengthSatisfied(registerPassword,textInputLayoutPasswordregister,"Password should comprise of minimum eight characters")){
            flag=1;
            return;
        }
        if(!inputValidation.isInputEditTextMatches(registerPassword,confirm_register_Password,textInputLayoutConfirmPasswordregister,"Password does not matches")){
            flag=1;
            return;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_city:
                city=(String)parent.getItemAtPosition(position);
                break;
            case R.id.spinner_bloodgroup:
                bloodgroup=(String)parent.getItemAtPosition(position);
            case R.id.spinner_gender:
                gender=(String)parent.getItemAtPosition(position);
            case  R.id.spinner_agegroup:
                agegroup=(String)parent.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Alert!");
        builder.setMessage("Do to you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }



}