package io.github.feliperce.protekt

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import feature.dbupdate.DbUpdateScreen
import feature.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            context.requestStoragePermissions {

            }
            DbUpdateScreen()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}