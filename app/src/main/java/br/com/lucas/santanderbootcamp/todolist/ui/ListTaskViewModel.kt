package br.com.lucas.santanderbootcamp.todolist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.lucas.santanderbootcamp.todolist.database.DataBaseConnect
import br.com.lucas.santanderbootcamp.todolist.database.Task
import kotlinx.coroutines.launch

class ListTaskViewModel {
    class ListTaskViewModel(private val context: Application) : AndroidViewModel(context) {

        private val taskList = MutableLiveData<List<Task>>()

        fun refresh() {
            viewModelScope.launch {
                taskList.postValue(
                    DataBaseConnect.getTaskDao(context).getAllTasks()
                )
            }
        }
        fun findTaskByPosition(position: Int) = taskList.value?.get(position)
    }
}