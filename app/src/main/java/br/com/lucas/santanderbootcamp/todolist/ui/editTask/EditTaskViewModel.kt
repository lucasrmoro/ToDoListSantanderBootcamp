package br.com.lucas.santanderbootcamp.todolist.ui.editTask

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucas.santanderbootcamp.todolist.R
import br.com.lucas.santanderbootcamp.todolist.core.extensions.convertStringToLong
import br.com.lucas.santanderbootcamp.todolist.database.DataBaseConnect
import br.com.lucas.santanderbootcamp.todolist.database.Task
import kotlinx.coroutines.launch

class EditTaskViewModel : ViewModel() {

    val isTaskTitleValid = MutableLiveData<Boolean>()
    val isTaskDateEmpty = MutableLiveData<Boolean>()
    val isTaskTimeEmpty = MutableLiveData<Boolean>()

    var task: Task? = null
        private set

    var totalTaskTime: Int? = null
        private set

    fun setup(task: Task) {
        this.task = task
        totalTaskTime = task.taskTime
    }

    fun checkTaskTitleIsValid(content: String) {
        isTaskTitleValid.value = content.length >= 3
    }

    fun checkTaskDateIsEmpty(content: String) {
        isTaskDateEmpty.value = content.isNotEmpty()
    }

    fun convertHourAndMinutesToFullTime(hour: Int, minutes: Int) {
        val hoursInMinutes = hour * 60
        totalTaskTime = hoursInMinutes + minutes
    }

    fun checkTaskTimeIsEmpty() {
        isTaskTimeEmpty.value = totalTaskTime != null
    }

    fun onSaveEvent(
        context: Context, taskTitle: String, taskDescription: String,
        taskDate: String,
        closeScreen: (() -> Unit)
    ) {
        if (task == null) {
            saveNewTask(context, taskTitle, taskDescription, taskDate, closeScreen)
        } else {
            task!!.taskTitle = taskTitle
            task!!.taskTime = totalTaskTime!!
            task!!.taskDate = taskDate.convertStringToLong()
            task!!.taskDescription = taskDescription
            saveSameTask(context, task!!, closeScreen)
        }
    }

    private fun saveNewTask(
        context: Context,
        taskTitle: String, taskDescription: String, taskDate: String,
        closeScreen: () -> Unit
    ) {
        checkTaskTitleIsValid(taskTitle)
        checkTaskDateIsEmpty(taskDate)
        checkTaskTimeIsEmpty()

        if (isTaskTitleValid.value == true &&
            isTaskDateEmpty.value == true &&
            isTaskTimeEmpty.value == true
        ) {
            viewModelScope.launch {
                DataBaseConnect.getTaskDao(context).insertTask(
                    Task(
                        taskTitle = taskTitle,
                        taskDescription = taskDescription,
                        taskDate = taskDate.convertStringToLong(),
                        taskTime = totalTaskTime!!,
                        uid = 0
                    )
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.successfully_created),
                    Toast.LENGTH_SHORT
                ).show()
                closeScreen()
            }
        } else {
            fillAllRequiredFieldsToast(context)
        }
    }

    private fun saveSameTask(
        context: Context,
        task: Task,
        closeScreen: () -> Unit
    ) {
        checkTaskTitleIsValid(task.taskTitle)
        checkTaskDateIsEmpty(task.taskDate.toString())
        checkTaskTimeIsEmpty()

        if (isTaskTitleValid.value == true &&
            isTaskDateEmpty.value == true &&
            isTaskTimeEmpty.value == true
        ) {
            viewModelScope.launch {
                DataBaseConnect.getTaskDao(context).updateTask(
                    task
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.successfully_edited),
                    Toast.LENGTH_SHORT
                ).show()
                closeScreen()
            }
        } else {
            fillAllRequiredFieldsToast(context)
        }
    }

    private fun fillAllRequiredFieldsToast(context: Context) {
        Toast.makeText(
            context,
            context.getString(R.string.fill_all_required_fields),
            Toast.LENGTH_SHORT
        ).show()
    }
}