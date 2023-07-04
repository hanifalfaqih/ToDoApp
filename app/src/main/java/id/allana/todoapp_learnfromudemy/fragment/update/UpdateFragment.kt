package id.allana.todoapp_learnfromudemy.fragment.update

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.allana.todoapp_learnfromudemy.R
import id.allana.todoapp_learnfromudemy.data.model.ToDoData
import id.allana.todoapp_learnfromudemy.databinding.FragmentUpdateBinding
import id.allana.todoapp_learnfromudemy.viewmodel.SharedViewModel
import id.allana.todoapp_learnfromudemy.viewmodel.ToDoViewModel


class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> findNavController().navigateUp()
                    R.id.menu_update -> updateToDoData()
                    R.id.menu_delete -> confirmDeleteToDoData()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.spinnerPriorityUpdate.onItemSelectedListener = sharedViewModel.listener

    }

    private fun confirmDeleteToDoData() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext()).also {
            it.setTitle(getString(R.string.title_delete_dialog, args.currentData.title))
            it.setMessage(getString(R.string.message_delete_dialog, args.currentData.title))
            it.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteData(args.currentData)
                Snackbar.make(requireView(), getString(R.string.message_delete), Snackbar.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            it.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialog.show()

    }

    private fun updateToDoData() {
        val title = binding.etTitleUpdate.text.toString()
        val description = binding.etDescriptionUpdate.text.toString()
        val priority = binding.spinnerPriorityUpdate.selectedItem.toString()

        val validation = sharedViewModel.checkDataFromUser(title, description)
        if (validation) {
            val updateData = ToDoData(
                args.currentData.id,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            viewModel.updateData(updateData)
            Snackbar.make(requireView(), getString(R.string.message_update), Snackbar.LENGTH_SHORT).show()
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