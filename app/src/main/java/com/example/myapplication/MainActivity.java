package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.DrawableWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private Button btnMeal,btnIngredient,btnNews,btnAddRecipe;
    private Button btnHeaderLogin,btnHeaderView,btnHeaderEdit,btnHeaderLogout;
    private TextView tvHeaderName;
    private ImageView ivHeaderPic;
    public static boolean isLogin = false;
    private static final int LOGIN_REQUEST_CODE = 100;
    private SharedPreferences sharedPreferences;
    private String dynamicIP = "http://140.117.71.66:8800/app/server_url/";
    public static String domain = "";

    private DrawerLayout drawerLayout;

    public static String user_id;
    public static String name;
    public static String pic_path;

    public static NavigationView navigationView;
    public static View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_login_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connect = (ConnectivityManager)getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();
        if(networkInfo==null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("受限服務");
            builder.setMessage("網路服務未開啟，開啟連線後再嘗試!");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.show();
        }
        else{
            setDynamicIP();
        }
        sharedPreferences = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        readPrefLogin();
        setToolbar();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        headerLayout = navigationView.getHeaderView(0);
//
//        if(isLogin){
//            setLoginInfo();
//        }else{
//            navigationView.removeHeaderView(headerLayout);
//            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main_no_login);
//            Menu menu = navigationView.getMenu();
//            menu.clear();
//
//            btnHeaderLogin = headerLayout.findViewById(R.id.btnHeaderLogin);
//            btnHeaderView = headerLayout.findViewById(R.id.btnHeaderView);
//            btnHeaderLogin.setOnClickListener(this);
//            btnHeaderView.setOnClickListener(this);
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefLogin();
        Menu menu = navigationView.getMenu();
        menu.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("退出應用程式");
            builder.setMessage("確定要退出See Food嗎?");
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void findViewId() {
        btnMeal = findViewById(R.id.btnMeal);
        btnIngredient = findViewById(R.id.btnIngredient);
        btnNews = findViewById(R.id.btnNews);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnMeal.setOnClickListener(this);
        btnIngredient.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnAddRecipe.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent;

        if (id == R.id.nav_camera) {
            //fragment = new CameraFragment();
            intent = new Intent(MainActivity.this,GeOneActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_album) {

        } else if (id == R.id.nav_map) {
//            fragment = new MapFragment();
            intent = new Intent(MainActivity.this,MyFacoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_ingredient) {

        } else if (id == R.id.nav_meal) {

        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()){
            case R.id.btnMeal:
                intent = new Intent(MainActivity.this,InputpictureActivity.class);
                bundle = new Bundle();
                bundle.putInt("INPUT_TYPE",1); //input type = 1 是查詢餐點
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnIngredient:
                intent = new Intent(MainActivity.this,InputActivity.class);
                bundle = new Bundle();
                bundle.putInt("INPUT_TYPE",2); //input type = 2 是查詢食材
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnNews:
                intent = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAddRecipe:
                if(isLogin){
                    intent = new Intent(MainActivity.this,AddRecipeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"請先登入再進行此功能!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnHeaderLogin:
                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,LOGIN_REQUEST_CODE);
                //startActivity(intent);
                break;
            case R.id.btnHeaderView:
                intent = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnHeaderEdit:
                intent = new Intent(MainActivity.this,EditActivity.class);
                startActivity(intent);
                break;
            case R.id.btnHeaderLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("登出系統");
                builder.setMessage("確定登出系統?");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isLogin = false;
                        savePrefLogin();
                        onResume();
                    }
                });
                builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        if(requestCode==requestCode){
            isLogin = data.getBooleanExtra("isLogin",false);
            name = data.getStringExtra("name");
            user_id = data.getStringExtra("user_id");
            pic_path = data.getStringExtra("pic_path");
            savePrefLogin();
            readPrefLogin();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void readPrefLogin(){
        isLogin = sharedPreferences.getBoolean("isLogin",false);
        user_id = sharedPreferences.getString("user_id","");
//        String email = sharedPreferences.getString("name","");
//        String pwd = sharedPreferences.getString("pwd","");
        name = sharedPreferences.getString("name","");
        pic_path = sharedPreferences.getString("pic_path","");
//        String time = sharedPreferences.getString("time","");
    }

    public void savePrefLogin(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",isLogin);
        editor.putString("user_id",user_id);
//        editor.putString("email",email);
//        editor.putString("pwd",user_id);
        editor.putString("name",name);
        editor.putString("pic_path",pic_path);
//        editor.putString("time",time);

        editor.commit();
    }

    public void setLoginInfo(){
        navigationView.removeHeaderView(headerLayout);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        btnHeaderEdit = headerLayout.findViewById(R.id.btnHeaderEdit);
        btnHeaderLogout = headerLayout.findViewById(R.id.btnHeaderLogout);
        tvHeaderName = headerLayout.findViewById(R.id.tvHeaderName);
        tvHeaderName.setText(name);
        ivHeaderPic = headerLayout.findViewById(R.id.ivHeaderPic);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = GridViewAdapter.getBitmapFromURL(pic_path);
                ivHeaderPic.post(new Runnable() {
                    @Override
                    public void run() {
                        ivHeaderPic.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        btnHeaderEdit.setOnClickListener(this);
        btnHeaderLogout.setOnClickListener(this);
    }

    public void setToolbar(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);

        if(isLogin){
            setLoginInfo();
        }else{
            navigationView.removeHeaderView(headerLayout);
            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main_no_login);
            Menu menu = navigationView.getMenu();
            menu.clear();

            btnHeaderLogin = headerLayout.findViewById(R.id.btnHeaderLogin);
            btnHeaderView = headerLayout.findViewById(R.id.btnHeaderView);
            btnHeaderLogin.setOnClickListener(this);
            btnHeaderView.setOnClickListener(this);
        }
    }

    public void setDynamicIP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                OkHttpClient client = new OkHttpClient();
                try {
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder().url(dynamicIP).post(formBody).build();
                    final Response response = client.newCall(request).execute();

                    String res =  response.body().string();;
                    int index = res.lastIndexOf("\"url\":");
                    domain = res.substring(index+8,res.length()-2);

                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}
