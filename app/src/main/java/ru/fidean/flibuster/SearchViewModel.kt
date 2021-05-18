package ru.fidean.flibuster

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "SearchViewModelTAG"

sealed class SearchState {
    object LoadingState : SearchState()
    object SendState : SearchState()
    class ErrorState(var message: String) : SearchState()
}

class SearchViewModel : ViewModel() {
    var state = MutableLiveData<BookListState>().apply { postValue(BookListState.LoadingState) }


}