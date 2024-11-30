package com.example.latihan20nov

import java.io.Serializable

data class Task(
    var judul: String,
    var tanggal: String,
    var kategori: String,
    var deskripsi: String,
    var saved: Boolean = false
) : Serializable
