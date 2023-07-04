package id.allana.todoapp_learnfromudemy.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.allana.todoapp_learnfromudemy.data.model.ToDoData
import id.allana.todoapp_learnfromudemy.databinding.ItemTodoLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var dataListToDo = emptyList<ToDoData>()

    inner class ListViewHolder(private val binding: ItemTodoLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ToDoData) {
            binding.toDoData = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemTodoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dataListToDo.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataListToDo[position])
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataListToDo, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataListToDo = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}