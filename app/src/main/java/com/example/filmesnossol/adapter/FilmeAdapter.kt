package com.example.filmesnossol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.filmesnossol.R
import com.example.filmesnossol.model.Filme

class FilmeAdapter(private val context: Context, private var listaFilmes: List<Filme>) : BaseAdapter() {

    override fun getCount(): Int = listaFilmes.size

    override fun getItem(position: Int): Any = listaFilmes[position]

    override fun getItemId(position: Int): Long = listaFilmes[position].id?.toLong() ?: 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        
        val filme = listaFilmes[position]
        
        val tvTitulo = view.findViewById<TextView>(R.id.tvTitle)
        val tvGenero = view.findViewById<TextView>(R.id.tvGenre)
        val tvAno = view.findViewById<TextView>(R.id.tvYear)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val tvNota = view.findViewById<TextView>(R.id.tvRating)
        
        tvTitulo.text = filme.titulo
        tvGenero.text = filme.genero
        tvAno.text = filme.ano.toString()
        tvStatus.text = filme.status
        tvNota.text = "Nota: ${filme.nota}/5"
        
        if (filme.status == context.getString(R.string.status_watched)) {
            tvStatus.setTextColor(context.getColor(android.R.color.holo_green_dark))
        } else {
            tvStatus.setTextColor(context.getColor(android.R.color.holo_orange_dark))
        }
        
        return view
    }

    fun atualizarDados(novaLista: List<Filme>) {
        listaFilmes = novaLista
        notifyDataSetChanged()
    }
}