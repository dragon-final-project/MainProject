package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddIngredientActivity extends AppCompatActivity {

    Button
            btnNew,btnDelect,btnBack;

    LinearLayout
            ll_in_sv,ll ;

    View
            buttonView;

    int count=1;
    Intent intent2;
    String Name;

    EditText[] ett2 = new EditText[30];
    EditText[] ett3 = new EditText[30];
    String[] str=new String[30];
    String[] str2=new String[30];
    Editable ett2Text,ett3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        buttonView = LayoutInflater.from(AddIngredientActivity.this).inflate(R.layout.button, null);
        ll_in_sv = (LinearLayout)findViewById(R.id.ll_in_sv);
        btnNew = (Button)buttonView.findViewById(R.id.btn_new);
        btnDelect = (Button)buttonView.findViewById(R.id.btn_del);
        btnBack = findViewById(R.id.btnBack);
        intent2 = this.getIntent();
        Name = intent2.getStringExtra("name");

        addListView();
        setActions();
    }

    public void addListView(){

        ll_in_sv.removeAllViews();


        //personal資料來源
        for (int i = 0; i < count; i++) {


            View view = LayoutInflater.from(AddIngredientActivity.this).inflate(R.layout.object, null); //物件來源
            ll = (LinearLayout) view.findViewById(R.id.ll);
            ett2[i] = (EditText)ll.findViewById(R.id.ett2);
            // editTexts[i].setId(i);
            ett2[i].setText(str[i]);

            ett3[i] = (EditText)ll.findViewById(R.id.ett3);
            ett3[i].setText(str2[i]);



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

                    ett3Text = ett3[i].getText();
                    str2[i] = ett3Text.toString();
                }
                if(ett2[count-1].length()==0 || ett3[count-1].length()==0){
                    Toast.makeText(AddIngredientActivity.this,"請先輸入上筆資料後再進行新增!",Toast.LENGTH_SHORT).show();
                }
                else{
                    count++;
                    addListView();
                }
            }
        });

        btnDelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (count > 1) {
                    count--;
                    addListView();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddIngredientActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    public void Nextpage(View v){


        ett2Text = ett2[(count-1)].getText();
        str[(count-1)] = ett2Text.toString();

        ett3Text = ett3[(count-1)].getText();
        str2[(count-1)] = ett3Text.toString();

        if(ett2[count-1].length()==0 ||ett3[count-1].length()==0){
            Toast.makeText(AddIngredientActivity.this,"請輸入食材名稱與數量!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, AddInstructionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("n",count);
            bundle.putStringArray("Food",str);
            bundle.putStringArray("Number",str2);
            bundle.putString("Name",Name);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
