package ru.fidean.flibuster.ViewModels

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.fidean.flibuster.Book
import ru.fidean.flibuster.Comment
import ru.fidean.flibuster.ServerAPI
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
                Log.d(TAG, bookID.toString())
                val doc: Document = Jsoup.connect("http://flibusta.site/b/${bookID}/").get()
                var title = doc.select("#main > h1").text()
                val autor = doc.select("#main > a:nth-child(5)").text()
                val anotation = doc.select("#main > p").text()
                val comments = doc.select("span[style=word-wrap:break-word;]")
                var commentsList = mutableListOf<Comment>()
                for (comment in comments) {
                    commentsList.add(
                        Comment(
                            comment.text().toString().substringBefore(" в "),
                            comment.text().toString().substringAfter(" в ").drop(28).removePrefix(" ")
                        )
                    )
                }
                val tempBook = Book(bookID, title, autor, anotation, "", commentsList, listOf<String>())
                book.postValue(tempBook)
                state.postValue(BookState.ShowState)
            }

        } catch (e: IOException) {
            if (e.localizedMessage != null) {
                state.postValue(BookState.ErrorState(e.localizedMessage))
            }
        }
    }

    fun download(activity: Activity) {
        val download =
            ServerAPI.api.downloadFile("https://flibusta.site/b/${book.value!!.id}/fb2/")
        download.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                if (response.code() == 302) {
                    var uri = response.headers().get("Location")
                    if (uri != null) {
                        downloadFile(uri, activity)
                    } else {
                        state.postValue(BookState.ErrorState("Response without uri"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                if (t != null) {
                    state.postValue(BookState.ErrorState(t.localizedMessage))
                }
                if (t != null && t.message.equals("timeout")) {
                    try {
                        download(activity)
                    } catch (ex: Exception) {
                        state.postValue(BookState.ErrorState(t.localizedMessage))
                    }
                }
            }
        })
    }


    fun downloadFile(uri: String, activity: Activity) {
        Log.d(TAG, "Start download")
        val downloadmanager: DownloadManager =
            activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri =
            Uri.parse(uri)
        val request = DownloadManager.Request(uri)
        request.setTitle(book.value!!.title)
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        var downloadID = downloadmanager.enqueue(request)
    }
}