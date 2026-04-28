package com.example.filmesnossol.dao

import android.content.ContentValues
import android.content.Context
import com.example.filmesnossol.db.DBHelper
import com.example.filmesnossol.model.Filme

class FilmeDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun inserir(filme: Filme): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUNA_TITULO, filme.titulo)
            put(DBHelper.COLUNA_GENERO, filme.genero)
            put(DBHelper.COLUNA_NOTA, filme.nota)
            put(DBHelper.COLUNA_STATUS, filme.status)
            put(DBHelper.COLUNA_ANO, filme.ano)
        }
        val id = db.insert(DBHelper.TABELA_FILMES, null, values)
        db.close()
        return id
    }

    fun atualizar(filme: Filme): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUNA_TITULO, filme.titulo)
            put(DBHelper.COLUNA_GENERO, filme.genero)
            put(DBHelper.COLUNA_NOTA, filme.nota)
            put(DBHelper.COLUNA_STATUS, filme.status)
            put(DBHelper.COLUNA_ANO, filme.ano)
        }
        val count = db.update(
            DBHelper.TABELA_FILMES,
            values,
            "${DBHelper.COLUNA_ID} = ?",
            arrayOf(filme.id.toString())
        )
        db.close()
        return count
    }

    fun excluir(id: Int): Int {
        val db = dbHelper.writableDatabase
        val count = db.delete(
            DBHelper.TABELA_FILMES,
            "${DBHelper.COLUNA_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return count
    }

    fun listarTodos(ordenarPor: String = DBHelper.COLUNA_TITULO, filtro: String? = null, valorFiltro: String? = null): List<Filme> {
        val listaFilmes = mutableListOf<Filme>()
        val db = dbHelper.readableDatabase
        
        var selecao: String? = null
        var argumentosSelecao: Array<String>? = null
        
        if (filtro != null && valorFiltro != null) {
            selecao = "$filtro LIKE ?"
            argumentosSelecao = arrayOf("%$valorFiltro%")
        }

        val cursor = db.query(
            DBHelper.TABELA_FILMES,
            null,
            selecao,
            argumentosSelecao,
            null,
            null,
            ordenarPor
        )

        if (cursor.moveToFirst()) {
            do {
                val filme = Filme(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_TITULO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_GENERO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_NOTA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_STATUS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUNA_ANO))
                )
                listaFilmes.add(filme)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaFilmes
    }
}