package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail;
    TextInputLayout textInputLayoutPassword,textInputLayoutEmail;
    TextInputEditText editPassword;
    AppCompatTextView text_forgot,text_create;
    AppCompatButton bt_login;
    CheckBox remember_me;

    FirebaseAuth mAuth;


//
    MyReceiver myReceiver;




    InputValidation inputValidation;            //reference create
    int flag=0;                        //inside case if true then SignIn


    ProgressDialog progressDialog;      //for Please wait!signing.....
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        declareView();
        declareListener();

        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);



//
    }

    private void declareView(){
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        inputValidation=new InputValidation(this);
        myReceiver=new MyReceiver(LoginActivity.this,LoginActivity.this);


        textInputLayoutEmail=(TextInputLayout) findViewById(R.id.textInputLayoutEmaiL);
        editEmail=(EditText) findViewById(R.id.editEmail);
        textInputLayoutPassword=(TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        editPassword=(TextInputEditText) findViewById(R.id.editPassword);
        text_forgot=(AppCompatTextView) findViewById(R.id.text_forgot);
        text_create=(AppCompatTextView) findViewById(R.id.text_create);
        bt_login=(AppCompatButton) findViewById(R.id.bt_login);
        remember_me=(CheckBox)findViewById(R.id.remember_me);

    }

    private  void declareListener(){
        bt_login.setOnClickListener(this);
       text_create.setOnClickListener(this);
        text_forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.remember_me:
                if(flag==0){
                    Toast.makeText(getApplicationContext(),"Password Remembered",Toast.LENGTH_SHORT).show();
                }

            case R.id.bt_login:
             ValidationRule();
                if(flag==0){
                    StartSign();
                }
                break;

            case R.id.text_create:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

                break;
            case R.id.text_forgot:
                Intent intent1=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent1);
                break;


        }
    }

    private void ValidationRule(){
        flag=0;
        if(!inputValidation.isEditTextFilled(editEmail,"Enter Valid Email")){
            flag=1;
            return;

        }
        if(!inputValidation.isEditTextEmail(editEmail,"Enter Valid Email")){
            flag=1;
            return;
        }
        if(!inputValidation.isInputEditTextFilled((TextInputEditText) editPassword,textInputLayoutPassword,"Enter Password")){
            flag=1;
            return;
        }
        if(!inputValidation.isPasswordLengthSatisfied((TextInputEditText)editPassword,textInputLayoutPassword,"Password Length short")){
            flag=1;
            return;
        }



    }

         private void StartSign(){
       progressDialog.setMessage("Signing....");
       progressDialog.show();
       email=editEmail.getText().toString();   //checking   whether email  is null
       password=editPassword.getText().toString();//checking   whether  password is null


             //method for signing in with username and password
      mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
          if(task.isSuccessful()){
      //        Snackbar.make(getCurrentFocus(),"Signing Successful",Snackbar.LENGTH_LONG).show();
              Toast.makeText(LoginActivity.this, "Signing Successful", Toast.LENGTH_SHORT).show();

             Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
             startActivity(intent);
             progressDialog.dismiss();
             finish();
          }
          else
          {
              Snackbar.make(getCurrentFocus(),"Invalid Credentials ",Snackbar.LENGTH_LONG).show();
              progressDialog.dismiss();
          }

      }
  });
          }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
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