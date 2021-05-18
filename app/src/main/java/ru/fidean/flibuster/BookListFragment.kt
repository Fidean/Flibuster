package ru.fidean.flibuster

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.book_fragment.*
import kotlinx.android.synthetic.main.book_list_fragment.*

private const val TAG = "BookListFragmentTAG"

class BookListFragment : Fragment() {

    private lateinit var viewModel: BookListViewModel
    private val args: BookListFragmentArgs by navArgs()

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
                    Log.d(TAG,args.title)
                    viewModel.parse(
                        args.title,
                        args.ganre,
                        args.lastName,
                        args.firstName,
                        args.midleName,
                        args.minSize,
                        args.maxSize,
                        args.form,
                        args.minYear,
                        args.maxYear,
                        args.sort
                    )
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