package feature.dbupdate.state


data class UpdateDbUiState (
    val loading: Boolean = false,
    val isMd5DbUpdated: Boolean = false,
    val isSha1DbUpdated: Boolean = false,
    val isSha256DbUpdated: Boolean = false
)

sealed class UpdateDbIntent {
    class UpdateDb(
        val md5DbPath: String,
        val sha1DbPath: String,
        val sha256DbPath: String
    ) : UpdateDbIntent()
}