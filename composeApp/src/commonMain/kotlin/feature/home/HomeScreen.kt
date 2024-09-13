package feature.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import ca.gosyer.appdirs.AppDirs
import data.client
import data.remote.AvService
import extensions.saveBloomFilter
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

@Composable
fun HomeScreen() {

    Text("dasdasdasdasdas")
    val avService = AvService()

    val scope = rememberCoroutineScope()

    val path = Path("/data/data/io.github.feliperce.protekt/cache/dasdasdsa.bin")

    val appDirs = AppDirs("io.github.feliperce.protekt", "feliperce")

    //SystemFileSystem.sink(Path(appDirs.getUserCacheDir()))

    /*SystemFileSystem.sink(path, false).buffered().use { sink ->
        println("ADF")
        sink.writeInt(34124)
    }*/

    scope.launch {

        println("UUUUUUUUUUUUUUUUUUUUUU: ${path}")
        avService.getMd5Db(
            onRead = {
                SystemFileSystem.sink(Path(appDirs.getUserCacheDir()+"/dasdas.bin"), false).buffered().use { sink ->
                    println("ADFFASFASDJMOIKFSJDOIFJOISD JOIFJSIODFJIOS DJOIF JSDIOFJIOSDJIOFSDJIOFJIOSD")
                    sink.write(it)
                }
            }
        )
    }
}

@Composable
fun HomeContent() {

}