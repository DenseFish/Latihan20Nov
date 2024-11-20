import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.latihan20nov.R

data class Task(val name: String, val date: String, val category: String, val description: String, var status: String)

class TaskAdapter(private val context: Context, private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnHapus: Button = view.findViewById(R.id.btnHapus)
        val btnUbah: Button = view.findViewById(R.id.btnUbah)
        val btnKerjakan: Button = view.findViewById(R.id.btnKerjakan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.btnHapus.setOnClickListener {
            tasks.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.btnUbah.setOnClickListener {
        }

        holder.btnKerjakan.setOnClickListener {
            task.status = "In Progress"
            notifyItemChanged(position)
        }

//        holder.btnSelesai.setOnClickListener {
//            task.status = "Completed"
//            notifyItemChanged(position)
//        }
    }

    override fun getItemCount(): Int = tasks.size
}
