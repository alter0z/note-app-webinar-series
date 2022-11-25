package com.intern.notesappwebinarseries.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intern.notesappwebinarseries.databinding.NoteCardBinding
import com.intern.notesappwebinarseries.listeners.OnDeleteClickListener
import com.intern.notesappwebinarseries.listeners.OnEditClickListener
import com.intern.notesappwebinarseries.models.NoteModel
import java.text.SimpleDateFormat

class NoteListAdapter: RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private val noteList = ArrayList<NoteModel>()
    private lateinit var onEditClickListener: OnEditClickListener
    private lateinit var onDeleteClickListener: OnDeleteClickListener

    fun setNote(list: List<NoteModel>) {
        this.noteList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnEditClickListener(onEditClickListener: OnEditClickListener) {
        this.onEditClickListener = onEditClickListener
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }
    inner class ViewHolder(val binding: NoteCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder) {
            with (noteList[position]) {
                binding.timeAgo.text = getTimeAgo(timeAgo!!)
                binding.title.text = title
                binding.note.text = note
                binding.dateAdded.text = date

                binding.edit.setOnClickListener { onEditClickListener.onEditClick(noteList[position]) }
                binding.delete.setOnClickListener {
                    onDeleteClickListener.onDeleteClick(noteList[position])
                    noteList.clear()
                }

                if (position == 0) {
                    val params = itemView.layoutParams as RecyclerView.LayoutParams
                    params.topMargin = 280
                    itemView.layoutParams = params
                } else if (position == noteList.lastIndex) {
                    val params = itemView.layoutParams as RecyclerView.LayoutParams
                    params.bottomMargin = 80
                    itemView.layoutParams = params
                }
            }
        }
    }

    private fun getTimeAgo(date: String): CharSequence {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val time = sdf.parse(date)?.time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time!!,now,DateUtils.MINUTE_IN_MILLIS)
    }

    override fun getItemCount() = noteList.size
}