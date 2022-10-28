package com.intern.notesappwebinarseries.listeners

import com.intern.notesappwebinarseries.models.NoteModel

interface OnEditClickListener {
    fun onEditClick(data: NoteModel)
}