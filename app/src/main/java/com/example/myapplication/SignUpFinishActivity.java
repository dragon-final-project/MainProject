package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFinishActivity extends AppCompatActivity implements View.OnClickListener {

    private String userName,name,pwd,pwdCheck,birth;
    private Button btnSubmit;
    private Button btnSelectPic;
    private ImageView ivPic;
    private String post_url = "http://140.117.71.66/project/sign_up.php";
    private StringBuffer hashPwd;
    private static final int SELECT_PHOTO = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    private String upLoadServerUri = "http://140.117.71.66:8800/app/upload_head/";
    private String imagepath = null;
    private String pic_path = "";
    private ProgressDialog dialog = null;
    private int serverResponseCode = 0;

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
        btnSelectPic = findViewById(R.id.btnSelectPic);
        btnSubmit.setOnClickListener(this);
        btnSelectPic.setOnClickListener(this);
        ivPic = findViewById(R.id.ivPic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpFinishActivity.this);
                builder.setTitle("帳號註冊");
                builder.setMessage("即將創建帳戶!\n請確認資料輸入是否正確!");
                builder.setNegativeButton("返回修改",null);
                builder.setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new SubmitTask().execute(post_url);
                    }
                });
                builder.show();
                break;
            case R.id.btnSelectPic:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
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

            if(imagepath!=null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog = ProgressDialog.show(SignUpFinishActivity.this, "", "Uploading file...", true);
                    }
                });
                uploadFile(imagepath);
                paramsMap.put("pic_path",pic_path);
            }

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
            Intent intent = new Intent(SignUpFinishActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SELECT_PHOTO && resultCode == this.RESULT_OK){
            if (Build.VERSION.SDK_INT >= 23) {
                int hasPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasPermissions != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    return;
                }
            }

            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap= BitmapFactory.decodeFile(imagepath);
            ivPic.setImageBitmap(bitmap);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        final String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        final byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();
            Toast.makeText(SignUpFinishActivity.this,"123",Toast.LENGTH_SHORT).show();
            Log.e("uploadFile", "Source File not exist :"+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(SignUpFinishActivity.this,"Source File not exist :"+ imagepath,Toast.LENGTH_LONG).show();
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("image_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                //接收回傳字串
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuilder sb = new StringBuilder();
                String line;
                while((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
                br.close();

                if(serverResponseCode == 200){
                    int index = sb.lastIndexOf("\"head_url\":");
                    pic_path = sb.substring(index+13,sb.length()-3);
                }
                else{
                    Toast.makeText(SignUpFinishActivity.this,"Error",Toast.LENGTH_LONG).show();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUpFinishActivity.this, "MalformedURLException Exception : check script url.", Toast.LENGTH_LONG).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUpFinishActivity.this, "Got Exception : see logcat ", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}
