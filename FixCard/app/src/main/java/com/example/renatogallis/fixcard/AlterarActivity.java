package com.example.renatogallis.fixcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.renatogallis.fixcard.ListActivity;
import com.example.renatogallis.fixcard.Model.Livro;
import com.example.renatogallis.fixcard.dao.Scripts_Table_Livros;
import com.example.renatogallis.fixcard.R;
import com.example.renatogallis.fixcard.Utills.Utilitarios;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static android.media.MediaRecorder.VideoSource.CAMERA;


public class AlterarActivity extends AppCompatActivity {
    private ImageView imagem_atual;
    private EditText titulo_atual;
    private EditText autor_atual;
    private String titulo_antigo;
    Utilitarios util = new Utilitarios();
    byte[] imagem = null;
   private Scripts_Table_Livros books_atual = new Scripts_Table_Livros(this);
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        imagem_atual = (ImageView) findViewById(R.id.imagem_hq_alterar);
        titulo_atual = (EditText) findViewById(R.id.titulo_alterar);

        autor_atual = (EditText) findViewById(R.id.autor_alterar);

        if (getIntent() != null) {
            imagem_atual.setImageBitmap(util.byteTobitmap(getIntent().getByteArrayExtra("FOTO")));
            titulo_atual.setText(getIntent().getStringExtra("TITULO"));
            autor_atual.setText(getIntent().getStringExtra("AUTOR"));
            titulo_antigo = getIntent().getStringExtra("TITULO");

        }

    }

    public void foto_alterar(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, CAMERA);
    }

    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.menu_item_share) {

            Intent sendIntent = new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("*/*");
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + titulo_atual.getText().toString()+".jpg");
            try {
                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }

                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(getIntent().getByteArrayExtra("FOTO"));

            } catch (IOException e) {
                e.printStackTrace();
            }

            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/"+titulo_atual.getText().toString()+".jpg"));

            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    //pegar o resultado da foto:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitMap = (Bitmap) extras.get("data");
            imagem_atual.setImageBitmap(imageBitMap);

            //convertendo a image em um array de Bytes para colocar no banco
            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, saida);
            imagem = saida.toByteArray();
        }

    }

    public void Alterar(View view) {
        int verificacao =   books_atual.verificaDuplicidadeCadastro(titulo_atual.getText().toString());

        if (titulo_atual.getText().toString().equals("") || autor_atual.getText().toString().equals("")) {
            Snackbar.make(view, R.string.Menssagem_Erro_Add_Livro, Snackbar.LENGTH_LONG).show();
        }else {
            Livro livro = new Livro();
            livro.setTitulo(titulo_atual.getText().toString());
            livro.setAutor(autor_atual.getText().toString());
            books_atual.alterar_livro(livro, imagem, titulo_antigo);
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {


        savedInstanceState.putByteArray("FOTO_AICIONAR", imagem);
        savedInstanceState.putString("TITULO_ADICIONAR", titulo_atual.getText().toString());
        savedInstanceState.putString("AUTOR_ADICIONAR", autor_atual.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagem = savedInstanceState.getByteArray("FOTO_AICIONAR");
        titulo_atual.setText(savedInstanceState.getString("TITULO_ADICIONAR"));
        autor_atual.setText(savedInstanceState.getString("AUTOR_ADICIONAR"));
        imagem_atual.setImageBitmap(util.byteTobitmap(imagem));
    }
}
