package practice.app.mypaging3networkexample.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import practice.app.mypaging3networkexample.data.model.Repo
import practice.app.mypaging3networkexample.data.repository.GithubRepository

interface GetGithubPagingSourceUseCase {
    fun execute(query: String): Flow<PagingData<Repo>>
}

class GetGithubPagingSourceUseCaseImpl(
    private val githubRepository: GithubRepository
) : GetGithubPagingSourceUseCase {

    override fun execute(query: String): Flow<PagingData<Repo>> {
        return githubRepository.execute(query)
    }

}