package com.example.latihan20nov

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
        val category: TextView = itemView.findViewById(R.id.tv_kategori)
        val description: TextView = itemView.findViewById(R.id.tv_deskripsi)
        val saveArrow: ImageView = itemView.findViewById(R.id.saveTask)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
        val btnEdit: Button = itemView.findViewById(R.id.btnUbah)
        val btnStart: Button = itemView.findViewById(R.id.btnKerjakan)
        val btnEnd: Button = itemView.findViewById(R.id.btnSelesai)
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
        holder.category.text = task.kategori
        holder.description.text = task.deskripsi

        // Nonaktifkan listener sementara saat mengatur CheckBox
        holder.saveArrow.setOnClickListener {
            if (task.saved) {
                task.saved = false // Update task.saved to false
                removeTaskFromPreferences(task)
                Toast.makeText(context, "Task removed from bookmark", Toast.LENGTH_SHORT).show()
            } else { // Use else instead of else if
                task.saved = true // Update task.saved to true
                saveTaskToPreferences(task)
                Toast.makeText(context, "Task saved from bookmark", Toast.LENGTH_SHORT).show()
            }
        }

        // Start
        holder.btnStart.setOnClickListener {
            Toast.makeText(context, "Task ${task.judul} started!", Toast.LENGTH_SHORT).show()
            holder.btnStart.visibility = View.INVISIBLE
            holder.btnEnd.visibility = View.VISIBLE
            holder.btnEdit.isEnabled = false
            holder.btnEdit.setBackgroundColor(Color.GRAY)
            holder.saveArrow.visibility = View.INVISIBLE
        }

        // End
        holder.btnEnd.setOnClickListener {
            Toast.makeText(context, "Task ${task.judul} completed!", Toast.LENGTH_SHORT).show()
            holder.saveArrow.visibility = View.VISIBLE
            holder.btnStart.visibility = View.INVISIBLE
            holder.btnEdit.visibility = View.INVISIBLE
            holder.btnEnd.visibility = View.INVISIBLE
            holder.btnHapus.visibility = View.INVISIBLE

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
            removeTaskFromPreferences(task)
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
