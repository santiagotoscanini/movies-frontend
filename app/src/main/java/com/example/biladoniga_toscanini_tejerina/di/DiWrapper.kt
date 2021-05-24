package com.example.biladoniga_toscanini_tejerina.di

import com.data.datasources.GameDataSource
import com.data.repositories.TeamRepository
import com.data.repositories.TeamRepositorySP
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// to get more information about Koin DI https://medium.com/swlh/dependency-injection-with-koin-6b6364dc8dba

val viewModelModule = module {
    viewModel {
        LaunchViewModel(get())
    }
}

val repositoryModule = module {
    factory<TeamRepository> {
        TeamRepositorySP(get())
    }
}

val dataSourceModule = module {
    single {
        GameDataSource(get())
    }
}
