package ru.fidean.flibuster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

private const val TAG = "BookListViewModelTAG"

sealed class BookListState {
    object LoadingState : BookListState()
    object ShowState : BookListState()
    class ErrorState(var message: String) : BookListState()
}

class BookListViewModel : ViewModel() {

    var state = MutableLiveData<BookListState>().apply { postValue(BookListState.LoadingState) }
    var bookList = MutableLiveData<List<Book>>()

    private fun parseBook(book: Element): Book {
        var title = book.select("a[href^=/b/]").first().text()
        var autor = book.select("a[href^=/a/]").last().text()
        var id = book.select("a[href^=/b/]").attr("href")
        id = id.subSequence(3, id.length).toString()
        var series = book.select("a[href^=/s/] > span").text()
        return Book(id.toInt(), title, autor, "", series)
    }

    fun parse(
        title: String,
        ganre: String,
        lastName: String,
        firstName: String,
        midleName: String,
        minSize: String,
        maxSize: String,
        form: String,
        minYear: String,
        maxYear: String,
        sort: String
    ) {
        //ab=ab1&
        //t=Title&
        //g=Ganre&
        //ln=LastName&
        //fn=FirstName&
        //mn=MidleName&
        //s1=sizeMin&
        //s2=sizeMax&
        //e=Format&
        //lng=Lang& ???
        //issueYearMin=ageMin&
        //issueYearMax=ageMax&
        //sort=sd st ss 1-по убыванию 2-по возрастанию
        try {
            CoroutineScope(Dispatchers.IO).launch {
                var bookListT: MutableList<Book> = mutableListOf()
                val doc: Document =
                    Jsoup.connect("http://flibusta.site/makebooklist?ab=ab1&sort=${sort}&t=${title}&g=${ganre}&ln=${lastName}&fn=${firstName}&mn=${midleName}&s1=${minSize}&s2=${maxSize}&e=${form}&lng=&issueYearMin=${minYear}&issueYearMax=${maxYear}&/")
                        .get()
                var books = doc.select("body > form > div")
                for (book in books) {
                    bookListT.add(parseBook(book))
                }
                bookList.postValue(bookListT)
                state.postValue(BookListState.ShowState)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error: ${e.message}")
        }
    }
}