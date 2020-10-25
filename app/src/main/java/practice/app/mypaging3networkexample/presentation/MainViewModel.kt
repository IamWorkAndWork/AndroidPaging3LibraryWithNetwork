package practice.app.mypaging3networkexample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import practice.app.mypaging3networkexample.data.model.Repo
import practice.app.mypaging3networkexample.domain.usecase.GetGithubPagingSourceUseCase

class MainViewModel(private val getGithubPagingSourceUseCase: GetGithubPagingSourceUseCase) :
    ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Repo>>? = null

    fun searchRepos(queryString: String): Flow<PagingData<Repo>>? {

        val lastResult = currentSearchResult

        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }

        currentQueryValue = queryString

        val newRequest = getGithubPagingSourceUseCase.execute(queryString)
            .cachedIn(viewModelScope)

        currentSearchResult = newRequest

        return newRequest
    }

    class Factory(private val getGithubPagingSourceUseCase: GetGithubPagingSourceUseCase) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(
                getGithubPagingSourceUseCase
            ) as T
        }

    }
}