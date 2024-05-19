package com.yt.smartmarket.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.yt.smartmarket.Net.PicUpload;
import com.yt.smartmarket.R;
import com.yt.smartmarket.db.DBHelper;

public class TestActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private EditText etUsername;
    private TextView tvUserInfo;
    private ImageView ivAvatar;
    String imagePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // 请求权限
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);
        etUsername = findViewById(R.id.et_test_user);
        Button btnGetInfo = findViewById(R.id.btn_test_get);
        Button btnSelect = findViewById(R.id.btn_test_select);
        tvUserInfo = findViewById(R.id.tv_test_get);
        ivAvatar = findViewById(R.id.iv_test_avatar);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        btnGetInfo.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor =  db.rawQuery("SELECT * FROM Users WHERE Name = ?",new String[]{etUsername.getText().toString()});
            if(cursor.moveToFirst()){
                String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                String pwd = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
                String avatar = cursor.getString(cursor.getColumnIndexOrThrow("Avatar"));
                tvUserInfo.append(email+"\n"+pwd+"\n");
                Glide
                        .with(this)
                        .load(avatar)
                        .transform(new CircleCrop())
                        .into(ivAvatar);
                cursor.close();
            }
        });
        // 选择图片按钮单击事件
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            assert data != null;
            Uri uri = data.getData();
            imagePath = getPathFromUri(uri);
            // 选择图片后立即上传
            PicUpload upload = new PicUpload();
            upload.uploadImage(imagePath);
        }
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    // 选择图片
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }
}
