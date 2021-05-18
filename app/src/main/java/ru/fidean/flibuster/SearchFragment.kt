package ru.fidean.flibuster

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.search_fragment.*

private const val TAG = "SearchFragmentTAG"

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchButton.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToBookListFragment(titleSearchEditText.text.toString(),firstNameSearchEditText.text.toString())
            NavHostFragment.findNavController(this).navigate(action)
        }
        viewModel.state.observe(viewLifecycleOwner){
            when(it){
                is BookListState.ErrorState -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
                is BookListState.LoadingState -> {

                }
                is BookListState.ShowState -> {

                }
            }
        }
    }

}