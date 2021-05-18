package ru.fidean.flibuster

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.book_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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
            val downloadmanager: DownloadManager =
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri: Uri =
                Uri.parse("http://flibusta.site/b/${viewModel.book.value!!.id}/fb2/")
            val request = DownloadManager.Request(uri)
            request.setTitle(viewModel.book.value!!.title)
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            var downloadID = downloadmanager.enqueue(request)
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

                    titleText.text = viewModel.book.value?.title ?: "Null"
                    autorText.text = viewModel.book.value?.autor ?: "Null"
                    anotationText.text = viewModel.book.value?.anotation ?: "Null"
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