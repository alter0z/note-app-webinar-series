package com.intern.notesappwebinarseries.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.intern.notesappwebinarseries.services.DatabaseReference

class FirebaseService {
    private val ref = DatabaseReference.getDBRef()

    val id = ref.push().key

    private var _data = MutableLiveData<List<NoteModel>>()
    val data get() =  _data

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getData() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val notes = data.getValue(NoteModel::class.java)
                        val noteList = ArrayList<NoteModel>()
                        noteList.add(notes!!)
                        _data.value = noteList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _message.value = error.message
            }
        })
    }

    fun addNote(model: NoteModel) {
        if (id != null) ref.child(id).setValue(model).addOnCompleteListener {
            _isSuccess.value = true
        }
    }

    fun editNote(title: String, note: String, date: String, id: String) {
        ref.child(id).child("title").setValue(title)
        ref.child(id).child("note").setValue(note)
        ref.child(id).child("date").setValue(date)
    }

    fun deleteNote(id: String) {
        ref.child(id).setValue(null).addOnCompleteListener {
            _isSuccess.value = true
        }
    }
}