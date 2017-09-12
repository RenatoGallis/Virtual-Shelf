package com.example.renatogallis.fixcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.renatogallis.fixcard.R;

public class Abaut extends AppCompatActivity {
private String N_TELEFONE = "26238227";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abaut);
    }

    public  void ligar(View view){

        realiza_ligacao();
    }

    public  void realiza_ligacao(){
        Uri uri = Uri.parse("tel:" + N_TELEFONE);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
    
}
