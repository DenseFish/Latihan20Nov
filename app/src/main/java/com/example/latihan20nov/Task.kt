package com.example.latihan20nov

import java.io.Serializable

data class Task(
    val judul: String,
    val tanggal: String,
    val deskripsi: String,
    var isCompleted: Boolean = false
) : Serializable
