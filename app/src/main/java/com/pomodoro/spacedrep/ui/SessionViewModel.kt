package com.pomodoro.spacedrep.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.spacedrep.data.AppDatabase
import com.pomodoro.spacedrep.data.LearningSession
import kotlinx.coroutines.launch

class SessionViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).sessionDao()
    val sessions = dao.getAll()

    fun add(session: LearningSession) = viewModelScope.launch { dao.insert(session) }
    fun delete(session: LearningSession) = viewModelScope.launch { dao.delete(session) }
}
