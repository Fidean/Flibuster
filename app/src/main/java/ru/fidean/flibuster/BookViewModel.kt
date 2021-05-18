package ru.fidean.flibuster

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

private const val TAG = "BookViewModelTAG"
sealed class BookState {
    object LoadingState : BookState()
    object ShowState : BookState()
    class ErrorState(var message: String) : BookState()
}

class BookViewModel : ViewModel() {
    var state = MutableLiveData<BookState>().apply { postValue(BookState.LoadingState) }
    var book = MutableLiveData<Book>()
    fun parse(bookID: Int) {

        try {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d(TAG,bookID.toString())
                val doc: Document = Jsoup.connect("http://flibusta.site/b/${bookID}/").get()
                var title = doc.select("#main > h1").text()
                val autor = doc.select("#main > a:nth-child(5)").text()
                val anotation = doc.select("#main > p").text()
                val tempBook = Book(bookID, title, autor, anotation,"")
                book.postValue(tempBook)
                state.postValue(BookState.ShowState)
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }

}