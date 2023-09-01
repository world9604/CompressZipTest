package com.example.compressziptest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compressziptest.ui.theme.CompressZipTestTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.FileHeader
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File
import java.util.zip.ZipException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            val file = File("storage/emulated/0/test123")
            val zipFile = compressZip(file, "1234")
            val unzippedFile = decompressZip(zipFile,
                "storage/emulated/0", "1234")
        }

        setContent {
            CompressZipTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun compressZip(file: File, password: String): File {
        val zipFile = ZipFile("${file.absolutePath}.zip")
        val zipParameters = ZipParameters()
        zipParameters.compressionLevel = CompressionLevel.NORMAL
        zipParameters.compressionMethod = CompressionMethod.DEFLATE

        if (password.isNotBlank()) {
            zipFile.setPassword(password.toCharArray())
            zipParameters.isEncryptFiles = true
            zipParameters.encryptionMethod = EncryptionMethod.ZIP_STANDARD
        }

        zipFile.addFolder(file, zipParameters)
        file.deleteRecursively()
        return zipFile.file
        /*
        mediaScanFile(mBackupFile)
        mediaScanFile(zipFile.file)
        */
    }

    private fun decompressZip(file: File, destinationPath: String, password: String): File {
        return try {
            val zipFile = ZipFile(file)
            if (zipFile.isEncrypted) zipFile.setPassword(password.toCharArray())
            zipFile.extractAll(destinationPath)
            File(destinationPath, file.name)
        } catch (e: ZipException) {
            e.printStackTrace()
            file
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompressZipTestTheme {
        Greeting("Android")
    }
}