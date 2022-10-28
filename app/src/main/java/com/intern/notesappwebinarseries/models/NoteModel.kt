package com.intern.notesappwebinarseries.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(val id: String? = null, val timeAgo: String? = null, val title: String? = null, val note: String? = null, val date: String? = null): Parcelable
