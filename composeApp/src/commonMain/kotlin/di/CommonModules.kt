package di

import feature.dbupdate.di.dbUpdateModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    actualModules: List<Module> = listOf(),
    appDeclarations: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclarations()
        modules(
            listOf(
                dataModule,
                dbUpdateModule
            ) + actualModules
        )
    }