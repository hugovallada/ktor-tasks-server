package br.com.tasks.plugins

import br.com.tasks.core.domain.data.service.TaskService
import br.com.tasks.routes.taskRoute
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val taskService by inject<TaskService>()
    install(Routing) {
        taskRoute(taskService)
    }
}
