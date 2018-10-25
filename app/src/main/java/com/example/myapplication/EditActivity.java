package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PHOTO = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private String json_url = "http://140.117.71.66/project/get_account_info.php";
    private String update_url = "http://140.117.71.66/project/update_account_info.php";
    private String upLoadServerUri = "http://140.117.71.66:8800/app/upload_head/";
    private String imagepath = null;
    private String pic_path = "12345";
    private ProgressDialog dialog = null;
    private int serverResponseCode = 0;

    private ImageView ivPic;
    private EditText[] etColumn;
    String[] columnStrings = {"etName","etPwd","etPwdCheck","etBirth"};
    ArrayList<AccountInfo> list;
    private Button btnSelectPic,btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findViewId();
        new SubmitTask().execute(json_url,"get_account_info");
    }

    private void findViewId() {
        ivPic = findViewById(R.id.ivPic);
        etColumn = new EditText[4];
        etColumn[0] = findViewById(R.id.etName);
        etColumn[1] = findViewById(R.id.etPwd);
        etColumn[2] = findViewById(R.id.etPwdCheck);
        etColumn[3] = findViewById(R.id.etBirth);
        btnSelectPic = findViewById(R.id.btnSelectPic);
        btnSave = findViewById(R.id.btnSave);
        btnSelectPic.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnSelectPic:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
                break;
            case R.id.btnSave:
                if(!etColumn[1].getText().toString().equals(etColumn[2].getText().toString())){
                    Toast.makeText(EditActivity.this,"密碼與密碼確認必須一致!", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setTitle("更新個人資料");
                    builder.setMessage("是否確定更新個人資料?");
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new SubmitTask().execute(update_url,"update_account_info");
                            //Toast.makeText(EditActivity.this,"Good!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                break;
        }
    }

    private class SubmitTask extends AsyncTask<String,String,String> {
        private String task;

        @Override
        protected String doInBackground(String... strings) {
            task = strings[1];
            StringBuffer hashPwd;
            final HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("user_id",MainActivity.user_id);

            if(task.equals("update_account_info")){
                for(int i=0;i<etColumn.length;i++){
                    if(etColumn[i].length()!=0){
                        if(i==1){
                            try {  //密碼加密
                                MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                                byte[] hash = sha256.digest(etColumn[1].getText().toString().getBytes());
                                hashPwd = new StringBuffer();

                                for(int j=0;i<hash.length;j++){
                                    String hex = Integer.toHexString(0xff & hash[j]);
                                    if(hex.length() == 1) hashPwd.append('0');
                                    hashPwd.append(hex);
                                }
                                paramsMap.put("etPwd", hashPwd.toString());
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                        paramsMap.put(columnStrings[i],etColumn[i].getText().toString());
                    }
                }
                if(imagepath!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = ProgressDialog.show(EditActivity.this, "", "Uploading file...", true);
                        }
                    });
                    uploadFile(imagepath);
                    paramsMap.put("pic_path",pic_path);
                }
            }

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                final String line = paramsMap.get(key);

                builder.add(key, paramsMap.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(strings[0]).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Toast.makeText(EditActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(task.equals("get_account_info")){
                Intent intent = new Intent();
                Gson gson = new Gson();
                AccountInfo[] data = gson.fromJson(s,AccountInfo[].class);
                list = new ArrayList<>(Arrays.asList(data));

                etColumn[0].setText(list.get(0).getName());
                if(list.get(0).getPic_path().length()!=0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bitmap = GridViewAdapter.getBitmapFromURL(list.get(0).getPic_path());
                            ivPic.post(new Runnable() {
                                @Override
                                public void run() {
                                    ivPic.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();
                }
            }
            else{
                if(s.equals("successful")){
                    Toast.makeText(EditActivity.this,"修改成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(EditActivity.this, "修改失敗!", Toast.LENGTH_SHORT).show();
                }
            }
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
            Toast.makeText(EditActivity.this,"123",Toast.LENGTH_SHORT).show();
            Log.e("uploadFile", "Source File not exist :"+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(EditActivity.this,"Source File not exist :"+ imagepath,Toast.LENGTH_LONG).show();
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

                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                    int index = sb.lastIndexOf("\"head_url\":");
                    pic_path = sb.substring(index+13,sb.length()-3);
                }
                else{
                    Toast.makeText(EditActivity.this,"Error",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(EditActivity.this, "MalformedURLException Exception : check script url.", Toast.LENGTH_LONG).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditActivity.this, "Got Exception : see logcat ", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}
