package br.com.lucas.santanderbootcamp.todolist.ui.listTask

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.lucas.santanderbootcamp.todolist.R
import br.com.lucas.santanderbootcamp.todolist.core.extensions.OnItemClickListener
import br.com.lucas.santanderbootcamp.todolist.core.extensions.addOnItemClickListener
import br.com.lucas.santanderbootcamp.todolist.databinding.ActivityListTaskBinding
import br.com.lucas.santanderbootcamp.todolist.ui.InfoTaskActivity
import br.com.lucas.santanderbootcamp.todolist.ui.editTask.EditTaskActivity


class ListTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListTaskBinding
    lateinit var viewModel: ListTaskViewModel
    private val adapter by lazy { ListTaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTaskBinding.inflate(layoutInflater)
        viewModel = ListTaskViewModel(application)
        setContentView(binding.root)

        viewModel.taskList.observe(
            this
        ) { tasks ->
            (binding.recyclerViewTasks.adapter as ListTaskAdapter).addTask(tasks)
        }
        setupList()
        insertListeners(this)
    }

    private fun setupList() {
        binding.recyclerViewTasks.adapter = adapter
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (adapter.itemCount == 0) {
                    binding.includeState.emptyState.visibility = View.VISIBLE
                    binding.recyclerViewTasks.visibility = View.INVISIBLE
                } else {
                    binding.includeState.emptyState.visibility = View.GONE
                    binding.recyclerViewTasks.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun insertListeners(context: Context) {
        binding.fabAddTask.setOnClickListener {
            EditTaskActivity.launchNewTaskScreen(context)
        }
        binding.recyclerViewTasks.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val task = viewModel.findTaskByPosition(position)
                task?.let { InfoTaskActivity.launchInfoTaskActivity(context, task = it) }
            }
        })
        adapter.listenerEdit = {
            EditTaskActivity.launchEditTaskScreen(context, it)
        }
        adapter.listenerDelete = {
            viewModel.delete(it) {
                Toast.makeText(this, getString(R.string.successfully_deleted), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshScreen()
    }
}