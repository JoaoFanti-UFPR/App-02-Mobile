package com.example.filmesnossol

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.filmesnossol.adapter.FilmeAdapter
import com.example.filmesnossol.dao.FilmeDAO
import com.example.filmesnossol.db.DBHelper
import com.example.filmesnossol.model.Filme
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var lvFilmes: ListView
    private lateinit var etBusca: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnFiltroTodos: Button
    private lateinit var btnFiltroAssistidos: Button
    private lateinit var btnFiltroParaAssistir: Button
    private lateinit var fabAdicionar: FloatingActionButton
    private lateinit var tvTotalContagem: TextView
    private lateinit var tvMediaNota: TextView

    private lateinit var filmeDAO: FilmeDAO
    private lateinit var adaptador: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        filmeDAO = FilmeDAO(this)

        lvFilmes = findViewById(R.id.lvMovies)
        etBusca = findViewById(R.id.etSearch)
        btnBuscar = findViewById(R.id.btnSearch)
        btnFiltroTodos = findViewById(R.id.btnFilterAll)
        btnFiltroAssistidos = findViewById(R.id.btnFilterWatched)
        btnFiltroParaAssistir = findViewById(R.id.btnFilterToWatch)
        fabAdicionar = findViewById(R.id.fabAdd)
        tvTotalContagem = findViewById(R.id.tvTotalCount)
        tvMediaNota = findViewById(R.id.tvAvgRating)

        carregarDadosIniciais()
        configurarLista()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    private fun carregarDadosIniciais() {
        val filmes = filmeDAO.listarTodos()
        if (filmes.isEmpty()) {
            filmeDAO.inserir(Filme(null, "O Poderoso Chefão", "Drama", 5, getString(R.string.status_watched), 1972))
            filmeDAO.inserir(Filme(null, "Batman: O Cavaleiro das Trevas", "Ação", 5, getString(R.string.status_watched), 2008))
            filmeDAO.inserir(Filme(null, "A Origem", "Sci-Fi", 4, getString(R.string.status_to_watch), 2010))
            filmeDAO.inserir(Filme(null, "Pulp Fiction", "Crime", 5, getString(R.string.status_watched), 1994))
            filmeDAO.inserir(Filme(null, "Duna: Parte 2", "Sci-Fi", 0, getString(R.string.status_to_watch), 2024))
        }
    }

    private fun configurarLista() {
        val filmes = filmeDAO.listarTodos(ordenarPor = "${DBHelper.COLUNA_TITULO} ASC")
        adaptador = FilmeAdapter(this, filmes)
        lvFilmes.adapter = adaptador
        atualizarEstatisticas(filmes)
    }

    private fun setupListeners() {
        fabAdicionar.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        lvFilmes.setOnItemClickListener { _, _, position, _ ->
            val filme = adaptador.getItem(position) as Filme
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val query = etBusca.text.toString()
            val listaFiltrada = filmeDAO.listarTodos(filtro = DBHelper.COLUNA_TITULO, valorFiltro = query)
            adaptador.atualizarDados(listaFiltrada)
            atualizarEstatisticas(listaFiltrada)
        }

        btnFiltroTodos.setOnClickListener { atualizarLista() }

        btnFiltroAssistidos.setOnClickListener {
            val listaFiltrada = filmeDAO.listarTodos(filtro = DBHelper.COLUNA_STATUS, valorFiltro = getString(R.string.status_watched))
            adaptador.atualizarDados(listaFiltrada)
            atualizarEstatisticas(listaFiltrada)
        }

        btnFiltroParaAssistir.setOnClickListener {
            val listaFiltrada = filmeDAO.listarTodos(filtro = DBHelper.COLUNA_STATUS, valorFiltro = getString(R.string.status_to_watch))
            adaptador.atualizarDados(listaFiltrada)
            atualizarEstatisticas(listaFiltrada)
        }
    }

    private fun atualizarLista() {
        val todosFilmes = filmeDAO.listarTodos(ordenarPor = "${DBHelper.COLUNA_TITULO} ASC")
        adaptador.atualizarDados(todosFilmes)
        atualizarEstatisticas(todosFilmes)
    }

    private fun atualizarEstatisticas(filmes: List<Filme>) {
        tvTotalContagem.text = getString(R.string.label_total, filmes.size)
        
        val media = if (filmes.isNotEmpty()) {
            filmes.map { it.nota }.average()
        } else {
            0.0
        }
        tvMediaNota.text = getString(R.string.label_avg, media)
    }
}
