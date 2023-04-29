package br.com.tasks.data.service

import br.com.tasks.core.domain.data.repository.TaskRepository
import br.com.tasks.core.domain.data.service.TaskService
import br.com.tasks.core.domain.model.Task
import br.com.tasks.core.domain.usecase.ValidateCreateTaskRequest
import br.com.tasks.data.request.CreateTaskRequest
import br.com.tasks.data.request.UpdateTaskRequest
import br.com.tasks.data.response.SimpleResponse

class TaskServiceImpl(private val taskRepository: TaskRepository, private val validateCreateTaskRequest: ValidateCreateTaskRequest): TaskService {
	override suspend fun getTasks(): List<Task> = taskRepository.getTasks()
	override suspend fun insert(createTaskRequest: CreateTaskRequest): SimpleResponse {
		val result = validateCreateTaskRequest(createTaskRequest)
		if (!result) {
			return SimpleResponse(success = false, message = "Preencha os campos", statusCode = 400)
		}

		val insert = taskRepository.insert(task = createTaskRequest.toTask())
		if (!insert) {
			return SimpleResponse(success = false, message = "Error ao cadastrar a task", statusCode = 500)
		}
		return SimpleResponse(success = true, message = "Tarefa cadastrada com sucesso", statusCode = 201)
	}

	override suspend fun getTaskById(id: String): Task? = taskRepository.getTaskById(id)
	override suspend fun update(taskId: String, updatedTask: UpdateTaskRequest): SimpleResponse {
		val task = getTaskById(taskId) ?: return SimpleResponse(false, "Nenhuma taskcom esse id", 404)

		return if(taskRepository.update(taskId, updatedTask, task)) {
			SimpleResponse(success = true, message = "Tarefa atualizada", statusCode = 202)
		} else {
			SimpleResponse(success = false, message = "Error ao atualizar tarefa", statusCode = 500)
		}
	}


}