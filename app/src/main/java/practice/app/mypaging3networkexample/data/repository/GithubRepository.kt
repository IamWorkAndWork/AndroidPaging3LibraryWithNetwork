package practice.app.mypaging3networkexample.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import practice.app.mypaging3networkexample.data.api.GithubService
import practice.app.mypaging3networkexample.data.model.Repo

interface GithubRepository {
    fun execute(query: String): Flow<PagingData<Repo>>
}

class GithubRepositoryImpl(private val githubService: GithubService) : GithubRepository {

    companion object {
        private const val NETWORK_PAGE_SIZE = 5
    }

    override fun execute(query: String): Flow<PagingData<Repo>> {

        val pagingConfig = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {

                GithubPagingSource(
                    service = githubService,
                    query = query
                )

            }
        ).flow

    }

}