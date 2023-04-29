package br.com.tasks.routes

import br.com.tasks.core.domain.data.service.TaskService
import br.com.tasks.data.request.CreateTaskRequest
import br.com.tasks.data.request.UpdateTaskRequest
import br.com.tasks.data.response.SimpleResponse
import br.com.tasks.utils.enums.Constants.PARAM_ID
import br.com.tasks.utils.enums.Constants.TASKS_ROUTE
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.taskRoute(taskService: TaskService) {
	route(TASKS_ROUTE) {
		getTasks(taskService)
		getTaskById(taskService)
		insert(taskService)
		update(taskService)
	}
}

private fun Route.getTasks(taskService: TaskService) {
	get {
		try {
			val tasks = taskService.getTasks()
			call.respond(HttpStatusCode.OK, tasks)
		} catch (ex: Exception) {
			application.log.error(ex.message)
			call.respond(HttpStatusCode.BadRequest)
		}
	}
}

private fun Route.getTaskById(taskService: TaskService) {
	get("/{id}") {
		val taskId = call.parameters[PARAM_ID] ?: ""
		if (taskId.isBlank()) {
			call.respond(HttpStatusCode.BadRequest, "O id não pode ser vazio")
		}
		taskService.getTaskById(taskId)?.let { task ->
			call.respond(HttpStatusCode.OK, task)
		} ?: call.respond(HttpStatusCode.NotFound, SimpleResponse(false, "Nenhuma task com o id $taskId", 404))

	}
}

private fun Route.insert(taskService: TaskService) {
	post {
		try {
			val request = call.receiveNullable<CreateTaskRequest>()
			request?.let { createTaskRequest ->
				val simpleResponse = taskService.insert(createTaskRequest)
				if (simpleResponse.success) {
					call.respond(HttpStatusCode.Created, simpleResponse)
				} else {
					call.respond(HttpStatusCode(simpleResponse.statusCode ?: 500, "Error"), simpleResponse.message)
				}
			} ?: call.respond(HttpStatusCode.BadRequest, "O corpo veio vazio")
		} catch (ex: Exception) {
			call.respond(HttpStatusCode.InternalServerError, "Erro interno")
		}
	}
}

private fun Route.update(taskService: TaskService) {
	put("/{id}") {
		val taskId = call.parameters[PARAM_ID] ?: ""
		if (taskId.isBlank()) {
			call.respond(HttpStatusCode.BadRequest, "O id não pode ser vazio")
		}
		val task= call.receiveNullable<UpdateTaskRequest>()
		task?.let {
			val result = taskService.update(taskId, task)
			call.respond(HttpStatusCode(result.statusCode ?: 500, result.message), message = result.message)
		} ?: call.respond(HttpStatusCode.BadRequest, "O corpo da requisição não pode ser vazio")
	}
}