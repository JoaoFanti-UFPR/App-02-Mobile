package com.example.filmesnossol

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.filmesnossol.dao.FilmeDAO
import com.example.filmesnossol.model.Filme
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class AddEditActivity : AppCompatActivity() {

    private lateinit var etTitulo: TextInputEditText
    private lateinit var etGenero: TextInputEditText
    private lateinit var etAno: TextInputEditText
    private lateinit var etNota: TextInputEditText
    private lateinit var rgStatus: RadioGroup
    private lateinit var btnSalvar: Button
    private lateinit var btnExcluir: Button

    private var filme: Filme? = null
    private lateinit var filmeDAO: FilmeDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        filmeDAO = FilmeDAO(this)

        etTitulo = findViewById(R.id.etTitle)
        etGenero = findViewById(R.id.etGenre)
        etAno = findViewById(R.id.etYear)
        etNota = findViewById(R.id.etRating)
        rgStatus = findViewById(R.id.rgStatus)
        btnSalvar = findViewById(R.id.btnSave)
        btnExcluir = findViewById(R.id.btnDelete)

        // Alterado de "movie" para "filme" para manter o padrão
        filme = intent.getSerializableExtra("filme") as? Filme

        if (filme != null) {
            supportActionBar?.title = getString(R.string.title_edit)
            etTitulo.setText(filme?.titulo)
            etGenero.setText(filme?.genero)
            etAno.setText(filme?.ano.toString())
            etNota.setText(filme?.nota.toString())
            if (filme?.status == getString(R.string.status_watched)) {
                findViewById<RadioButton>(R.id.rbWatched).isChecked = true
            } else {
                findViewById<RadioButton>(R.id.rbToWatch).isChecked = true
            }
            btnExcluir.visibility = View.VISIBLE
        } else {
            supportActionBar?.title = getString(R.string.title_new)
        }

        btnSalvar.setOnClickListener { salvarFilme() }
        btnExcluir.setOnClickListener { excluirFilme() }
    }

    private fun salvarFilme() {
        val titulo = etTitulo.text.toString()
        val genero = etGenero.text.toString()
        val anoStr = etAno.text.toString()
        val notaStr = etNota.text.toString()
        val status = if (rgStatus.checkedRadioButtonId == R.id.rbWatched) 
            getString(R.string.status_watched) else getString(R.string.status_to_watch)

        if (titulo.isBlank() || genero.isBlank() || anoStr.isBlank() || notaStr.isBlank()) {
            Toast.makeText(this, getString(R.string.msg_fill_all), Toast.LENGTH_SHORT).show()
            return
        }

        val ano = anoStr.toIntOrNull() ?: 0
        val nota = notaStr.toIntOrNull() ?: 0

        if (nota < 0 || nota > 5) {
            Toast.makeText(this, getString(R.string.msg_invalid_rating), Toast.LENGTH_SHORT).show()
            etNota.requestFocus()
            return
        }
        
        if (ano < 1888) { 
            Toast.makeText(this, getString(R.string.msg_invalid_year), Toast.LENGTH_SHORT).show()
            etAno.requestFocus()
            return
        }

        val novoFilme = Filme(filme?.id, titulo, genero, nota, status, ano)

        if (filme == null) {
            filmeDAO.inserir(novoFilme)
            Toast.makeText(this, getString(R.string.msg_success_add), Toast.LENGTH_SHORT).show()
        } else {
            filmeDAO.atualizar(novoFilme)
            Toast.makeText(this, getString(R.string.msg_success_update), Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun excluirFilme() {
        filme?.id?.let {
            filmeDAO.excluir(it)
            Toast.makeText(this, getString(R.string.msg_deleted), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
