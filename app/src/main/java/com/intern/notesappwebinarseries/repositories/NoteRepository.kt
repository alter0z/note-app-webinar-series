package com.intern.notesappwebinarseries.repositories

import androidx.lifecycle.LiveData
import com.intern.notesappwebinarseries.models.FirebaseService
import com.intern.notesappwebinarseries.models.NoteModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository {
    private val service: FirebaseService = FirebaseService()
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    val id = service.id

    fun getData() {
        executorService.execute { service.getData() }
    }

    val message: LiveData<String> = service.message
    fun data(): LiveData<List<NoteModel>> = service.data

    fun addNote(model: NoteModel) {
        executorService.execute { service.addNote(model) }
    }

    val isSuccess: LiveData<Boolean> = service.isSuccess

    fun editNote(title: String, note: String, date: String, id: String) {
        executorService.execute { service.editNote(title, note, date, id) }
    }

    fun deleteNote(id: String) {
        executorService.execute { service.deleteNote(id) }
    }
}