package com.example.latihan20nov

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Task>
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        taskList = loadTasks().toMutableList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskAdapter(taskList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val btnTambah = findViewById<Button>(R.id.btnTambah)
        btnTambah.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }
    private fun loadTasks(): List<Task> {
        val sharedPreferences = getSharedPreferences("TaskPreferences", MODE_PRIVATE)
        val tasksJson = sharedPreferences.getString("tasks", "[]") ?: "[]"
        val gson = Gson()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(tasksJson, type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val newTask = data?.getSerializableExtra("newTask") as? Task
            newTask?.let {
                taskList.add(it)
                adapter.notifyDataSetChanged()
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            val updatedTask = data?.getSerializableExtra("updatedTask") as? Task
            val position = data?.getIntExtra("position", -1) ?: -1

            if (updatedTask != null) {
                // validasi position before updating taskList
                if (position in 0 until taskList.size) {
                    taskList[position] = updatedTask
                    adapter.notifyItemChanged(position)
                } else {
                    // Error handle invalid position
                    Log.e("MainActivity", "Invalid position: $position")
                    Toast.makeText(this, "Error updating task: Invalid position", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}