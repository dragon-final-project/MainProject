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

public class AddInstructionActivity extends AppCompatActivity {

    TextView
            Step;
    Button
            btnNew,btnDelect;

    LinearLayout
            lll_in_sv ;

    View
            buttonView;

    String food,number,Name;
    int count=1,n;
    String Food[]=new String[30];
    String Number[]=new String[30];
    EditText[] ett4 = new EditText[10];
    String[] str=new String[10];
    Editable ett4Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instruction);

        buttonView = LayoutInflater.from(AddInstructionActivity.this).inflate(R.layout.button, null);
        lll_in_sv = (LinearLayout)findViewById(R.id.lll_in_sv);
        //btnNext = (Button)findViewById(R.id.btn_next);
        btnNew = (Button)buttonView.findViewById(R.id.btn_new);
        btnDelect = (Button)buttonView.findViewById(R.id.btn_del);

        Bundle bundle = this.getIntent().getExtras();
        Food = bundle.getStringArray("Food");
        n=bundle.getInt("n");
        Number = bundle.getStringArray("Number");
        Name = bundle.getString("Name");

        addListView();
        setActions();
    }

    public void addListView(){

        lll_in_sv.removeAllViews();

        //personal資料來源
        for (int i = 0; i < count; i++) {


            View view = LayoutInflater.from(AddInstructionActivity.this).inflate(R.layout.object2, null); //物件來源
            LinearLayout lll = (LinearLayout) view.findViewById(R.id.lll);
            Step = (TextView) lll.findViewById(R.id.Step); //獲取LinearLayout中各元件
            Step.setText("Step:"+(i+1));

            ett4[i] = (EditText) lll.findViewById(R.id.ett4); //獲取LinearLayout中各元件
            food=Food[i];
            number=Number[i];
            ett4[i].setText(str[i]);



            lll_in_sv.addView(view);
        }
        lll_in_sv.addView(buttonView);
    }

    private void setActions() {

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<count;i++) {
                    ett4Text = ett4[i].getText();
                    str[i] = ett4Text.toString();


                }
                count++;
                addListView();
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


    }
    public void Nextpage(View v){

        ett4Text = ett4[(count-1)].getText();
        str[(count-1)] = ett4Text.toString();

        //Intent show = new Intent(this, AddRecipeResultActivity.class);
        Intent show = new Intent(this, resultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("n",n);
        bundle.putInt("m",count);
        bundle.putStringArray("Food",Food);
        bundle.putStringArray("Number",Number);
        bundle.putStringArray("Method",str);
        bundle.putString("Name",Name);
        show.putExtras(bundle);
        startActivity(show);

    }
}
