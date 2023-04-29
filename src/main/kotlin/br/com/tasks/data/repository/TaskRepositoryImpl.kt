package br.com.tasks.data.repository

import br.com.tasks.core.domain.data.repository.TaskRepository
import br.com.tasks.core.domain.model.Task
import br.com.tasks.data.request.UpdateTaskRequest
import br.com.tasks.utils.enums.Constants.TASKS_COLLECTION
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class TaskRepositoryImpl(
	private val database: CoroutineDatabase
) : TaskRepository {
	private val taskCollection: CoroutineCollection<Task> = database.getCollection<Task>(TASKS_COLLECTION)


	override suspend fun getTasks(): List<Task> = taskCollection.find().toList()


	override suspend fun getTaskById(id: String): Task? = taskCollection.findOneById(id)

	override suspend fun insert(task: Task): Boolean = taskCollection.insertOne(task).wasAcknowledged()

	override suspend fun update(id: String, updatedTaskRequest: UpdateTaskRequest, currentTask: Task): Boolean =
		taskCollection.updateOneById(
			id = id, update = Task(
				id = currentTask.id,
				title = updatedTaskRequest.title,
				description = updatedTaskRequest.description,
				priority = updatedTaskRequest.priority,
				dueDate = currentTask.dueDate,
				completed = currentTask.completed,
				createdAt = currentTask.createdAt
			)
		).wasAcknowledged()

	override suspend fun delete(id: String): Boolean = taskCollection.deleteOneById(id).wasAcknowledged()

	override suspend fun completeTask(id: String): Long =
		taskCollection.updateOne(Task::id eq id, setValue(Task::completed, true)).modifiedCount

}