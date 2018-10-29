package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.example.myapplication.CameraFragment.SELECT_PHOTO;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_PHOTO = 2;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    String mCurrentPhotoPath;

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = "http://140.117.71.66:8800/app/upload/";
    private String imagepath=null;

    private Button btnTextInput,btnCameraInput,btnAlbumInput;
    private EditText etSearch;
    private TextView tvTitle,messageText;
    private static int INPUT_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_input);
        setContentView(R.layout.input_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewId();
        Bundle bundle = getIntent().getExtras();
        INPUT_TYPE = bundle.getInt("INPUT_TYPE");

        if(INPUT_TYPE==1){
            tvTitle.setText("吃什麼?");
        }
        else if(INPUT_TYPE==2){
            tvTitle.setText("煮什麼?");
        }
    }

    private void findViewId() {
        btnTextInput = findViewById(R.id.btnTextInput);
        btnCameraInput = findViewById(R.id.btnCameraInput);
        btnAlbumInput = findViewById(R.id.btnAlbumInput);
        btnTextInput.setOnClickListener(this);
        btnCameraInput.setOnClickListener(this);
        btnAlbumInput.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnTextInput:
                final AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
                final View dialogView = LayoutInflater.from(InputActivity.this).inflate(R.layout.text_input_dialog,null);
                Button button = dialogView.findViewById(R.id.btnSearch);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etSearch = dialogView.findViewById(R.id.etSearch);
                        Intent intent = new Intent(InputActivity.this,SearchResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("searchText",etSearch.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setView(dialogView);
                builder.show();
                break;
            case R.id.btnCameraInput:
                dispatchTakePictureIntent();
                break;
            case R.id.btnAlbumInput:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(InputActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(InputActivity.this,
                        "com.example.android.provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "See Food_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == this.RESULT_OK) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
        }
        else if(requestCode == SELECT_PHOTO && resultCode == this.RESULT_OK){
            if (Build.VERSION.SDK_INT >= 23) {
                int hasPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasPermissions != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    return;
                }
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
            final View dialogView = LayoutInflater.from(InputActivity.this).inflate(R.layout.camera_input_dialog,null);
            messageText = dialogView.findViewById(R.id.message_text);

            Button btnUpload = dialogView.findViewById(R.id.btnUpload);
            Button btnSelect = dialogView.findViewById(R.id.btnSelect);
            ImageView ivUpload = dialogView.findViewById(R.id.ivUpload);
            btnUpload.setOnClickListener(new View.OnClickListener() { //上傳圖片
                @Override
                public void onClick(View view) {
                    dialog = ProgressDialog.show(InputActivity.this, "", "Uploading file...", true);
                    messageText.setText("uploading started.....");
                    new Thread(new Runnable() {
                        public void run() {
                            if (Build.VERSION.SDK_INT >= 23) {
                                int hasPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (hasPermissions != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                                    return;
                                }
                            }
                            uploadFile(imagepath);
                        }
                    }).start();
                }
            });

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
                }
            });
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap= BitmapFactory.decodeFile(imagepath);
            ivUpload.setImageBitmap(bitmap);
            builder.setView(dialogView);
            builder.show();
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
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();
            Toast.makeText(InputActivity.this,"123",Toast.LENGTH_SHORT).show();
            Log.e("uploadFile", "Source File not exist :"+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"+ imagepath);
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
//
//                Gson gson = new Gson();
//                DataColumn[] data = gson.fromJson(line,DataColumn[].class);
//                ArrayList<DataColumn> list = new ArrayList<>(Arrays.asList(data));
//                for(DataColumn s : list){
//                    Log.i("name: ",s.getName());
//                    Log.i("ingredients: ",s.getIngredients().toString());
//                }

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "上傳成功!\n點此查看分析結果!";
                            messageText.setText(msg);
                            Toast.makeText(InputActivity.this, "上傳成功!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    messageText.setText("Error");
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
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(InputActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(InputActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}
