package com.aindri.hemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class VerifyDetails extends AppCompatActivity {
    Button bt_verify_phone,bt_verify_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_details);
        bt_verify_email=findViewById(R.id.bt_verify_email);
        bt_verify_phone=findViewById(R.id.bt_verify_phone);


        bt_verify_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VerifyDetails.this,OTPverify.class);
                startActivity(intent);
                finish();
            }
        });
    }
}