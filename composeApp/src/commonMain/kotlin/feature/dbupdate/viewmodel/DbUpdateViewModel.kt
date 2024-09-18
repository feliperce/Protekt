package feature.dbupdate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Resource
import data.remote.AvService
import feature.dbupdate.repository.DbUpdateRepository
import feature.dbupdate.state.UpdateDbIntent
import feature.dbupdate.state.UpdateDbUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DbUpdateViewModel(
    private val dbUpdateRepository: DbUpdateRepository
): ViewModel() {

    private val intentChannel = Channel<UpdateDbIntent>(Channel.UNLIMITED)

    private val _updateDbState = MutableStateFlow(UpdateDbUiState(loading = false))
    val updateDbState: StateFlow<UpdateDbUiState> = _updateDbState.asStateFlow()

    init {
        handleIntents()
    }

    fun sendIntent(intent: UpdateDbIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        intentChannel
            .consumeAsFlow()
            .onEach { intent ->
                when(intent) {
                    is UpdateDbIntent.UpdateDb -> {
                        updateDb(
                            md5DbPath = intent.md5DbPath,
                            sha1DbPath = intent.sha1DbPath,
                            sha256DbPath = intent.sha256DbPath
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun updateDb(
        md5DbPath: String,
        sha1DbPath: String,
        sha256DbPath: String
    ) {
        viewModelScope.launch {
            dbUpdateRepository.getDb(
                AvService.MD5_DB_URL,
                md5DbPath
            ).collect { res ->
                when (res) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _updateDbState.update {
                            it.copy(loading = res.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        _updateDbState.update {
                            it.copy(
                                isMd5DbUpdated = res.data ?: false
                            )
                        }
                    }
                }
            }

            dbUpdateRepository.getDb(
                AvService.SHA1_DB_URL,
                sha1DbPath
            ).collect { res ->
                when (res) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _updateDbState.update {
                            it.copy(loading = res.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        _updateDbState.update {
                            it.copy(
                                isSha1DbUpdated = res.data ?: false
                            )
                        }
                    }
                }
            }

            dbUpdateRepository.getDb(
                AvService.SHA256_DB_URL,
                sha256DbPath
            ).collect { res ->
                when (res) {
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        _updateDbState.update {
                            it.copy(loading = res.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        _updateDbState.update {
                            it.copy(
                                isSha256DbUpdated = res.data ?: false
                            )
                        }
                    }
                }
            }
        }
    }

}