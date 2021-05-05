package ru.fidean.flibuster

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.book_fragment.*
import kotlinx.android.synthetic.main.book_list_fragment.*

class BookListFragment : Fragment() {

    companion object {
        fun newInstance() = BookListFragment()
    }

    private lateinit var viewModel: BookListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookListViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is BookListState.LoadingState -> {
                    viewModel.parse("", "", "", "", "", "", "", "", "", "", "")
                }
                is BookListState.ShowState -> {
                    bookListView.layoutManager = LinearLayoutManager(requireContext())
                    bookListView.adapter = BookListRecyclerAdapter(viewModel.bookList.value!!, this)
                }
                is BookListState.ErrorState -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

}