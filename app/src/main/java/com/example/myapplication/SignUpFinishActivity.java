package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFinishActivity extends AppCompatActivity implements View.OnClickListener {

    private String userName,name,pwd,pwdCheck,birth;
    private Button btnSubmit;
    private String post_url = "http://140.117.71.66/project/sign_up.php";
    private StringBuffer hashPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_finish);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        name = bundle.getString("name");
        pwd = bundle.getString("pwd");
        pwdCheck = bundle.getString("pwdCheck");
        birth = bundle.getString("birth");

        try {  //密碼加密
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(pwd.getBytes());
            hashPwd = new StringBuffer();

            for(int i=0;i<hash.length;i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hashPwd.append('0');
                hashPwd.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        findViewId();
    }

    private void findViewId() {
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                new SubmitTask().execute(post_url);
                break;
        }
    }

    private class SubmitTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> paramsMap = new HashMap<>();
            paramsMap.put("userName",userName);
            paramsMap.put("name",name);
            paramsMap.put("pwd",hashPwd.toString());
            paramsMap.put("pwdCheck",pwdCheck);
            paramsMap.put("birth",birth);
            FormBody.Builder builder = new FormBody.Builder();
            for(String key:paramsMap.keySet()){
                builder.add(key,paramsMap.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(post_url).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
//                Call call = client.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Toast.makeText(SignUpFinishActivity.this,"網路連線錯誤!",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Toast.makeText(SignUpFinishActivity.this,""+response.body().string(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Response response = client.newCall(request).execute();
//                return response.body().string();
            }catch (IOException e){
                Toast.makeText(SignUpFinishActivity.this,"網路連線錯誤!",Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(SignUpFinishActivity.this,"註冊成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpFinishActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
