package ru.fidean.flibuster

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

data class Book(
    var id: Int,
    var title: String,
    var autor: String,
    var anotation: String,
    var series: String,
    var comments: List<Comment>?,
    var genre: List<String>
)

data class Comment(
    var autor: String,
    var text:String
)





