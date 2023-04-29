package br.com.tasks

import br.com.tasks.di.databaseModule
import br.com.tasks.di.repositoryModule
import br.com.tasks.di.serviceModule
import br.com.tasks.di.validateCreateTaskRequestModule
import io.ktor.server.application.*
import br.com.tasks.plugins.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        modules(
            databaseModule,
            repositoryModule,
            serviceModule,
            validateCreateTaskRequestModule
        )
    }
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
