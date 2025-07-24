//package org.finAware.project.api
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import io.ktor.client.HttpClient
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import org.finAware.project.model.LearningEntry
//
//@RequiresApi(Build.VERSION_CODES.O)
//class LearningViewModel(client: HttpClient) : ViewModel() {
//    client: HttpClient
//    private val _entries = MutableStateFlow<List<LearningEntry>>(emptyList())
//    val entries: StateFlow<List<LearningEntry>> = _entries
//
//    init {
//        viewModelScope.launch {
//            try {
//                _entries.value = fetchLearningEntries(client = client)
//                println("✅ Entries loaded: ${_entries.value.size}")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                println("❌ Error: ${e.message}")
//            }
//        }
//    }
//}
