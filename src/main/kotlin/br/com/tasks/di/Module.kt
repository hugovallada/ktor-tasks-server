package br.com.tasks.di

import br.com.tasks.core.domain.data.repository.TaskRepository
import br.com.tasks.core.domain.data.service.TaskService
import br.com.tasks.core.domain.usecase.ValidateCreateTaskRequest
import br.com.tasks.core.domain.usecase.ValidateCreateTaskRequestImpl
import br.com.tasks.data.repository.TaskRepositoryImpl
import br.com.tasks.data.service.TaskServiceImpl
import br.com.tasks.utils.enums.Constants
import br.com.tasks.utils.enums.Constants.DATABASE_NAME
import br.com.tasks.utils.enums.Constants.MONGODB_URI
import io.ktor.util.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
	single {
		val client = KMongo.createClient(connectionString = MONGODB_URI).coroutine
		client.getDatabase(DATABASE_NAME)
	}
}

val repositoryModule = module {
	single<TaskRepository> {
		TaskRepositoryImpl(get())
	}
}

val serviceModule = module {
	single<TaskService> {
		TaskServiceImpl(get(), get())
	}
}

val validateCreateTaskRequestModule = module {
	single<ValidateCreateTaskRequest> {
		ValidateCreateTaskRequestImpl()
	}
}