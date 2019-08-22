package com.example.sandhu.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sandhu.Student.DatabaseHelper;
import com.example.sandhu.Student.MainActivity;
import com.example.sandhu.Student.Profile;
import com.example.sandhu.Student.R;
import com.example.sandhu.Student.RideDetails;
import com.example.sandhu.Student.UserModel;
import com.example.sandhu.Student.ViewProfile;

import javax.microedition.khronos.egl.EGLDisplay;

public class Signin extends AppCompatActivity {
EditText emailsign,passsign;
Button btnsignin, btnsignup;
  DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    emailsign = (EditText)findViewById(R.id.editTextsignemail);
    passsign = (EditText)findViewById(R.id.editTextsignpass);
        btnsignin = (Button) findViewById(R.id.buttonsign);
        btnsignup = (Button)findViewById(R.id.buttonsignup);
        db = new DatabaseHelper(this);


    btnsignin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (db.countRecords()>=1) {
                if (emailsign.getText().toString().equals(db.getMoviep(1).getEmail()) && passsign.getText().toString().equals(db.getMoviep(1).getPass())) {
                    if (db.checkdbsign() == true) {
                        db.sign(new UserModel("signed"));

                    } else {
                        db.updatesignin(1, "signed");

                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    db.sign(new UserModel("notsigned"));
                    Toast.makeText(getApplicationContext(),"Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }

        }
    });
    btnsignup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    });
    }

}
