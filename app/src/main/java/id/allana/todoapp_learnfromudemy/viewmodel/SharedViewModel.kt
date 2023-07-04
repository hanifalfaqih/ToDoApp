package id.allana.todoapp_learnfromudemy.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.allana.todoapp_learnfromudemy.R
import id.allana.todoapp_learnfromudemy.data.model.Priority
import id.allana.todoapp_learnfromudemy.data.model.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val _isEmptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEmptyDatabase: LiveData<Boolean> = _isEmptyDatabase

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>) {
        _isEmptyDatabase.value = toDoData.isEmpty()
    }

    private val _isGridLayout = MutableLiveData(false)
    val isGridLayout: LiveData<Boolean> = _isGridLayout

    fun toggleLayout() {
        val currentLayout = _isGridLayout.value ?: false
        _isGridLayout.value = !currentLayout
    }


    val listener: AdapterView.OnItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> { (parent?.selectedView as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1 -> { (parent?.selectedView as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                2 -> { (parent?.selectedView as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}

                /**
                 * solution from lesson in udemy course
                 * change text view color
                 *
                 *  0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                    1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                    2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}
                 */
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) { }

    }

    fun checkDataFromUser(title: String, description: String): Boolean {
        return if (title.isEmpty() || description.isEmpty()) false else (title.isNotEmpty() || description.isNotEmpty())

        /**
         * solution from lesson in udemy course
         * check if data empty or not
         *
         * return if(TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) false else !(title.isEmpty() || description.isEmpty())
         */

    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            else -> Priority.LOW
        }
    }

}