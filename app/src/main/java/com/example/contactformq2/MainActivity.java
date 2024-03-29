package com.example.contactformq2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    databasehelper mydb;
    EditText name,email,phone_home,phone_office;
    Button save,show;
    ImageView imageView;
    private static final int PICK_IMAGE = 1;
    private Uri imagePath;
    private Bitmap selectedImage;
    private String base= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new databasehelper(this);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone_home=findViewById(R.id.homephone);
        phone_office=findViewById(R.id.officephone);
        save=findViewById(R.id.save);
        show = findViewById(R.id.show);
        imageView=findViewById(R.id.imageview);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=mydb.getAllData();
                if(res.getCount()==0){
                    showmessage("Error","Nothing found");
                    //Toast.makeText(MainActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                }
                StringBuffer buffer=new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("ID :"+res.getString(0)+"\n");
                    buffer.append("Name :"+res.getString(1)+"\n");
                    buffer.append("Email :"+res.getString(2)+"\n");
                    buffer.append("Phone_Home :"+res.getString(3)+"\n");
                    buffer.append("Phone_Office :"+res.getString(4)+"\n\n");
                }
                //show all data
                showmessage("Data",buffer.toString());
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errormsg="";
                if(name.getText().toString().length()<5){
                    errormsg+="Name is too short\n";
                }
                if(!email.getText().toString().contains("@") || !email.getText().toString().contains(".")){
                    errormsg+="Email is not valid\n";
                }
                if(phone_home.getText().length()<11){
                    errormsg+="Phone number home is not valid\n";
                }
                if(phone_office.getText().length()<11){
                    errormsg+="Phone number office is not valid\n";
                }


                if(errormsg.length()>0){
                    showDialog(errormsg,"Back");
                }
                else
                {
                    String base=encodeImageViewImage();
                    boolean inserted=mydb.insertData(name.getText().toString(),email.getText().toString(),phone_home.getText().toString(),phone_office.getText().toString(),base);

                    if(inserted=true)
                        Toast.makeText(MainActivity.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Not Inserted", Toast.LENGTH_LONG).show();

                }

            }


        });


    }

    private void showmessage(String title, String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showDialog(String errormsg, String back) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage(errormsg);
        builder.setTitle("Error message");
        AlertDialog alert= builder.create();
        alert.show();
    }


    private String encodeImageViewImage() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }





}