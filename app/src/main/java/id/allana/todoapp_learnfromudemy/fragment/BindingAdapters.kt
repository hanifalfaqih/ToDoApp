package id.allana.todoapp_learnfromudemy.fragment

import android.view.View
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.allana.todoapp_learnfromudemy.R
import id.allana.todoapp_learnfromudemy.data.model.Priority
import id.allana.todoapp_learnfromudemy.data.model.ToDoData
import id.allana.todoapp_learnfromudemy.fragment.list.ListFragmentDirections

class BindingAdapters {

    companion object {

        @BindingAdapter("android:toAddFragment")
        @JvmStatic
        fun toAddFragment(view: FloatingActionButton, navigate: Boolean) {
            if (navigate) {
                view.setOnClickListener {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:stateEmptyDatabase")
        @JvmStatic
        fun stateEmptyDatabase(view: View, emptyDatabase: LiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parseToPrioritySelection")
        @JvmStatic
        fun parseToPrioritySelection(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> { view.setSelection(0) }
                Priority.MEDIUM -> { view.setSelection(1) }
                Priority.LOW -> { view.setSelection(2) }
            }
        }

        @BindingAdapter("android:parseToPriorityColor")
        @JvmStatic
        fun parseToPriorityColor(cardView: MaterialCardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> { cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red)) }
                Priority.MEDIUM -> { cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow)) }
                else -> { cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green)) }
            }
        }

        @BindingAdapter("android:toUpdateFragment")
        @JvmStatic
        fun toUpdateFragment(view: RelativeLayout, currentData: ToDoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentData)
                it.findNavController().navigate(action)
            }
        }


    }
}