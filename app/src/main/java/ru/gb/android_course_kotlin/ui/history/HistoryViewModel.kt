package ru.gb.android_course_kotlin.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.android_course_kotlin.App.Companion.getHistoryDao
import ru.gb.android_course_kotlin.DataState
import ru.gb.android_course_kotlin.data.localData.db.DbRepository
import ru.gb.android_course_kotlin.data.localData.db.IDbRepository

class HistoryViewModel(
    val historyLiveData: MutableLiveData<DataState> = MutableLiveData(),
    private val historyRepository: IDbRepository = DbRepository(getHistoryDao())
): ViewModel() {
    fun getAllHistory() {
        historyLiveData.postValue(DataState.Loading)
        Thread {
            historyLiveData.postValue(
                DataState.Success(
                    historyRepository.getAllHistory()
                )
            )
        }.start()
    }
}