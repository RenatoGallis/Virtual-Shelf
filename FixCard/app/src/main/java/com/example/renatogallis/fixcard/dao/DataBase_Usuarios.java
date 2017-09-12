package com.example.renatogallis.fixcard.dao;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBase_Usuarios extends SQLiteOpenHelper {
    public static final int VERSION = 5;

    public DataBase_Usuarios(Context context) {
        super(context, "Usuarios", null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase query) {
        query.execSQL(Scripts_Table_Usuarios.getCreateTableUsuarios());
        query.execSQL(Scripts_Table_Livros.getCreateTableLivros());
    }

    @Override
    public void onUpgrade(SQLiteDatabase query, int i, int i1) {
        query.execSQL("DROP TABLE IF EXISTS Usuarios");
        query.execSQL("DROP TABLE IF EXISTS livros");
        onCreate(query);

    }

    public static boolean getExistDadosTabela(SQLiteDatabase db, String table) {

        return DatabaseUtils.queryNumEntries(db, table) == 0;

    }


}
