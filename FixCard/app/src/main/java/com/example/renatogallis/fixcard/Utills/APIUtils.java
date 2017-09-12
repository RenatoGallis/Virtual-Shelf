package com.example.renatogallis.fixcard.Utills;


import com.example.renatogallis.fixcard.API.LocalAPI;
import com.example.renatogallis.fixcard.API.UsuarioAPI;
import com.example.renatogallis.fixcard.Client.RetrofitClient;

public class APIUtils {

    public static final String URL_LOCATION = "https://maps.googleapis.com/maps/";
    public static final String URL = "http://www.mocky.io";

    public static UsuarioAPI getUsuarioAPI(){
        return RetrofitClient.getClient(URL).create(UsuarioAPI.class);
    }

    public static LocalAPI getLocationAPI(){
        return RetrofitClient.getClient(URL_LOCATION).create(LocalAPI.class);
    }
}
