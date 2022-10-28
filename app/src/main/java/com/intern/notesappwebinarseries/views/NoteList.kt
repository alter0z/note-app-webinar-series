package com.intern.notesappwebinarseries.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.intern.notesappwebinarseries.adapters.NoteListAdapter
import com.intern.notesappwebinarseries.databinding.ActivityNoteListBinding
import com.intern.notesappwebinarseries.databinding.ConfirmDialogBinding
import com.intern.notesappwebinarseries.listeners.OnDeleteClickListener
import com.intern.notesappwebinarseries.listeners.OnEditClickListener
import com.intern.notesappwebinarseries.models.NoteModel
import com.intern.notesappwebinarseries.services.DatabaseReference

class NoteList : AppCompatActivity() {
    private var _binding: ActivityNoteListBinding? = null
    private val binding get() = _binding!!
    private var layoutManager: LayoutManager? = null
    private var adapter: NoteListAdapter? = null
    private val noteList = ArrayList<NoteModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = Dialog(this@NoteList)
        val dialogBinding = ConfirmDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ref = DatabaseReference.getDBRef()
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val notes = data.getValue(NoteModel::class.java)
                        noteList.add(notes!!)
                    }

                    layoutManager = LinearLayoutManager(this@NoteList,LinearLayoutManager.VERTICAL,false)
                    adapter = NoteListAdapter(noteList)

                    binding.notes.layoutManager = layoutManager
                    binding.notes.adapter = adapter

                    adapter?.setOnEditClickListener(object: OnEditClickListener{
                        override fun onEditClick(data: NoteModel) {
                            val intent = Intent(this@NoteList,EditNote::class.java)
                            intent.putExtra(EditNote.DATA,data)
                            startActivity(intent)
                        }
                    })

                    adapter?.setOnDeleteClickListener(object: OnDeleteClickListener{
                        override fun onDeleteClick(data: NoteModel) {
                            dialog.show()
                            dialogBinding.cancel.setOnClickListener { dialog.dismiss() }
                            dialogBinding.save.setOnClickListener {
                                ref.child(data.id!!).setValue(null).addOnCompleteListener {
                                    Toast.makeText(this@NoteList,"Note Added!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@NoteList,NoteList::class.java))
                                    finish()
                                }
                            }
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NoteList,error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.add.setOnClickListener { startActivity(Intent(this,AddNote::class.java)) }
    }

    override fun onPause() {
        super.onPause()
        noteList.removeAll(noteList.toSet())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        noteList.removeAll(noteList.toSet())
    }
}