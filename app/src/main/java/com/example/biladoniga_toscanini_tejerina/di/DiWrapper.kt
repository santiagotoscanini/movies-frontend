package com.example.biladoniga_toscanini_tejerina.di

import com.data.datasources.GameDataSource
import com.data.datasources.MovieDataSource
import com.data.repositories.movie.MovieRepositoryRetrofit
import com.data.repositories.movie.MovieRepository
import com.data.repositories.team.TeamRepository
import com.data.repositories.team.TeamRepositorySP
import com.example.biladoniga_toscanini_tejerina.commons.API_URL
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.example.biladoniga_toscanini_tejerina.ranking.viewmodel.RankingViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// to get more information about Koin DI https://medium.com/swlh/dependency-injection-with-koin-6b6364dc8dba

val viewModelModule = module {
    viewModel { LaunchViewModel(get()) }
    viewModel { GameViewModel(get(), get()) }
    viewModel { RankingViewModel(get()) }
}

val repositoryModule = module {
    factory<TeamRepository> {
        TeamRepositorySP(get())
    }
    factory<MovieRepository> {
        MovieRepositoryRetrofit(get())
    }
}

val dataSourceModule = module {
    single {
        GameDataSource(get())
    }
}

val retrofitDataSourceModule = module {
    fun provideUseApi(retrofit: Retrofit): MovieDataSource {
        return retrofit.create(MovieDataSource::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
}
