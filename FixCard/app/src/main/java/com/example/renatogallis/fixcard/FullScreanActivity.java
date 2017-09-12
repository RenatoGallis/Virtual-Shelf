package com.example.renatogallis.fixcard;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.renatogallis.fixcard.API.UsuarioAPI;
import com.example.renatogallis.fixcard.Model.Usuario;
import com.example.renatogallis.fixcard.dao.DataBase_Usuarios;
import com.example.renatogallis.fixcard.dao.Scripts_Table_Usuarios;
import com.example.renatogallis.fixcard.Utills.APIUtils;
import com.facebook.stetho.Stetho;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreanActivity extends AppCompatActivity {
   private final int SPLASH_DISPLAY_LENGTH = 5000;
    private UsuarioAPI usuarioAPI;
    private Usuario usuario;
    Scripts_Table_Usuarios dao = new Scripts_Table_Usuarios(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screan);
        start_stetho();


        if(DataBase_Usuarios.getExistDadosTabela(dao.returnConection(),dao.TABLE_USUARIOS) ==true) {
            carregaDados();
        }
        loading();



    }

   private void  loading(){
       //Passa a configuração de como a animação vai ser executada
       Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
       animation.reset();

       ImageView imageView = (ImageView) findViewById(R.id.splash);
       if(imageView!=null){
           imageView.clearAnimation();
           imageView.startAnimation(animation);
       }
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent = new Intent(FullScreanActivity.this,
                       LoginActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
               startActivity(intent);
               FullScreanActivity.this.finish();
           }
       },SPLASH_DISPLAY_LENGTH);
   }

    private void carregaDados(){
        usuarioAPI = APIUtils.getUsuarioAPI();
        usuarioAPI.getDadosUsuarios().enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    usuario =  response.body();
                    salvar_dados_cadastrais(usuario);

                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }


        });

    }

    public  void salvar_dados_cadastrais(Usuario usuario){

        dao.adicionar_usuario(usuario);
    }

    private void start_stetho() {
        Context context = getBaseContext();
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        // Enable command line interface
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));
        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();
        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

}
