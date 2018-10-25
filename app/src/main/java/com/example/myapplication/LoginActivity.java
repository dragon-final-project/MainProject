package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUserName,etPwd;
    private TextView tvSignUp;
    private Button btnLogin;

    private String userName,pwd;
    private StringBuffer hashPwd;
    private String post_url = "http://140.117.71.66/project/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewId();
    }

    private void findViewId() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(this);

        etUserName = findViewById(R.id.etUserName);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tvSignUp:
                intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                userName = etUserName.getText().toString();
                pwd = etPwd.getText().toString();
                if(userName.length()==0 || pwd.length()==0){
                    Toast.makeText(LoginActivity.this,"請輸入帳號密碼!",Toast.LENGTH_SHORT).show();
                }
                else{
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
                    new SubmitTask().execute(post_url);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(100,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class SubmitTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("userName", userName);
            paramsMap.put("pwd", hashPwd.toString());

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(post_url).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent();
            Gson gson = new Gson();
            AccountData data = gson.fromJson(s,AccountData.class);

            if(data.getLogin_status().equals("successful")){
                Toast.makeText(LoginActivity.this, "登入成功!", Toast.LENGTH_SHORT).show();

                intent.putExtra("isLogin",true);
                intent.putExtra("user_id",data.getUser_id());
                intent.putExtra("name",data.getName());
                intent.putExtra("pic_path",data.getPic_path());
                setResult(100,intent);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this, "登入失敗! 請檢查帳號或密碼是否錯誤!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
