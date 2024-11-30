package com.example.latihan20nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        val inputTask = findViewById<EditText>(R.id.taskName)
        val inputTanggal = findViewById<EditText>(R.id.taskDate)
        val inputKategori = findViewById<EditText>(R.id.taskCategory)
        val inputDeskripsi = findViewById<EditText>(R.id.taskDescription)
        val btnSimpan = findViewById<Button>(R.id.saveButton)

        val task = intent.getSerializableExtra("task") as? Task
        val position = intent.getIntExtra("position", -1)

        // Jika task tidak null, isi input dengan data task
        if (task != null) {
            inputTask.setText(task.judul)
            inputTanggal.setText(task.tanggal)
            inputKategori.setText(task.kategori)
            inputDeskripsi.setText(task.deskripsi)
        }

        btnSimpan.setOnClickListener {
            val taskName = inputTask.text.toString()
            val taskDate = inputTanggal.text.toString()
            val taskCat = inputKategori.text.toString()
            val taskDesc = inputDeskripsi.text.toString()

            if (taskName.isNotEmpty() && taskDate.isNotEmpty() && taskCat.isNotEmpty()) {
                val existingTask = intent.getSerializableExtra("task") as? Task // Check for existing task
                val position = intent.getIntExtra("position", -1)

                if (existingTask != null) {
                    // Update existing task
                    existingTask.judul = taskName
                    existingTask.tanggal = taskDate
                    existingTask.kategori = taskCat
                    existingTask.deskripsi = taskDesc

                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedTask", existingTask) // Use "updatedTask" key
                    resultIntent.putExtra("position", position)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    // Add new task (existing logic)
                    val newTask = Task(taskName, taskDate, taskCat, taskDesc, false)
                    val resultIntent = Intent()
                    resultIntent.putExtra("newTask", newTask)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
