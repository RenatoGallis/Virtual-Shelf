package com.example.renatogallis.fixcard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.renatogallis.fixcard.Model.Usuario;

/**
 * Created by Renato Gallis on 10/07/2017.
 */

public class Scripts_Table_Usuarios {

    public static final String TABLE_USUARIOS = "Usuarios";
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_SENHA = "senha";
    //Atributos tabela livros:
    private static final String[] COLUNS = {KEY_ID, KEY_NOME, KEY_SENHA};
    private SQLiteDatabase conn;
    private DataBase_Usuarios db_usuarios;

    //// public Scripts_Table_Usuarios(Context) {}

    public Scripts_Table_Usuarios(Context context) {
        db_usuarios = new DataBase_Usuarios(context);
    }


    public SQLiteDatabase returnConection() {
        SQLiteDatabase conn = db_usuarios.getWritableDatabase();
        return conn;
    }


    public void adicionar_usuario(Usuario usuario) {
        ContentValues contentValues = new ContentValues();
        try {
            conn = returnConection();
            contentValues.put(KEY_NOME, usuario.getUsuario());
            contentValues.put(KEY_SENHA, usuario.getSenha());
            conn.insert(TABLE_USUARIOS, null, contentValues);
            conn.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }

    }

    public static String getCreateTableUsuarios() {

        StringBuilder sql_create_table_usuarios = new StringBuilder();

        sql_create_table_usuarios.append("CREATE TABLE Usuarios (");
        sql_create_table_usuarios.append("id    INTEGER PRIMARY KEY  NOT NULL UNIQUE,");
        sql_create_table_usuarios.append("nome  STRING  UNIQUE NOT NULL,");
        sql_create_table_usuarios.append("senha STRING  NOT NULL)");

        return sql_create_table_usuarios.toString();
    }

    public Usuario getUsuario_Cadastrado_App() {

        String query_consulta = "SELECT * FROM" + " " + TABLE_USUARIOS;

        conn = returnConection();

        Cursor cursor = conn.rawQuery(query_consulta, null);

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            do {
                usuario = new Usuario();
                usuario.setUsuario(cursor.getString(1));
                usuario.setSenha(cursor.getString(2));

                // usuarios_Cadastrados.add(usuario);
            } while (cursor.moveToNext());
        }
        return usuario;
    }





}
