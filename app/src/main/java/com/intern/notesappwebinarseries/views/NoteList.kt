package com.intern.notesappwebinarseries.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.intern.notesappwebinarseries.adapters.NoteListAdapter
import com.intern.notesappwebinarseries.databinding.ActivityNoteListBinding
import com.intern.notesappwebinarseries.databinding.ConfirmDialogBinding
import com.intern.notesappwebinarseries.listeners.OnDeleteClickListener
import com.intern.notesappwebinarseries.listeners.OnEditClickListener
import com.intern.notesappwebinarseries.models.NoteModel
import com.intern.notesappwebinarseries.viewmodels.NoteViewModel

class NoteList : AppCompatActivity() {
    private var _binding: ActivityNoteListBinding? = null
    private val binding get() = _binding!!
    private var layoutManager: LayoutManager? = null
    private var adapter: NoteListAdapter? = null
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = Dialog(this@NoteList)
        val dialogBinding = ConfirmDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        noteViewModel.getData()
        noteViewModel.data().observe(this) {
            if (it != null) adapter?.setNote(it)
        }

        noteViewModel.message.observe(this) {
            Toast.makeText(this@NoteList,it, Toast.LENGTH_SHORT).show()
        }

        adapter = NoteListAdapter()
        layoutManager = LinearLayoutManager(this@NoteList,LinearLayoutManager.VERTICAL,false)

        binding.notes.layoutManager = layoutManager
        binding.notes.setHasFixedSize(true)
        binding.notes.adapter = adapter

        adapter?.setOnEditClickListener(object: OnEditClickListener{
            override fun onEditClick(data: NoteModel) {
                val intent = Intent(this@NoteList,EditNote::class.java)
                intent.putExtra(EditNote.DATA,data)
                startActivity(intent)
            }
        })

        adapter?.setOnDeleteClickListener(object: OnDeleteClickListener {
            override fun onDeleteClick(data: NoteModel) {
                dialog.show()
                dialogBinding.cancel.setOnClickListener { dialog.dismiss() }
                dialogBinding.save.setOnClickListener {
                    data.id?.let { id -> noteViewModel.deleteNote(id) }
                    noteViewModel.isSuccess.observe(this@NoteList) {
                        if (it) {
                            Toast.makeText(this@NoteList,"Note Added!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        })

        binding.add.setOnClickListener { startActivity(Intent(this,AddNote::class.java)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}