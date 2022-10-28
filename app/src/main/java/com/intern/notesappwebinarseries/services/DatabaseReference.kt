package com.intern.notesappwebinarseries.services

import com.google.firebase.database.FirebaseDatabase

object DatabaseReference {
    private val ref = FirebaseDatabase.getInstance().reference.child("data")
    fun getDBRef() = ref
}