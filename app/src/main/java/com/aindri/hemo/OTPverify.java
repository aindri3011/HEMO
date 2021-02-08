package com.aindri.hemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class OTPverify extends AppCompatActivity {

    CountryCodePicker ccp;
    PinView pin;
    TextView resend_otp,text_otpsending;
    ProgressBar inprogress;
    EditText Mobile_num;
    Button button_send;
    String verificationid;
    PhoneAuthProvider.ForceResendingToken token;
    boolean verificationInprogress=false;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_o_t_pverify);
        declareView();

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verificationInprogress){
                 if(!Mobile_num.getText().toString().trim().isEmpty()&& Mobile_num.getText().toString().trim().length()==10){
             String phonenum="+"+ccp.getSelectedCountryCode()+Mobile_num.getText().toString().trim();
                  inprogress.setVisibility(View.VISIBLE);
                  text_otpsending.setVisibility(View.VISIBLE);
                  text_otpsending.setText("Sending OTP....");
                  requestOTP(phonenum);

                 }else
                     {
                 Mobile_num.setError("Phone number is not valid");
                 }

                }
                else {
                    String userOTP = pin.getText().toString();
                    if (!userOTP.isEmpty() && userOTP.length() == 6) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, userOTP);
                        verifyAuth(credential);
                    } else
                        pin.setError("Valid OTP is required");
                }
            }
        });


    }

    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               Toast.makeText(OTPverify.this,"Verification Successful",Toast.LENGTH_LONG).show();

             startActivity(new Intent(OTPverify.this,LoginActivity.class));
             finish();
           }
           else{
         Toast.makeText(OTPverify.this,"Verification has been failed",Toast.LENGTH_LONG).show();
           }
            }
        });

    }

    private void declareView(){
        ccp=findViewById(R.id.ccp);
        resend_otp=findViewById(R.id.resend_otp);
        text_otpsending=findViewById(R.id.text_otpsending);
        inprogress=findViewById(R.id.inprogess);
        Mobile_num=findViewById(R.id.Mobile_num);
        button_send=findViewById(R.id.button_send);
        pin=findViewById(R.id.pin);
        mAuth=FirebaseAuth.getInstance();
    }

    private void requestOTP(String phonenum){
   PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenum, 60L, TimeUnit.SECONDS, OTPverify.this,
           new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
               @Override
               public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                   super.onCodeSent(s, forceResendingToken);
                   inprogress.setVisibility(View.GONE);
                   text_otpsending.setVisibility(View.GONE);
                   pin.setVisibility(View.VISIBLE);
                   verificationid=s;
                   token=forceResendingToken;
                   button_send.setText("Verify");
                   verificationInprogress=true;
               }

               @Override
               public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                   super.onCodeAutoRetrievalTimeOut(s);
                  Toast.makeText(OTPverify.this,"OTP Expired,Re-Request the OTP",Toast.LENGTH_LONG).show();
                  resend_otp.setVisibility(View.VISIBLE);

               }

               @Override
               public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                   verifyAuth(phoneAuthCredential);

               }

               @Override
               public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPverify.this,"Verification has been failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
               }
           });
    }
}