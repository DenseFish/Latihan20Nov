package com.example.latihan20nov

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val context: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val sharedPreferences =
        context.getSharedPreferences("TaskPreferences", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val gson = Gson()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_judul)
        val date: TextView = itemView.findViewById(R.id.tv_tanggal)
        val description: TextView = itemView.findViewById(R.id.tv_deskripsi)
        val checkBox: CheckBox = itemView.findViewById(R.id.endTask)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
        val btnEdit: Button = itemView.findViewById(R.id.btnUbah)
        val btnStartSelesai: Button = itemView.findViewById(R.id.btnKerjakan)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // Set data ke view
        holder.title.text = task.judul
        holder.date.text = task.tanggal
        holder.description.text = task.deskripsi

        // Nonaktifkan listener sementara saat mengatur CheckBox
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isCompleted

        // Listener untuk CheckBox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            if (isChecked) {
                saveTaskToPreferences(task)
            } else {
                removeTaskFromPreferences(task)
            }
            notifyItemChanged(position)
        }

        // Start/Selesai
        holder.btnStartSelesai.setOnClickListener {
            Toast.makeText(context, "Task ${task.judul} started/completed!", Toast.LENGTH_SHORT).show()
        }

        // Tombol Hapus
        holder.btnHapus.setOnClickListener {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            removeTaskFromPreferences(task)
        }

        // Tombol Edit
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, AddEditTaskActivity::class.java)
            intent.putExtra("task", task)
            intent.putExtra("position", position)
            (context as Activity).startActivityForResult(intent, 200)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    private fun saveTaskToPreferences(task: Task) {
        val currentTasks = loadTasksFromPreferences().toMutableList()
        if (!currentTasks.contains(task)) {
            currentTasks.add(task)
        }
        val tasksJson = gson.toJson(currentTasks)
        editor.putString("tasks", tasksJson).apply()
    }

    private fun removeTaskFromPreferences(task: Task) {
        val currentTasks = loadTasksFromPreferences().toMutableList()
        currentTasks.remove(task)
        val tasksJson = gson.toJson(currentTasks)
        editor.putString("tasks", tasksJson).apply()
    }

    private fun loadTasksFromPreferences(): List<Task> {
        val tasksJson = sharedPreferences.getString("tasks", "[]")
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(tasksJson, type)
    }
}
