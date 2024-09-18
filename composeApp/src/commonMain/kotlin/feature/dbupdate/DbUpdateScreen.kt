package feature.dbupdate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import feature.dbupdate.state.UpdateDbIntent
import feature.dbupdate.viewmodel.DbUpdateViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DbUpdateScreen() {
    val dbUpdateViewModel: DbUpdateViewModel = koinViewModel()

    val dbUpdateUiState by dbUpdateViewModel.updateDbState.collectAsState()

    val dbPath = "/data/data/io.github.feliperce.protekt/cache/"

    DbUpdateContent(
        onUpdateButtonClick = {
            dbUpdateViewModel.sendIntent(
                UpdateDbIntent.UpdateDb(
                    md5DbPath = "${dbPath}md5bf.bin",
                    sha1DbPath = "${dbPath}sha1bf.bin",
                    sha256DbPath = "${dbPath}sha256bf.bin"
                )
            )
        }
    )

    if (dbUpdateUiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DbUpdateContent(
    onUpdateButtonClick: () -> Unit
) {

    Column {
        Button(
            content = {
                Text("Update DB")
            },
            onClick = {
                onUpdateButtonClick()
            }
        )
    }
}