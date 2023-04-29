package br.com.tasks.data.request

import br.com.tasks.core.domain.model.Task

data class CreateTaskRequest(
	val title: String = "",
	val description: String = "",
	val dueDate: String = "",
	val priority: String = "",
) {
	fun toTask(): Task = Task(
		title = title, description = description,
		priority = priority, dueDate = dueDate
	)
}
