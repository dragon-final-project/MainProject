package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_PHOTO = 2;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    String mCurrentPhotoPath;

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

//    private String upLoadServerUri = "http://140.117.71.66:8800/app/upload/";
//    private String dynamicIP = "http://140.117.71.66:8800/app/server_url/";
    private String upLoadServerUri = MainActivity.domain+"/model/upload/";
    private String text_Search_url = "http://140.117.71.66/project/get_search_ingredient.php";
    private String imagepath=null;

    private Button btnTextInput,btnIngredientInput;
    private EditText etSearch;
    private TextView tvTitle,messageText;
    private static int INPUT_TYPE;
    private ArrayList<SearchResultData> list;

    Button btnNew,btnDelete;
    LinearLayout ll_in_sv,ll ;
    View buttonView;

    int count=1;
    int i = 0;
    Intent intent2;
    String Name;

    EditText[] ett2 = new EditText[30];
    EditText[] ett3 = new EditText[30];
    String[] str=new String[30];
    String[] str2=new String[30];
    Editable ett2Text,ett3Text;
    ImageButton[] btn_mic = new ImageButton[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
//        setContentView(R.layout.input_drawer);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        findViewId();
        Bundle bundle = getIntent().getExtras();
        INPUT_TYPE = bundle.getInt("INPUT_TYPE");

        if(INPUT_TYPE==1){
            tvTitle.setText("吃什麼?");
        }
        else if(INPUT_TYPE==2){
            tvTitle.setText("煮什麼?");
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FormBody.Builder builder = new FormBody.Builder();
//                OkHttpClient client = new OkHttpClient();
//                try {
//                    RequestBody formBody = builder.build();
//                    Request request = new Request.Builder().url(dynamicIP).post(formBody).build();
//                    final Response response = client.newCall(request).execute();
//
//                    String res =  response.body().string();;
//                    int index = res.lastIndexOf("\"url\":");
//                    upLoadServerUri = res.substring(index+8,res.length()-2);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(InputActivity.this,upLoadServerUri,Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                } catch (IOException e) {
//                    Toast.makeText(InputActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).start();
    }

    private void findViewId() {
        btnTextInput = findViewById(R.id.btnTextInput);
        btnIngredientInput = findViewById(R.id.btnIngredientInput);
        btnTextInput.setOnClickListener(this);
        btnIngredientInput.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
    }

    @Override
    public void onClick(View view) {
        final Intent intent;
        final AlertDialog.Builder builder;
        View dialogView;
        switch (view.getId()){
            case R.id.btnTextInput:
                builder = new AlertDialog.Builder(InputActivity.this);
                dialogView = LayoutInflater.from(InputActivity.this).inflate(R.layout.text_input_dialog,null);
                Button button = dialogView.findViewById(R.id.btnSearch);
                ImageButton btnMic = dialogView.findViewById(R.id.btn_mic);
                etSearch = dialogView.findViewById(R.id.etSearch);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        etSearch = dialogView.findViewById(R.id.etSearch);
                        if(etSearch.getText().length()==0){
                            Toast.makeText(InputActivity.this, "請輸入搜尋食材名稱!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dialog = ProgressDialog.show(InputActivity.this, "", "結果分析中...", true);
//                            messageText.setText("結果分析中.....");
                            new JSONTask().execute(text_Search_url,etSearch.getText().toString());
                        }
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelableArrayList("SearchResultData",list);
//                        bundle.putString("search_type","ingredient");
//                        Intent intent = new Intent(InputActivity.this,SearchResultActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                });

                btnMic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "說說你手邊有的食材吧～");
                        startActivityForResult(intent1,200);
                    }
                });
                builder.setView(dialogView);
                builder.show();
                break;
            case R.id.btnIngredientInput:
                builder = new AlertDialog.Builder(InputActivity.this);
                dialogView = LayoutInflater.from(InputActivity.this).inflate(R.layout.ingredient_input_dialog,null);
                Button btnSearch = dialogView.findViewById(R.id.btnSearch);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);


                ll_in_sv = dialogView.findViewById(R.id.ll_in_sv);
                buttonView = LayoutInflater.from(InputActivity.this).inflate(R.layout.button, null);
                btnNew = buttonView.findViewById(R.id.btn_new);
                btnDelete = buttonView.findViewById(R.id.btn_del);
//                btnBack = findViewById(R.id.btnBack);
                intent2 = this.getIntent();
                Name = intent2.getStringExtra("name");

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ett2[count-1].length()==0){
                            Toast.makeText(InputActivity.this,"搜尋欄位不得為空!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dialog = ProgressDialog.show(InputActivity.this, "", "結果分析中...", true);
                            new JSONTask().execute(text_Search_url,"ingredient_search");
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=0;i<count;i++) str[i] = "";
                        count = 1;
                        addListView();
                        ett2[count-1].setText("");
//                        ett3[count-1].setText("");
                    }
                });

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        for(int i=0;i<count;i++){
                            if(ett2[i].length()!=0){
                                str[i] = ett2[i].getText().toString();
                            }
                        }
                    }
                });

                addListView();
                setActions();

                builder.setView(dialogView);
                builder.show();
                break;
        }
    }

    class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
//                while(progress<20){
//                    progress++;
//                    SystemClock.sleep(20);
//                }
            HashMap<String, String[]> paramsMap = new HashMap<>();
            if(strings[1].equals("ingredient_search")){
                paramsMap.put("ingredient[]",str);
            }
            else{
                paramsMap.put("ingredient",new String[]{strings[1]});
            }

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                for (int i=0;i<paramsMap.get(key).length;i++){
                    String [] s = paramsMap.get(key);
                    if(s[i]!=null){
                        builder.add(key,s[i]);
                    }
                }
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(text_Search_url).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Toast.makeText(InputActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//                dialog = ProgressDialog.show(InputActivity.this, "", "資料載入中...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(s.equals("[]")){
                Toast.makeText(InputActivity.this,"查無食譜!",Toast.LENGTH_LONG).show();
            }
//            Toast.makeText(InputActivity.this,s,Toast.LENGTH_LONG).show();
            else{
                Gson gson = new Gson();
                SearchResultData[] data = gson.fromJson(s,SearchResultData[].class);
                list = new ArrayList<>(Arrays.asList(data));

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("SearchResultData",list);
                bundle.putString("search_type","ingredient");
                Intent intent = new Intent(InputActivity.this,SearchResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    public void addListView(){

        ll_in_sv.removeAllViews();

        //personal資料來源
        for (i = 0; i < count; i++) {

            View view = LayoutInflater.from(InputActivity.this).inflate(R.layout.object3, null); //物件來源
            ll = (LinearLayout) view.findViewById(R.id.ll);
            ett2[i] = (EditText)ll.findViewById(R.id.ett2);
            // editTexts[i].setId(i);
            ett2[i].setText(str[i]);

//            ett3[i] = (EditText)ll.findViewById(R.id.ett3);
//            ett3[i].setText(str2[i]);
            btn_mic[i] = ll.findViewById(R.id.btn_mic);
            btn_mic[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "說說你手邊有的食材吧～");
                    startActivityForResult(intent1,i-1);
                }
            });


            ll_in_sv.addView(view);
        }

        ll_in_sv.addView(buttonView);
    }

    private void setActions() {


        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                for(int i=0;i<count;i++) {
                    ett2Text = ett2[i].getText();
                    str[i] = ett2Text.toString();

//                    ett3Text = ett3[i].getText();
//                    str2[i] = ett3Text.toString();
                }
                if(ett2[count-1].length()==0){
                    Toast.makeText(InputActivity.this,"請先輸入上筆資料後再進行新增!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(count>=4){
                        Toast.makeText(InputActivity.this,"食材查詢欄位至多4項!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        count++;
                        addListView();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (count > 1) {
                    count--;
                    str[count]="";
//                    str2[count]="";
                    addListView();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                etSearch.setText(result.get(0));
            }
        }
        else{
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                ett2[requestCode].setText(result.get(0));
            }
        }
    }
}
