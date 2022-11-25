package com.intern.notesappwebinarseries.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.intern.notesappwebinarseries.databinding.ActivityEditNoteBinding
import com.intern.notesappwebinarseries.models.NoteModel
import com.intern.notesappwebinarseries.services.DatabaseReference
import com.intern.notesappwebinarseries.viewmodels.NoteViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditNote : AppCompatActivity() {
    private var _binding: ActivityEditNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<NoteModel>(DATA) as NoteModel

        binding.title.setText(data.title)
        binding.note.setText(data.note)

        binding.cancel.setOnClickListener { finish() }

        binding.save.setOnClickListener {
            val title = binding.title.text.toString()
            val note = binding.note.text.toString()
            val dateUpdate = "Edited on ${humanDateFormat(LocalDateTime.now())}"

            when {
                binding.title.text!!.isEmpty() -> binding.title.error = "Title Required!"
                binding.note.text!!.isEmpty() -> binding.note.error = "Note Required!"
                else -> {
                    data.id?.let { id -> noteViewModel.editNote(title, note, dateUpdate, id) }

                    Toast.makeText(this,"Note Edited!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,NoteList::class.java))
                    finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun humanDateFormat(date: LocalDateTime) = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))

    companion object {
        const val DATA = "data"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}