package id.allana.todoapp_learnfromudemy.data

import androidx.room.TypeConverter
import id.allana.todoapp_learnfromudemy.data.model.Priority

class PriorityConverter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}