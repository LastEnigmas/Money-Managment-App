package com.example.ariabank.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ariabank.MainActivity;
import com.example.ariabank.R;
import com.example.ariabank.Utils;
import com.example.ariabank.WebsiteActivity;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Users;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText edttxtEmail,edttxtPassword;
    private Button btnlogin;
    private TextView txtwarning,txtRegister,txtLicense;
    private AppDataBase db;

    private LoginUser loginuser;
    private DoesEmailexist emailexist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        txtLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, WebsiteActivity.class);
                startActivity(intent);
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin();
            }
        });
    }

    private void initLogin() {
        if(!edttxtEmail.getText().toString().equals("")){
            if (!edttxtPassword.getText().toString().equals("")){
                txtwarning.setVisibility(View.GONE);
                emailexist=new DoesEmailexist();
                emailexist.execute(edttxtEmail.getText().toString());

            }else {
                txtwarning.setVisibility(View.VISIBLE);
                txtwarning.setText("Please enter your password");

            }

        }else {
            txtwarning.setVisibility(View.VISIBLE);
            txtwarning.setText(R.string.plz_entr_email);
        }
    }


    private class DoesEmailexist extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db=AppDataBase.getInstance(LoginActivity.this);

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            List<Users> lst = db.usersdao().getSpecificUser(strings[0]);
            if (lst.size()>0){
                return true;


            }else {
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                loginuser=new LoginUser();
                loginuser.execute();

            }else {
                txtwarning.setVisibility(View.VISIBLE);
                txtwarning.setText(R.string.user_no_good);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(emailexist!=null){
            if(!emailexist.isCancelled()){
                emailexist.cancel(true);
            }
        }

        if(loginuser!=null){
            if(!loginuser.isCancelled()){
                loginuser.cancel(true);
            }
        }
    }

    private class LoginUser extends AsyncTask<Void,Void,Users>{
        private String email;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.email=edttxtEmail.getText().toString();
            this.password=edttxtPassword.getText().toString();

        }

        @Override
        protected Users doInBackground(Void... voids) {
            List<Users> lst=db.usersdao().login(email,password);
            if (lst.size()>0){

                Users user=lst.get(0);
                return user;

            }else {
                return null;

            }



        }

        @Override
        protected void onPostExecute(Users users) {
            super.onPostExecute(users);
            if (users!=null){
                Utils utils=new Utils(LoginActivity.this);
                utils.addUserToSharedPref(users);

                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }else {
                txtwarning.setVisibility(View.VISIBLE);
                txtwarning.setText(R.string.wrong_user);


            }
        }
    }
    private void initViews() {
        edttxtEmail= (EditText) findViewById(R.id.edttxtLoginemail);
        edttxtPassword= (EditText) findViewById(R.id.edttxtloginpass);
        btnlogin= (Button) findViewById(R.id.btnLogin);
        txtwarning= (TextView) findViewById(R.id.LoginWarning);
        txtRegister= (TextView) findViewById(R.id.GotoRegister);
        txtLicense= (TextView) findViewById(R.id.creditLogin);
    }
}