package com.js980112.vacplannertest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class SettingActivity extends AppCompatActivity {
    Switch sw1;
    GridView gv;
    NotificationSet notificationSet;

    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    MyCursorAdapter myCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sw1=findViewById(R.id.sw1);
        gv=findViewById(R.id.gvlist);

        try {
            mHelper = new MyDBHelper(this);
            db = mHelper.getWritableDatabase();
            cursor = db.rawQuery("select w_num as '_id',w_title,w_img from WishList ",null);
            myCursorAdapter=new MyCursorAdapter(this,cursor);

        } catch (Exception e) {
            // tvtest.setText(e + "");
            e.printStackTrace();
        }
        gv.setAdapter(myCursorAdapter);
        notificationSet=new NotificationSet(this);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sw1.isChecked())
                    notificationSet.notificationSomethings();
                else
                    notificationSet.cancel();

            }
        });


    }
    class MyCursorAdapter extends CursorAdapter {
        @SuppressWarnings("deprecation")
        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.item_layout, parent, false);
            return v;
        }

        public Bitmap getAppIcon(byte[] b){
            return BitmapFactory.decodeByteArray(b,0,b.length);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv=(TextView)view.findViewById(R.id.tv1);
            ImageView img=view.findViewById(R.id.w_img);

            String _title = cursor.getString(cursor.getColumnIndex("w_title"));
            Bitmap bitmap=getAppIcon(cursor.getBlob(cursor.getColumnIndex("w_img")));
            Log.d("testestsetsetset",_title+" "+bitmap.toString());
            tv.setText(_title);
            img.setImageBitmap(bitmap);

        }
    }
    public class NotificationSet {

        SharedPreferences setting;
        SharedPreferences.Editor editor;


        public RemoteViews contentView;
        NotificationManager nm;

        Context context;

        NotificationSet(Context context) {
            this.context = context;
            setting = context.getSharedPreferences("setting", MODE_PRIVATE);
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        public void notificationSomethings() {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            //notificationIntent.putExtra("notificationId", 9999); //전달할 값
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent notifiIntent=new Intent(context,PopupActivity.class);
            notifiIntent.putExtra("test","test");
            notifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifiIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

//            Intent notifiIntent2=new Intent(context,PopupActivity.class);
//            notifiIntent2.putExtra("img",takeScreenshot());
//            notifiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, notifiIntent2,  PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            contentView = new RemoteViews(context.getPackageName(), R.layout.notify);
            contentView.setOnClickPendingIntent(R.id.noti_btn1,pendingIntent);

            //contentView.setOnClickPendingIntent(R.id.noti_btn2,pendingIntent2);

            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContent(contentView)
                    .setContentIntent(contentIntent)
                    .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);
            }
            nm.notify(1234, builder.build());
        }
        public void cancel(){
            nm.cancel(1234);
        }
    }
//    private byte[] takeScreenshot() {
//        byte[] img=null;
//        try {
//            // create bitmap screen capture
//            // 화면 이미지 만들기
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            //bitmap=Bitmap.createScaledBitmap(bitmap,200,400,true);
//            ByteArrayOutputStream stream=new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//            img=stream.toByteArray();
//            v1.setDrawingCacheEnabled(false);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or OOM
//            e.printStackTrace();
//        }
//        return img;
//    }
}
