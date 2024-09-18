package feature.dbupdate.di

import feature.dbupdate.repository.DbUpdateRepository
import feature.dbupdate.viewmodel.DbUpdateViewModel
import org.koin.dsl.module

val dbUpdateModule = module {
    single { DbUpdateRepository(get()) }

    single { DbUpdateViewModel(get()) }
}