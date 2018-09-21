package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private EditText etUserName,etName,etPwd,etPwdCheck,etBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewId();
    }

    private void findViewId() {
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        etUserName = findViewById(R.id.etUserName);
        etName = findViewById(R.id.etName);
        etPwd = findViewById(R.id.etPwd);
        etPwdCheck = findViewById(R.id.etPwdCheck);
        etBirth = findViewById(R.id.etBirth);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()){
            case R.id.btnNext:
                if(etUserName.length()==0 || etPwd.length()==0 || etPwdCheck.length()==0){
                    Toast.makeText(SignUpActivity.this,"*欄位必填!!",Toast.LENGTH_SHORT).show();
                }
                else if(!etPwd.getText().toString().equals(etPwdCheck.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"密碼與密碼確認須一致!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    bundle = new Bundle();
                    bundle.putString("userName",etUserName.getText().toString());
                    bundle.putString("name",etName.getText().toString());
                    bundle.putString("pwd",etPwd.getText().toString());
                    bundle.putString("pwdCheck",etPwdCheck.getText().toString());
                    bundle.putString("birth",etBirth.getText().toString());
                    intent = new Intent(SignUpActivity.this,SignUpFinishActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        }
    }
}
