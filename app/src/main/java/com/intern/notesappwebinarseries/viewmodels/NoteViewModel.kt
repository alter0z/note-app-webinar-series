package com.intern.notesappwebinarseries.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.intern.notesappwebinarseries.models.NoteModel
import com.intern.notesappwebinarseries.repositories.NoteRepository

class NoteViewModel: ViewModel() {
    private val repository: NoteRepository = NoteRepository()

    val id = repository.id

    val message: LiveData<String> = repository.message
    fun data(): LiveData<List<NoteModel>> = repository.data()

    fun getData() {
        repository.getData()
    }

    fun addNote(model: NoteModel) {
        repository.addNote(model)
    }

    val isSuccess: LiveData<Boolean> = repository.isSuccess

    fun editNote(title: String, note: String, date: String, id: String) {
        repository.editNote(title, note, date, id)
    }

    fun deleteNote(id: String) {
        repository.deleteNote(id)
    }
}