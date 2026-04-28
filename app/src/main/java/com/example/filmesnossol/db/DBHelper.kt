package com.example.filmesnossol.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "filmes_nossol.db"
        const val DATABASE_VERSION = 2

        const val TABELA_FILMES = "filmes"
        const val COLUNA_ID = "id"
        const val COLUNA_TITULO = "titulo"
        const val COLUNA_GENERO = "genero"
        const val COLUNA_NOTA = "nota"
        const val COLUNA_STATUS = "status"
        const val COLUNA_ANO = "ano"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABELA_FILMES + "("
                + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_TITULO + " TEXT NOT NULL,"
                + COLUNA_GENERO + " TEXT NOT NULL,"
                + COLUNA_NOTA + " INTEGER,"
                + COLUNA_STATUS + " TEXT,"
                + COLUNA_ANO + " INTEGER" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABELA_FILMES")
        onCreate(db)
    }
}
