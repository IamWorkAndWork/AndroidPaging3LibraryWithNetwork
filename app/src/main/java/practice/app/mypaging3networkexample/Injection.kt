package practice.app.mypaging3networkexample

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import practice.app.mypaging3networkexample.data.api.GithubService
import practice.app.mypaging3networkexample.data.repository.GithubRepository
import practice.app.mypaging3networkexample.data.repository.GithubRepositoryImpl
import practice.app.mypaging3networkexample.domain.usecase.GetGithubPagingSourceUseCaseImpl
import practice.app.mypaging3networkexample.presentation.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {

    const val BASE_URL = "https://api.github.com/"

    fun provideMainViewmodelFactory(): MainViewModel.Factory {
        val logger = provideHttpLoggingInterception()
        val okHttpClient = provideOkHttpClient(logger)
        val retrofit = provideRetrofit(okHttpClient)
        val githubService = provideGithubService(retrofit)
        val githubRepository = provideGithubRepository(githubService)
        val getGithubPagingSourceUseCase = provideGetGithubPagingSourceUseCase(githubRepository)
        return MainViewModel.Factory(getGithubPagingSourceUseCase)
    }

    fun provideGetGithubPagingSourceUseCase(githubRepository: GithubRepository): GetGithubPagingSourceUseCaseImpl {
        return GetGithubPagingSourceUseCaseImpl(githubRepository)
    }

    fun provideGithubRepository(githubService: GithubService): GithubRepository {
        return GithubRepositoryImpl(githubService)
    }

    fun provideGithubService(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    fun provideHttpLoggingInterception(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

}