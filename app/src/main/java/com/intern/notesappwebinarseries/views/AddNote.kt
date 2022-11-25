package com.intern.notesappwebinarseries.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.intern.notesappwebinarseries.databinding.ActivityAddNoteBinding
import com.intern.notesappwebinarseries.models.NoteModel
import com.intern.notesappwebinarseries.viewmodels.NoteViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNote : AppCompatActivity() {
    private var _binding: ActivityAddNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            val id = noteViewModel.id
            val timeAgo = dateFormat(LocalDateTime.now())
            val title = binding.title.text.toString()
            val note = binding.note.text.toString()
            val date = humanDateFormat(LocalDateTime.now())

            when {
                binding.title.text!!.isEmpty() -> binding.title.error = "Title Required!"
                binding.note.text!!.isEmpty() -> binding.note.error = "Note Required!"
                else -> {
                    val noteModel = NoteModel(id,timeAgo,title,note,date)

                    noteViewModel.addNote(noteModel)
                    noteViewModel.isSuccess.observe(this) {
                        if (it) {
                            Toast.makeText(this,"Note Added!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,NoteList::class.java))
                            finish()
                        }
                    }
                }
            }
        }

        binding.cancel.setOnClickListener { finish() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun humanDateFormat(date: LocalDateTime) = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(date: LocalDateTime) = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}