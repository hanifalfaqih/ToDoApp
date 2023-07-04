package id.allana.todoapp_learnfromudemy.fragment.list

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.allana.todoapp_learnfromudemy.R
import id.allana.todoapp_learnfromudemy.databinding.FragmentListBinding
import id.allana.todoapp_learnfromudemy.fragment.list.adapter.ListAdapter
import id.allana.todoapp_learnfromudemy.viewmodel.SharedViewModel
import id.allana.todoapp_learnfromudemy.viewmodel.ToDoViewModel
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var externalMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getAllData.observe(viewLifecycleOwner) { list ->
            sharedViewModel.checkIfDatabaseEmpty(list)
            adapter.setData(list)
        }

        sharedViewModel.isGridLayout.observe(viewLifecycleOwner) { isGridLayout ->
            setIcon(isGridLayout)

            if (isGridLayout) {
                binding.rvListTodo.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            } else {
                binding.rvListTodo.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val searchMenu = menu.findItem(R.id.menu_search)
                val searchView = searchMenu.actionView as androidx.appcompat.widget.SearchView
                searchView.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_change_layout -> {
                        externalMenuItem = menuItem
                        sharedViewModel.toggleLayout()
                    }
                    R.id.menu_delete_all -> {
                        confirmDeleteAllData()
                    }
                    R.id.menu_priority_high -> {
                        viewModel.getDataSortByHighPriority.observe(viewLifecycleOwner) { list ->
                            adapter.setData(list)
                        }
                    }
                    else -> {
                        viewModel.getDataSortByLowPriority.observe(viewLifecycleOwner) { list ->
                            adapter.setData(list)
                        }
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onQueryTextSubmit(query: String?): Boolean { return true }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            lifecycleScope.launch {
                delay(500)
                searchData(newText)
            }
        }
        return true
    }

    private fun searchData(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchData(searchQuery).observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.setData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvListTodo.adapter = this.adapter
        binding.rvListTodo.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTodo.itemAnimator = LandingAnimator().apply {
            addDuration = 250
        }

        /**
         * swipe to delete data from recycler view
         */
        swipeToDelete(binding.rvListTodo)
    }

    private fun confirmDeleteAllData() {
        val alertDialog = MaterialAlertDialogBuilder(requireContext()).also {
            it.setTitle(R.string.delete_all)
            it.setMessage(R.string.message_delete_all_dialog)
            it.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteAll()
                Snackbar.make(requireView(), getString(R.string.message_delete), Snackbar.LENGTH_SHORT).show()
            }
            it.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialog.show()

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataListToDo[viewHolder.adapterPosition]
                viewModel.deleteData(itemToDelete)
                Snackbar.make(requireView(), getString(R.string.message_delete), Snackbar.LENGTH_SHORT).setAction(getString(R.string.undo)){
                    viewModel.insertData(itemToDelete)
                }.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setIcon(isGridLayout: Boolean) {
        externalMenuItem?.let {
            if (isGridLayout) {
                it.setIcon(R.drawable.ic_list)
            } else {
                it.setIcon(R.drawable.ic_grid)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}