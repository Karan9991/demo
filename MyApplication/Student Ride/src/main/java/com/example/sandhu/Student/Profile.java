package com.example.sandhu.Student;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity {
    EditText fname,lname,sid,uni,email,phone,pass;
    TextView t1;
    Button save,edit;
private boolean isValid;

    private boolean isImageSelected;

    private boolean isButton;

    private static final int CAMERA_REQUEST = 200;

    private ImageView pic, rpic;
    private DatabaseHelper db;

    private UserModel userModel;
    private Bitmap bp;
    private byte[] photo;

    TabLayout tabLayout;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tabLayout = (TabLayout)findViewById(R.id.simpleTabLayout);
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Profile");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("           View Profile");
        tabLayout.addTab(secondTab);

db = new DatabaseHelper(this);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        fname = (EditText)findViewById(R.id.firstname);
        lname = (EditText)findViewById(R.id.lastname);
        sid = (EditText)findViewById(R.id.studentid);
        uni = (EditText)findViewById(R.id.university);
        pass = (EditText)findViewById(R.id.passwordedit);
        email = (EditText)findViewById(R.id.emailedit);
        phone = (EditText)findViewById(R.id.phoneedit);
        t1 = (TextView) findViewById(R.id.textView3);
        pic = (ImageView)findViewById(R.id.imageView2);

        save = (Button)findViewById(R.id.savebtn);
        edit = (Button)findViewById(R.id.button3);

isButton = false;

tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (databaseHelper.countRecords() >=1) {
            if (tab.getPosition() == 1) {
                startActivity(new Intent(getApplicationContext(), ViewProfile.class));
            }
        }
        else {
            TabLayout.Tab tabb = tabLayout.getTabAt(0);
            tabb.select();
            Toast.makeText(getApplicationContext(),"No Record Found for View Profile",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});


//DATABASE
edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       if (db.checkdb() == false){
           Toast.makeText(getApplication(),"No Record Found For Edit Profile",Toast.LENGTH_SHORT).show();
       }
       else {
           fname.setText(db.getMoviep(1).getFname());
           lname.setText(db.getMoviep(1).getLname());
           sid.setText(db.getMoviep(1).getSid());
           uni.setText(db.getMoviep(1).getUni());
           pass.setText(db.getMoviep(1).getPass());
           email.setText(db.getMoviep(1).getEmail());
           phone.setText(db.getMoviep(1).getPhone());
           save.setText("UPDATE");
           // pic.setImageBitmap(convertToBitmap(db.getMoviep(1).getImage()));
           isButton = true;
       }
    }
});
        fname.setAllCaps(false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( TextUtils.isEmpty(fname.getText())){
                   fname.setError( "First Name is required!" );
                   isValid = false;

                }
                else if(TextUtils.isEmpty(lname.getText())){
                    lname.setError( "Last Name is required!" );
                    isValid = false;
                }
                else if (TextUtils.isEmpty(sid.getText())){
                    sid.setError( "Student ID is required!" );
                    isValid = false;
                }
                else if (TextUtils.isEmpty(uni.getText())){
                    uni.setError( "University Name is required!" );
                    isValid = false;
                }
                else if(TextUtils.isEmpty(pass.getText())){
                    pass.setError( "Password is required!" );
                    isValid = false;
                }
                else if (TextUtils.isEmpty(email.getText())){
                    email.setError( "E-Mail is required!" );
                    isValid = false;
                }
                else if (TextUtils.isEmpty(phone.getText())){
                    phone.setError( "Phone is required!" );
                    isValid = false;
                }
                else{
                    isValid = true;
                }

if (isValid == true && isButton == false && isImageSelected ==true) {
if (databaseHelper.countRecords() <1) {
    save();
    fname.setText("");
    lname.setText("");
    sid.setText("");
    uni.setText("");
    pass.setText("");
    email.setText("");
    phone.setText("");
    isButton = true;
}
else {
    update();
}
}
          else if(isButton == true && isImageSelected == true){
update();
                }
                else if (isImageSelected == false){
                    Toast.makeText(getApplication(),"Please Select Profile Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

           }

public void update(){
    databaseHelper.updateUserdatap(1, fname.getText().toString(), lname.getText().toString(), sid.getText().toString(), uni.getText().toString(), pass.getText().toString(), email.getText().toString(), phone.getText().toString(), profileImage(bp));
    fname.setText("");
    lname.setText("");
    sid.setText("");
    uni.setText("");
    pass.setText("");
    email.setText("");
    phone.setText("");
    save.setText("SAVE");
    Toast.makeText(getApplication(),"Updated",Toast.LENGTH_SHORT).show();
    isButton = false;
    pic.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
}





//PICTURE

    private Bitmap convertToBitmap(byte[] b){

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }

    public void selectImage(View view){
        new AlertDialog.Builder(Profile.this)
                .setTitle("Make Your Selection")
                // .setMessage("Do you think Mike is awesome?")
                // .setIcon(R.drawable.ninja)
                .setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, 2);
                                isImageSelected = true;
                                //  dialog.cancel();
                            }
                        })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(intent,CAMERA_REQUEST);
                            isImageSelected = true;
                        }
                       // dialog.cancel();
                    }
                }).show();




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();

                    if(choosenImage !=null){

                        bp=decodeUri(choosenImage, 400);
                       //pic.setImageBitmap(bp);
                    }
                }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Uri choosenImage = data.getData();

            if(choosenImage !=null){

                bp=decodeUri(choosenImage, 400);
              //  pic.setImageBitmap(bp);
            }

        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }


    private void getValues(){
    photo = profileImage(bp);
}

    //Insert data to the database
    private void addContact(){
        getValues();

          db.addProfile(new UserModel(fname.getText().toString(), lname.getText().toString(), sid.getText().toString(), uni.getText().toString(), pass.getText().toString(), email.getText().toString(), phone.getText().toString(), photo));
        startActivity(new Intent(Profile.this, MainActivity.class));

        Toast.makeText(getApplicationContext(),"Saved successfully", Toast.LENGTH_LONG).show();
    }

    public void save() {

            addContact();

    }



    @Override
    public boolean onSupportNavigateUp() {
        if (db.countRecords()>=1&&db.countsignRecords()>=1) {
            if ("signed".equals(db.getsign(1).getSign())) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        }
        finish();
        return super.onSupportNavigateUp();

    }


}
