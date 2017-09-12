package com.example.renatogallis.fixcard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.database.DatabaseUtilsCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.renatogallis.fixcard.Model.Livro;
import com.example.renatogallis.fixcard.Utills.Utilitarios;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Renato Gallis on 06/08/2017.
 */

public class Scripts_Table_Livros {
    //Atributos tabela livros:
    private static final String KEY_FOTO = "foto";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_DESCRICAO = "descricao";
    private static final String KEY_ANO_LANCAMENTO = "ano_lancamento";
    private static final String KEY_AUTOR = "autor";
    private static final String KEY_EDITORA = "editora";
    private static final String KEY_SELO = "selo";
    private static final String TABLE_LIVRO = "livros";
    private SQLiteDatabase conn;
    private DataBase_Usuarios db_livros;
    private byte[] imagem;
    private List<Livro> lista = new LinkedList<>();

    public Scripts_Table_Livros(Context context) {
        db_livros = new DataBase_Usuarios(context);
    }

    public SQLiteDatabase returnConection() {
        SQLiteDatabase conn = db_livros.getWritableDatabase();
        return conn;
    }

    public void adicionar_livro(Livro livro, byte[] bytes) {
        ContentValues contentValues = new ContentValues();

        try {
            conn = returnConection();
            contentValues.put(KEY_FOTO, bytes);
            contentValues.put(KEY_TITULO, livro.getTitulo());
            contentValues.put(KEY_DESCRICAO, livro.getDescricao());
            contentValues.put(KEY_AUTOR, livro.getAutor());
            contentValues.put(KEY_EDITORA, livro.getEditora());
            contentValues.put(KEY_SELO, livro.getSelo());

            conn.insert(TABLE_LIVRO, null, contentValues);
            conn.close();
        } catch (SQLException ex1) {
            ex1.getMessage();
        }
    }

    public void alterar_livro(Livro livro, byte[] bytes, String titulo_antigo) {
        ContentValues contentValues = new ContentValues();
        try {
            conn = returnConection();
            if (bytes != null) {
                contentValues.put(KEY_FOTO, bytes);
            }
            contentValues.put(KEY_TITULO, livro.getTitulo());
            contentValues.put(KEY_DESCRICAO, livro.getDescricao());
            contentValues.put(KEY_AUTOR, livro.getAutor());
            contentValues.put(KEY_EDITORA, livro.getEditora());
            contentValues.put(KEY_SELO, livro.getSelo());
            conn.update(TABLE_LIVRO, contentValues, "titulo=?", new String[]{titulo_antigo});
            conn.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }

    }


    public static String getCreateTableLivros() {
        StringBuilder sql_create_table_livros = new StringBuilder();
        sql_create_table_livros.append("CREATE TABLE livros (");
        sql_create_table_livros.append("id             INTEGER  , ");
        sql_create_table_livros.append("foto           BLOB, ");
        sql_create_table_livros.append(" titulo         STRING  NOT NULL  PRIMARY KEY , ");
        sql_create_table_livros.append(" descricao      STRING, ");
        sql_create_table_livros.append(" autor          STRING  NOT NULL, ");
        sql_create_table_livros.append(" editora        STRING, ");
        sql_create_table_livros.append(" selo            STRING ");
        sql_create_table_livros.append(" ); ");


        return sql_create_table_livros.toString();
    }

    public int verificaDuplicidadeCadastro(String nome) {
        conn = returnConection();
        int resultado = (int) DatabaseUtils.queryNumEntries(conn, TABLE_LIVRO, "titulo=?", new String[]{nome});
        conn.close();
        return resultado;
    }

    public List<Livro> retornaTodosLivros() {
        conn = returnConection();
        String query = "SELECT  * FROM " + TABLE_LIVRO;
        Cursor cursor = conn.rawQuery(query, null);
        Livro livro;
        Utilitarios util = new Utilitarios();
        if (cursor.moveToFirst()) {
            do {
                livro = new Livro();
                imagem = cursor.getBlob(1);
                livro.setFoto(util.byteTobitmap(imagem));
                livro.setTitulo(cursor.getString(2));
                livro.setAutor(cursor.getString(4));
                lista.add(livro);
            } while (cursor.moveToNext());
        }
        conn.close();
        return lista;
    }


    public void deletarLivro(String id) {
        try {

            conn = returnConection();
            conn.delete(TABLE_LIVRO, "titulo=?", new String[]{id});
            conn.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }

    }
}
