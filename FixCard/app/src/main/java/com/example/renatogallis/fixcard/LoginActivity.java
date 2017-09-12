package com.example.renatogallis.fixcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.renatogallis.fixcard.Model.Usuario;
import com.example.renatogallis.fixcard.dao.Scripts_Table_Usuarios;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.stetho.Stetho;


public class LoginActivity extends AppCompatActivity {


    Scripts_Table_Usuarios consult = new Scripts_Table_Usuarios(this);
    private EditText edtNome;
    private EditText edtSenha;
    private CheckBox chkConectado;
    private LoginButton fb;
    private Usuario usuario;
    private CallbackManager callbackManager;
    private final String KEY_APP_PREFERENCES = "login";
    private LoginButton facebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        facebook = (LoginButton) findViewById(R.id.btn_login_facebook);
        edtNome = (EditText) findViewById(R.id.tilNome);
        edtSenha = (EditText) findViewById(R.id.tilSenha);
        chkConectado = (CheckBox) findViewById(R.id.ckConectado);
        usuario = consult.getUsuario_Cadastrado_App();


        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                IniciaApp();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "ocorreu erro3",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "ocorreu erro",Toast.LENGTH_LONG);

            }
        });


        if (isConect())
            IniciaApp();


    }

    public void login(View v) {

        if (Verifica_Autenticacao() == true) {
            if (chkConectado.isChecked()) {
                keepConnected();
            }
            IniciaApp();
            // intent para a 1 tela do app
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.Toast_Error_Login, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public boolean Verifica_Autenticacao() {
        return usuario.getUsuario()
                .equals(edtNome.getText()
                        .toString()) && usuario.getSenha()
                .equals(edtSenha.getText().toString());
    }

    private boolean isConect() {
        AccessToken status_facebook = AccessToken.getCurrentAccessToken();
        SharedPreferences sp = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        String nome = sp.getString("nome", "");
        String senha = sp.getString("senha", "");

        LoginManager.getInstance().getLoginBehavior();
        if ((nome.equals("") && senha.equals("")) && status_facebook == null)
            return false;
        else {
            return true;
        }
    }

    private void keepConnected() {
        SharedPreferences sp = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("nome", edtNome.getText().toString());
        e.putString("senha", edtSenha.getText().toString());
        e.putBoolean("chkConectado", chkConectado.isChecked());
        e.apply();
    }

    private void IniciaApp() {
        startActivity(new Intent(this, PrincipalActivit.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
