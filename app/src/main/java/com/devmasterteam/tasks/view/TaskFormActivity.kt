package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = mutableListOf()
    private var taskIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()

        loadDataFromActivity()
        observe()



        //Spinner


        // Layout
        setContentView(binding.root)
    }

    private fun observe(){
        viewModel.priorityList.observe(this){
            listPriority = it
            val list = mutableListOf<String>()
            for (p in it){
                list.add(p.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this){
            if(it.status()){
                toast(it.message())
                finish()
            } else {
                toast(it.message())
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))

        }

        viewModel.taskLoad.observe(this){
            if(!it.status()){
                toast(it.message())
                finish()
            }
        }
    }

    private fun getIndex(priorityId:Int): Int {
        var index = 0
        for(l in listPriority){
            if(l.id == priorityId){break}
            index++
        }
        return index
    }

    private fun toast(str: String){
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if(bundle != null){
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdentification)

        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View) {
        if (v.id == R.id.button_date){
            handleDate()
        } else if (v.id == R.id.button_save){
            handleSave()
        }
    }

    private fun handleSave(){
        val task = TaskModel().apply {
            this.id = taskIdentification
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id

        }
        viewModel.save(task)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleDate(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }


}