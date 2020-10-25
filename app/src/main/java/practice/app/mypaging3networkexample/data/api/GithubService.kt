package practice.app.mypaging3networkexample.data.api

import practice.app.mypaging3networkexample.data.model.RepoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): RepoSearchResponse

}
