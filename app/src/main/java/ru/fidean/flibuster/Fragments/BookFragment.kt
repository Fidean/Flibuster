package ru.fidean.flibuster.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.book_fragment.*
import kotlinx.android.synthetic.main.book_list_fragment.*
import ru.fidean.flibuster.BookListRecyclerAdapter
import ru.fidean.flibuster.CommentsRecyclerAdapter
import ru.fidean.flibuster.LoginData
import ru.fidean.flibuster.R
import ru.fidean.flibuster.ViewModels.BookState
import ru.fidean.flibuster.ViewModels.BookViewModel

private const val TAG = "BookFragmentTAG"

class BookFragment : Fragment() {

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args: BookFragmentArgs by navArgs()
        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        downloadButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(activity!!, permissions, 0)
            }
            viewModel.download(activity!!)
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is BookState.LoadingState -> {
                    Log.d(TAG, arguments!!.getInt("bookID").toString())
                    viewModel.parse(arguments!!.getInt("bookID"))
                    progressBar.visibility = View.VISIBLE
                    autorText.visibility = View.INVISIBLE
                    titleText.visibility = View.INVISIBLE
                    anotationText.visibility = View.INVISIBLE
                    downloadButton.visibility = View.INVISIBLE
                }
                is BookState.ShowState -> {
                    if (LoginData.cookie != null) {
                        Toast.makeText(requireContext(), LoginData.cookie, Toast.LENGTH_LONG).show()
                    }
                    titleText.text = viewModel.book.value?.title ?: "Null"
                    autorText.text = viewModel.book.value?.autor ?: "Null"
                    anotationText.text = viewModel.book.value?.anotation ?: "Null"
                    commentsView.layoutManager = LinearLayoutManager(requireContext())
                    commentsView.adapter = CommentsRecyclerAdapter(viewModel.book.value!!.comments!!)
                    progressBar.visibility = View.INVISIBLE
                    autorText.visibility = View.VISIBLE
                    titleText.visibility = View.VISIBLE
                    anotationText.visibility = View.VISIBLE
                    downloadButton.visibility = View.VISIBLE
                }
                is BookState.ErrorState -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

}