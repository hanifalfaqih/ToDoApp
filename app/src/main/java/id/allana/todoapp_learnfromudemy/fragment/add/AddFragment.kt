package id.allana.todoapp_learnfromudemy.fragment.add

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import id.allana.todoapp_learnfromudemy.R
import id.allana.todoapp_learnfromudemy.data.model.ToDoData
import id.allana.todoapp_learnfromudemy.databinding.FragmentAddBinding
import id.allana.todoapp_learnfromudemy.viewmodel.SharedViewModel
import id.allana.todoapp_learnfromudemy.viewmodel.ToDoViewModel


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerPriority.onItemSelectedListener = sharedViewModel.listener

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    findNavController().navigateUp()
                } else {
                    insertDataToDatabase()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun insertDataToDatabase() {
        val title = binding.etTitle.text.toString()
        val priority = binding.spinnerPriority.selectedItem.toString()
        val description = binding.etDescription.text.toString()

        val validation = sharedViewModel.checkDataFromUser(title, description)
        if (validation) {
            // when validation return true, add data to database
            val newData = ToDoData(
                0, title, sharedViewModel.parsePriority(priority), description
            )
            viewModel.insertData(newData)

            // after success insert data to database, show snackbar and navigate back to list fragment
            Snackbar.make(requireView(), getString(R.string.message_add), Snackbar.LENGTH_SHORT).show()
            // back to list fragment navigate up
            findNavController().navigateUp()
        } else {
            Snackbar.make(requireView(), getString(R.string.message_when_data_empty), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}