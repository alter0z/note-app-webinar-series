package com.intern.notesappwebinarseries.listeners

import com.intern.notesappwebinarseries.models.NoteModel

interface OnDeleteClickListener {
    fun onDeleteClick(data: NoteModel)
}