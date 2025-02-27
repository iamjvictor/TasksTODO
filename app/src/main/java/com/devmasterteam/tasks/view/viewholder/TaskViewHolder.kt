package com.devmasterteam.tasks.view.viewholder

import android.app.AlertDialog
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(task: TaskModel) {
        itemBinding.textDescription.text = task.description
        itemBinding.textPriority.text = task.priorityDescription
        Log.d("TaskViewHolder", "object recebida: $task ")

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(task.dueDate)
        itemBinding.textDueDate.text = date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "Data não disponível"




        if(task.complete){
            itemBinding.imageTask.setImageResource(R.drawable.ic_done)
        }else {
            itemBinding.imageTask.setImageResource(R.drawable.ic_todo)
        }


        // Eventos
        itemBinding.rowTask.setOnClickListener { listener.onListClick(task.id) }
        itemBinding.imageTask.setOnClickListener {
            if(task.complete){
                listener.onUndoClick(task.id)
            }else {
                listener.onCompleteClick(task.id)
            }
        }

        itemBinding.rowTask.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { _, _ ->
                    listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }
}