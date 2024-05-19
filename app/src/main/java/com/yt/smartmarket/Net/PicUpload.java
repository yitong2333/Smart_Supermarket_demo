package com.yt.smartmarket.Net;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PicUpload {

    // 定义上传图片的方法
    public void uploadImage(String imagePath) {
        OkHttpClient client = new OkHttpClient();

        // 设置图片文件
        File file = new File(imagePath);

        // 设置请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("smfile", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .addFormDataPart("format", "json")
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url("https://sm.ms/api/v2/upload")
                .header("Authorization", "XDdA4WbVniZlUmi21ilFYWwp7oroCDNS") // 替换为实际的 Authorization token
                .post(requestBody)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // 获取响应数据
                assert response.body() != null;
                String responseData = response.body().string();

                // 处理响应数据
                // 这里可以解析 responseData，获取上传结果等信息
                System.out.println(responseData);
            }
        });
    }
}