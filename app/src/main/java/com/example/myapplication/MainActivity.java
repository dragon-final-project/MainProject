package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.DrawableWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private Button btnMeal,btnIngredient,btnNews,btnAddRecipe;
    private Button btnHeaderLogin,btnHeaderView,btnHeaderEdit,btnHeaderLogout;
    private TextView tvHeaderName;
    private boolean isLogin = false;
    private static final int LOGIN_REQUEST_CODE = 100;

    private DrawerLayout drawerLayout;

    public static String user_id;
    private static String name;

    public static NavigationView navigationView;
    public static View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_login_main);

//        if(isLogin) {
//            setContentView(R.layout.activity_main);
//        }
//        else{
//            setContentView(R.layout.no_login_main);
//        }
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

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        View headerLayout = navigationView.getHeaderView(0);
//        if(isLogin){
//            btnHeaderEdit = headerLayout.findViewById(R.id.btnHeaderEdit);
//            btnHeaderEdit.setOnClickListener(this);
//        }else{
//
//            btnHeaderLogin = headerLayout.findViewById(R.id.btnHeaderLogin);
//            btnHeaderView = headerLayout.findViewById(R.id.btnHeaderView);
//            btnHeaderLogin.setOnClickListener(this);
//            btnHeaderView.setOnClickListener(this);
//        }

        findViewId();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.getHeaderView(0);
        headerLayout = navigationView.getHeaderView(0);

//        Menu menuLayout = navigationView.getMenu();

        if(isLogin){
//            navigationView.removeHeaderView(headerLayout);
//            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
//            navigationView.inflateMenu(R.menu.activity_main_drawer);
//            btnHeaderEdit = headerLayout.findViewById(R.id.btnHeaderEdit);
//            btnHeaderLogout = headerLayout.findViewById(R.id.btnHeaderLogout);
//            tvHeaderName = headerLayout.findViewById(R.id.tvHeaderName);
//            tvHeaderName.setText(name);
//            btnHeaderEdit.setOnClickListener(this);
//            btnHeaderLogout.setOnClickListener(this);
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
            fragment = new CameraFragment();
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
                intent = new Intent(MainActivity.this,InputActivity.class);
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
                intent = new Intent(MainActivity.this,AddRecipeActivity.class);
                startActivity(intent);
                break;
            case R.id.btnHeaderLogin:
                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,LOGIN_REQUEST_CODE);
                break;
            case R.id.btnHeaderView:
                intent = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnHeaderEdit:
                Toast.makeText(MainActivity.this,"Edit",Toast.LENGTH_SHORT).show();
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

            if(isLogin){
            navigationView.removeHeaderView(headerLayout);
            headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            btnHeaderEdit = headerLayout.findViewById(R.id.btnHeaderEdit);
            btnHeaderLogout = headerLayout.findViewById(R.id.btnHeaderLogout);
            tvHeaderName = headerLayout.findViewById(R.id.tvHeaderName);
            tvHeaderName.setText(name);
            btnHeaderEdit.setOnClickListener(this);
            btnHeaderLogout.setOnClickListener(this);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
