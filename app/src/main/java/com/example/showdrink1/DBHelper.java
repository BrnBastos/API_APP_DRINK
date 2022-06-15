package com.example.showdrink1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_DRINK = "tbl_drink";
    public static final String ID_DRINK = "idDrink";
    public static final String NAME_DRINK = "nomeDrink";
    public static final String INTRUCOES = "instrucoes";
    private static final String DB_NAME = "DBDrink.db";
    private static final String DRINK_IMAGE = "imageDrink";
    private static final int DB_VERSAO = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TBL_DRINK = "CREATE TABLE " + TABLE_DRINK + "( "
                + ID_DRINK + " integer primary key, "
                + INTRUCOES + "text not null, "
                + DRINK_IMAGE + "text not null, "
                + NAME_DRINK + "text not null);";
        db.execSQL(CREATE_TBL_DRINK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String drop_table_bests = "DROP TABLE IF EXISTS " + TABLE_DRINK + ";";
        db.execSQL(drop_table_bests);
    }
}
