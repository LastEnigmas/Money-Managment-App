package com.example.ariabank.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ariabank.MainActivity;
import com.example.ariabank.R;
import com.example.ariabank.Utils;
import com.example.ariabank.WebsiteActivity;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Users;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText edttxtEmail,edttxtPassword,edttxtAddress,edttxtName;
    private TextView txtWarning,txtLogin,txtLicense;
    private Button btnRegister;
    private AppDataBase appdatabase;
    private ImageView firstImageView,secondImageView;
    private String image_url;

    private DoesUserExist doesUserExist;
    private RegistertheUser registertheUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initviews();
        secondImageView.setBackgroundColor(getResources().getColor(R.color.white));
        firstImageView.setBackgroundColor(getResources().getColor(R.color.white));
        appdatabase=AppDataBase.getInstance(RegisterActivity.this);
        image_url="first";
        handleimages();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRegister();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        txtLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(RegisterActivity.this, WebsiteActivity.class);
            startActivity(intent);
            }
        });
    }

    private void handleimages() {
        firstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstImageView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.imageview_background,null));
                secondImageView.setBackgroundColor(getResources().getColor(R.color.white));
                image_url="first";
            }
        });

        secondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondImageView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.imageview_background,null));
                firstImageView.setBackgroundColor(getResources().getColor(R.color.white));
                image_url="second";
            }
        });

    }

    private void initRegister() {

        String email=edttxtEmail.getText().toString();
        String password=edttxtPassword.getText().toString();

        if (email.equals("") || password.equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText(R.string.enter_warning);
        }else {
            txtWarning.setVisibility(View.GONE);
            doesUserExist=new DoesUserExist();
            doesUserExist.execute(email);
        }
    }

    private void initviews() {
        edttxtEmail= (EditText) findViewById(R.id.edttxtemail);
        edttxtPassword= (EditText) findViewById(R.id.edttxtpassword);
        edttxtAddress= (EditText) findViewById(R.id.edttxtAddress);
        edttxtName= (EditText) findViewById(R.id.edttxtName);

        txtWarning= (TextView) findViewById(R.id.txtviewWarning);
        txtLogin= (TextView) findViewById(R.id.txtLogin);
        txtLicense= (TextView) findViewById(R.id.credit);
        btnRegister= (Button) findViewById(R.id.btnRegister);

        firstImageView= (ImageView) findViewById(R.id.firstimg);
        secondImageView= (ImageView) findViewById(R.id.secondimg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=doesUserExist){
            if(!doesUserExist.isCancelled()){
                doesUserExist.cancel(true);
            }
        }

        if(null!=registertheUser){
            if(!registertheUser.isCancelled()){
                registertheUser.cancel(true);
            }
        }
    }

    private class DoesUserExist extends AsyncTask<String,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... strings) {

            List<Users> lst=null;
            lst = appdatabase.usersdao().getSpecificUser(strings[0]);
            if(lst==null || lst.size()==0){

                return false;
            }
            else {

                return true;
            }



        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean==true){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText(R.string.exist_user);
                edttxtEmail.setText("");
            }else {
                txtWarning.setVisibility(View.GONE);
                registertheUser=new RegistertheUser();
                registertheUser.execute();
            }
        }
    }
    private class RegistertheUser extends AsyncTask<Void,Void,Users>{
        private String email;
        private String password;
        private String address;
        private String first_name;
        private String last_name;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email=edttxtEmail.getText().toString();
            address=edttxtAddress.getText().toString();
            password=edttxtPassword.getText().toString();

            String[] names=edttxtName.getText().toString().split(" ");
            if(names.length>=1){
                this.first_name=names[0];
                for (int i=1;i< names.length;i++){
                    if(i>1){
                        last_name+= " "+names[i];
                    }else {
                        last_name +=names[i];
                    }
                }

            }else {
                this.first_name=names[0];
            }


        }

        @Override
        protected Users doInBackground(Void... voids) {
            Users user=new Users(email,password,first_name,last_name,address,image_url);
            appdatabase.usersdao().addUser(user);
            appdatabase.close();
            return user;


        }

        @Override
        protected void onPostExecute(Users users) {
            super.onPostExecute(users);
            if(users!=null){
                Toast.makeText(RegisterActivity.this, "User "+users.getEmail()+" registered successfully.", Toast.LENGTH_SHORT).show();
                Utils utils=new Utils(RegisterActivity.this);
                utils.addUserToSharedPref(users);
                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(RegisterActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

            }

        }
    }
}