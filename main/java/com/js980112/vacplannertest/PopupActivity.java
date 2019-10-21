package com.js980112.vacplannertest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PopupActivity extends Activity {
    ImageView img;
    EditText et1;
    Button btnSubmit;


    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        img=findViewById(R.id.img);
        et1=findViewById(R.id.et1);
        btnSubmit=findViewById(R.id.btnSubmit);

        mHelper=new MyDBHelper(this);
        db=mHelper.getWritableDatabase();


        //공유로 받아왔을경우
        try {
            Intent intent = getIntent();
            String action=intent.getAction();
            String type=intent.getType();
            if(Intent.ACTION_SEND.equals(action)&&type!=null){
                if(type.equals("text/plain"))
                    et1.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
                else if(type.startsWith("image/")){
                    Uri imageUri=(Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    img.setImageURI(imageUri);
                }

            }else{
                Bitmap bitmap=BitmapFactory.decodeByteArray(intent.getByteArrayExtra("img"),0,intent.getByteArrayExtra("img").length);
                img.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query="";

                query = "insert into WishList values(null, ?,?);";
                // tvtest.setText(query + "");
                SQLiteStatement p=db.compileStatement(query);

                p.bindString(1,et1.getText().toString());
                p.bindBlob(2,getByteArrayFromDrawable(img.getDrawable()));
                p.execute();

                Toast.makeText(getApplicationContext(),"submit",Toast.LENGTH_SHORT).show();
                finish();
                //refreshDB();
            }
        });

    }
    public byte[] getByteArrayFromDrawable(Drawable d){
        Bitmap bitmap=((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] data=stream.toByteArray();
        return data;
    }
    public boolean exeQuery(String query) {
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            //tvtest.append(e+"");
            return false;
        }
    }

}
