package id.allana.todoapp_learnfromudemy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import id.allana.todoapp_learnfromudemy.data.ToDoDatabase
import id.allana.todoapp_learnfromudemy.data.model.ToDoData
import id.allana.todoapp_learnfromudemy.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    // init ToDoDao and ToDoDatabase use parameter application from AndroidViewModel
    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository = ToDoRepository(toDoDao)


    val getAllData: LiveData<List<ToDoData>> = repository.getAllData
    val getDataSortByHighPriority: LiveData<List<ToDoData>> = repository.getDataSortByHighPriority
    val getDataSortByLowPriority: LiveData<List<ToDoData>> = repository.getDataSortByLowPriority

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchData(searchQuery: String): LiveData<List<ToDoData>> {
        return repository.searchData(searchQuery )
    }

}