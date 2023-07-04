package id.allana.todoapp_learnfromudemy.data.repository

import androidx.lifecycle.LiveData
import id.allana.todoapp_learnfromudemy.data.ToDoDao
import id.allana.todoapp_learnfromudemy.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()
    val getDataSortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()
    val getDataSortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchData(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDao.searchData(searchQuery)
    }
}