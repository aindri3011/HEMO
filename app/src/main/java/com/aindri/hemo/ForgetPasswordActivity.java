package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    ImageView go;
    EditText forgot_email;
    TextView exist_view;

    ProgressDialog startprogress,mprogress;

    InputValidation inputValidation;
    int flag=0;

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);
        declareView();

        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver,filter);







        exist_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        //code for sending link for password reset
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress.setMessage("Please Wait....");
                mprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mprogress.setCancelable(false);
                mprogress.show();

                ValidationRule();
                if(flag==0){
             sendPasswordResetLink();
                }
            }
        });
        startprogress.setMessage("Please Wait");
        startprogress.setMessage("Loading....");
        startprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        startprogress.setCancelable(false);
        startprogress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                startprogress.dismiss();
            }
        }).start();
    }

    public void sendPasswordResetLink(){
FirebaseAuth.getInstance().sendPasswordResetEmail(forgot_email.getText().toString())
        .addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            mprogress.dismiss();

            android.app.AlertDialog.Builder dialog1=new android.app.AlertDialog.Builder(ForgetPasswordActivity.this);
            dialog1.setTitle("Result");
            dialog1.setMessage("Please reset link to your registered Email Id");
            dialog1.setCancelable(false);
            dialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            dialog1.show();
        }

    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(ForgetPasswordActivity.this);
        dialog.setMessage("Email id does not Exist");
        dialog.setCancelable(false);
        dialog.show();
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
            }
        });
    }
   });

    }

    private void ValidationRule(){
        flag=0;

        if(!inputValidation.isEditTextFilled(forgot_email,"Please Enter your Email Id")){
            flag =1;
            return;
        }
        if(!inputValidation.isEditTextEmail(forgot_email,"Please Enter a Valid Email Id")){
            flag=1;
            return;
        }
    }
    private void declareView(){
        go=findViewById(R.id.go);
        forgot_email=findViewById(R.id.forgot_email);
        exist_view=findViewById(R.id.exist_user);
        mprogress=new ProgressDialog(this);
        startprogress=new ProgressDialog(this);
        inputValidation=new InputValidation(this);
        myReceiver=new MyReceiver(ForgetPasswordActivity.this,ForgetPasswordActivity.this);
    }


}