package com.example.filmesnossol.model

import java.io.Serializable

data class Filme(
    val id: Int? = null,
    val titulo: String,
    val genero: String,
    val nota: Int, // 0 a 5
    val status: String, // "Assistido" ou "Desejado"
    val ano: Int
) : Serializable