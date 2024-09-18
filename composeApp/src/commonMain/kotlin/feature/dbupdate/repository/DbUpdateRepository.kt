package feature.dbupdate.repository

import data.Resource
import data.remote.AvService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class DbUpdateRepository(
    private val avService: AvService
) {

    suspend fun getDb(
        dbUrl: String,
        dbPath: String
    ) = flow<Resource<Boolean>> {
        avService.getDb(dbUrl) { bytes ->
            SystemFileSystem.sink(Path(dbPath), false).buffered().use { sink ->
                println("ADFFASFASDJMOIKFSJDOIFJOISD JOIFJSIODFJIOS DJOIF JSDIOFJIOSDJIOFSDJIOFJIOSD")
                sink.write(bytes)
            }
        }

        emit(Resource.Success(data = true))
    }.onStart {
        emit(Resource.Loading(true))
    }.onCompletion {
        emit(Resource.Loading(false))
    }

}