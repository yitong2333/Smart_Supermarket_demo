package com.yt.smartmarket.Activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.yt.smartmarket.R;
import com.yt.smartmarket.db.DBHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    /** @noinspection resource*/
    private void initView() {
        Button btnSign = findViewById(R.id.btn_signup_signup);
        EditText etName = findViewById(R.id.et_signup_id);
        EditText etEmail = findViewById(R.id.et_signup_email);
        EditText etPwd = findViewById(R.id.et_signup_password);
        TextView tvAlreadyHave = findViewById(R.id.tv_signup_login);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        tvAlreadyHave.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        });
        btnSign.setOnClickListener(v -> {
            //获取输入的数据
            String username = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pwd = toMD5(etPwd.getText().toString().trim());//用md5加密，避免明文存储密码
            String avatarUrl = getString(R.string.defaultAvatar);//默认头像
            if (etName.getText().toString().trim().isEmpty()){
                showSnackBar(v,"用户名不能为空！","好");
            } else if (etEmail.getText().toString().trim().isEmpty()) {
                showSnackBar(v,"邮箱不能为空！","好");
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher((etEmail.getText().toString().trim())).matches()){
                showSnackBar(v,"邮箱格式不正确！","好");
            } else if (etPwd.getText().toString().trim().isEmpty()) {
                showSnackBar(v,"密码不能为空！","好");
            }
            else{
                String isSuccess = dbHelper.userRegister(db,username,pwd,email,avatarUrl);
                if (isSuccess.equals("注册成功")){
                    // 注册成功
                    showSnackBar(v,isSuccess,"好");
                    // 使用线程延时2秒执行跳转操作
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }, 2000);
                }
                else {
                    // 注册失败
                    showSnackBar(v,isSuccess,"好");
                }
            }
        });
    }
    public void showSnackBar(View view,String title,String button){
        Snackbar snackbar = Snackbar.make(view,title,Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(R.color.purple2))
                .setAction(button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });
        snackbar.show();
    }
    // MD5转换类
    public String toMD5(String password) {
        try {
            // 创建MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            // 创建十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
