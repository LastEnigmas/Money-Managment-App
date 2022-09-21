package com.example.ariabank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ariabank.Authentication.RegisterActivity;
import com.example.ariabank.dataBase.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Utils {

    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void addUserToSharedPref(Users user){
        SharedPreferences sharedPreferences= context.getSharedPreferences("Logged_in_user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        Gson gson=new Gson();

        editor.putString("user", gson.toJson(user));
        editor.apply();




    }

    public Users isUserLoggedIn(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("Logged_in_user",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        Type type=new TypeToken<Users>(){}.getType();

        Users user=gson.fromJson(sharedPreferences.getString("user",null),type );

        return user;


    }

    public void signOutUser(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("Logged_in_user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.remove("user");
        editor.commit();
        Intent intent=new Intent(context, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }



}
