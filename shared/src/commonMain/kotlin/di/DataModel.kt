package di

import data.client
import data.remote.AvService
import org.koin.dsl.module

val dataModule = module {
    factory { AvService(client) }
}