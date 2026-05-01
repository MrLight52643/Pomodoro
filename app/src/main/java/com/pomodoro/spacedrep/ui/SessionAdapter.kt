package com.pomodoro.spacedrep.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pomodoro.spacedrep.R
import com.pomodoro.spacedrep.data.LearningSession
import java.text.SimpleDateFormat
import java.util.*

class SessionAdapter(
    private val onDelete: (LearningSession) -> Unit,
    private val onDetails: (LearningSession) -> Unit
) : ListAdapter<LearningSession, SessionAdapter.VH>(DIFF) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvBook: TextView = view.findViewById(R.id.tvBook)
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val tvNextReview: TextView = view.findViewById(R.id.tvNextReview)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val session = getItem(position)
        holder.tvBook.text = session.bookName
        holder.tvInfo.text = "${session.pagesLearned} pages · ${session.repetitions} rép. · ${session.masteryLevel.label}"

        val reviews = computeReviews(session.startDateMillis, session.masteryLevel)
        val now = System.currentTimeMillis()
        val next = reviews.firstOrNull { it.dateMillis >= now }
        holder.tvNextReview.text = if (next != null)
            "Prochaine révision : ${next.label} (${dateFormat.format(Date(next.dateMillis))})"
        else
            "Toutes les révisions terminées ✓"

        holder.btnDelete.setOnClickListener { onDelete(session) }
        holder.itemView.setOnClickListener { onDetails(session) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<LearningSession>() {
            override fun areItemsTheSame(a: LearningSession, b: LearningSession) = a.id == b.id
            override fun areContentsTheSame(a: LearningSession, b: LearningSession) = a == b
        }
    }
}
