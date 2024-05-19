package com.yt.smartmarket.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.yt.smartmarket.R;
import com.yt.smartmarket.db.DBHelper;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        Button btnLogin = findViewById(R.id.btn_login_login);
        EditText etId = findViewById(R.id.et_login_id);
        EditText etPasswd = findViewById(R.id.et_login_password);
        TextView tvNotHaveAccount = findViewById(R.id.tv_login_signup);
        CheckBox cbRemember = findViewById(R.id.cb_login_remember);
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        // 加载保存的用户名和密码
        SharedPreferences preferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        etId.setText(preferences.getString("username", ""));
        etPasswd.setText(preferences.getString("password", ""));
        cbRemember.setChecked(preferences.getBoolean("remember", false));
        tvNotHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            String pwd = toMD5(etPasswd.getText().toString().trim());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Name = ?", new String[]{etId.getText().toString()});
            if (cursor.moveToFirst()) {
                String realPwd = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
                if (pwd.equals(realPwd)) {
                    if (cbRemember.isChecked()) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", etId.getText().toString());
                        editor.putString("password", etPasswd.getText().toString());
                        editor.putBoolean("remember", true);
                        editor.apply();
                    } else {
                        preferences.edit().clear().apply();
                    }

                    // 处理用户的首次登录和正常登录
                    if (cursor.getString(cursor.getColumnIndexOrThrow("IsFirstLogin")).equals("1")) {
                        showSnackBar(v, "您是第一次登录，请完成信息补充", "好");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                            String currentUserName = etId.getText().toString().trim();// 获取用户名
                            intent.putExtra("USERNAME",currentUserName);// 传入Intent中
                            startActivity(intent);
                            finish();
                        }, 2000);
                    } else {
                        showSnackBar(v, "登录成功，正在跳转至主页……", "好");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }, 2000);
                    }
                } else {
                    showSnackBar(v, "账号或密码错误！", "好");
                }
            }
        });

    }

    // MD5转换类
    public String toMD5(@NonNull String password) {
        try {
            // 创建MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();

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
    public void showSnackBar(View view,String title,String button){
        Snackbar snackbar = Snackbar.make(view,title,Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(R.color.purple2))
                .setAction(button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });
        snackbar.show();
    }
}
