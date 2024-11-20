package com.example.latihan20nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var taskNameInput: EditText
    private lateinit var taskDateInput: EditText
    private lateinit var taskCategoryInput: EditText
    private lateinit var taskDescriptionInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        // Hubungkan elemen UI dengan findViewById
        taskNameInput = findViewById(R.id.taskNameInput)
        taskDateInput = findViewById(R.id.taskDateInput)
        taskCategoryInput = findViewById(R.id.taskCategoryInput)
        taskDescriptionInput = findViewById(R.id.taskDescriptionInput)
        saveButton = findViewById(R.id.saveButton)

        // Tambahkan fungsi untuk tombol Simpan
        saveButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra("taskName", taskNameInput.text.toString())
                putExtra("taskDate", taskDateInput.text.toString())
                putExtra("taskCategory", taskCategoryInput.text.toString())
                putExtra("taskDescription", taskDescriptionInput.text.toString())
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
