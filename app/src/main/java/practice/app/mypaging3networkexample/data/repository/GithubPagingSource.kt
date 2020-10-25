package practice.app.mypaging3networkexample.data.repository

import androidx.paging.PagingSource
import practice.app.mypaging3networkexample.data.api.GithubService
import practice.app.mypaging3networkexample.data.model.Repo
import retrofit2.HttpException
import java.io.IOException

class GithubPagingSource(
    private val service: GithubService,
    private val query: String
) : PagingSource<Int, Repo>() {

    companion object {
        private const val GITHUB_STARTING_PAGE_INDEX = 1
        private const val IN_QUALIFIER = "in:name,description"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX

        val apiQuery = query + IN_QUALIFIER

        return try {

            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items

            val prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1
            val nextKey = if (repos.isEmpty()) null else position + 1

            LoadResult.Page(
                data = repos,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


}

