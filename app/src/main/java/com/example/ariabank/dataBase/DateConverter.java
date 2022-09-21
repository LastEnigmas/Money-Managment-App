package com.example.ariabank.dataBase;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public String dateToString(Date date){
        Gson gson=new Gson();


        return gson.toJson(date);

    }

    @TypeConverter
    public Date StringToDate(String json){
        Gson gson=new Gson();
        Type type=new TypeToken<Date>(){}.getType();
        return gson.fromJson(json,type);

    }

}
