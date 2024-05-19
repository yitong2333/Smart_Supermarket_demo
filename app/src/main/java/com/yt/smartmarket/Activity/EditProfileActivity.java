package com.yt.smartmarket.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yt.smartmarket.R;
import com.yt.smartmarket.db.DBHelper;

public class EditProfileActivity extends AppCompatActivity {
    TextView tvName;
    EditText etEditEmail,etEditPhone,etEditAddress,etEditPass;
    ImageView ivAvatar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        ivAvatar = findViewById(R.id.iv_edit_avatar);
        FloatingActionButton fabEditEdit = findViewById(R.id.flb_edit_edit);
        tvName = findViewById(R.id.tv_edit_name);
        etEditEmail = findViewById(R.id.et_edit_email);
        etEditPhone = findViewById(R.id.et_edit_phone);
        etEditAddress = findViewById(R.id.et_edit_address);
        etEditPass = findViewById(R.id.et_edit_pass);
        Button btnSave = findViewById(R.id.btn_edit_save);
        initData();

        // 编辑头像按钮事件
        fabEditEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void initData() {
        // 接收上一个页面传来的USERNAME等数据，并显示在界面上
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        tvName.setText(username);
        SQLiteDatabase db = new DBHelper(getApplicationContext()).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Name = ?",new String[]{username});
        if (cursor.moveToFirst()){
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("Addr"));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow("Avatar"));
            etEditEmail.setText(email);
            etEditPhone.setText(phone);
            etEditAddress.setText(address);
            Glide
                    .with(this)
                    .load(avatar)
                    .transform(new CircleCrop())
                    .into(ivAvatar);
            cursor.close();
        }
    }
}
