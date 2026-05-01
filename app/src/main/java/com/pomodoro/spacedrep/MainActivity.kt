package com.pomodoro.spacedrep

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pomodoro.spacedrep.data.LearningSession
import com.pomodoro.spacedrep.data.MasteryLevel
import com.pomodoro.spacedrep.databinding.ActivityMainBinding
import com.pomodoro.spacedrep.ui.SessionAdapter
import com.pomodoro.spacedrep.ui.SessionViewModel
import com.pomodoro.spacedrep.ui.computeReviews
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SessionViewModel
    private lateinit var adapter: SessionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SessionViewModel::class.java]

        adapter = SessionAdapter(
            onDelete = { session ->
                AlertDialog.Builder(this)
                    .setTitle("Supprimer")
                    .setMessage("Supprimer « ${session.bookName} » ?")
                    .setPositiveButton("Supprimer") { _, _ -> viewModel.delete(session) }
                    .setNegativeButton("Annuler", null)
                    .show()
            },
            onDetails = { session -> showDetails(session) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.sessions.observe(this) { sessions ->
            adapter.submitList(sessions)
            binding.tvEmpty.visibility = if (sessions.isEmpty())
                android.view.View.VISIBLE else android.view.View.GONE
        }

        binding.fab.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_session, null)
        val etBook = dialogView.findViewById<EditText>(R.id.etBook)
        val etPages = dialogView.findViewById<EditText>(R.id.etPages)
        val etRepetitions = dialogView.findViewById<EditText>(R.id.etRepetitions)
        val spinnerMastery = dialogView.findViewById<Spinner>(R.id.spinnerMastery)

        val levels = MasteryLevel.values()
        spinnerMastery.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            levels.map { it.label })

        AlertDialog.Builder(this)
            .setTitle("Nouvelle session")
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val book = etBook.text.toString().trim()
                val pages = etPages.text.toString().toIntOrNull() ?: 0
                val reps = etRepetitions.text.toString().toIntOrNull() ?: 0
                val mastery = levels[spinnerMastery.selectedItemPosition]
                if (book.isNotEmpty()) {
                    viewModel.add(LearningSession(
                        bookName = book,
                        pagesLearned = pages,
                        repetitions = reps,
                        masteryLevel = mastery
                    ))
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun showDetails(session: LearningSession) {
        val reviews = computeReviews(session.startDateMillis, session.masteryLevel)
        val fmt = SimpleDateFormat("EEE dd/MM/yyyy", Locale.FRENCH)
        val now = System.currentTimeMillis()

        val sb = StringBuilder()
        sb.appendLine("📚 ${session.bookName}")
        sb.appendLine("Pages : ${session.pagesLearned}  |  Répétitions : ${session.repetitions}")
        sb.appendLine("Maîtrise : ${session.masteryLevel.label}")
        sb.appendLine()
        sb.appendLine("Planning de révisions :")
        sb.appendLine()
        reviews.forEach { r ->
            val done = r.dateMillis < now
            val marker = if (done) "✓" else "○"
            sb.appendLine("$marker  ${r.label.padEnd(5)}  ${fmt.format(Date(r.dateMillis))}")
        }

        AlertDialog.Builder(this)
            .setTitle("Détail des révisions")
            .setMessage(sb.toString())
            .setPositiveButton("Fermer", null)
            .show()
    }
}
