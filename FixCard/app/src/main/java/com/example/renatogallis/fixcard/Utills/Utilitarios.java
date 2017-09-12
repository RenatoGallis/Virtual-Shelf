package com.example.renatogallis.fixcard.Utills;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Renato Gallis on 22/08/2017.
 */

public class Utilitarios {




    public Utilitarios() {}

    public byte[] BitMaptoByteArray(Bitmap imageBitMap) {
        //convertendo a image em um array de Bytes para colocar no banco
        byte[] imagem;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, saida);
        return imagem = saida.toByteArray();
    }

    public Bitmap byteTobitmap(byte[] imagem) {
        Bitmap imagebtm = null;
        try {
            ByteArrayInputStream imagemStream = new ByteArrayInputStream(imagem);
            imagebtm = BitmapFactory.decodeStream(imagemStream);
            return imagebtm;
        } catch (Exception e) {
            e.getMessage();
        }
        return imagebtm;
    }

}
