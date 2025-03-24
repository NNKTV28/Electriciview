package com.nnktv28.elecriciview

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import androidx.documentfile.provider.DocumentFile
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import android.content.Context
import android.net.Uri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ElectricityApp()
                }
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ElectricityApp() {
    var selectedFolder by remember { mutableStateOf<DocumentFile?>(null) }
    var fileList by remember { mutableStateOf(listOf<DocumentFile>()) }
    var selectedFile by remember { mutableStateOf<DocumentFile?>(null) }
    val context = LocalContext.current as Activity

    // Load saved folder URI on first composition
    LaunchedEffect(Unit) {
        val sharedPrefs = context.getSharedPreferences("ElectricityAppPrefs", Context.MODE_PRIVATE)
        val savedFolderUri = sharedPrefs.getString("lastFolderUri", null)
        savedFolderUri?.let { uriString ->
            val uri = Uri.parse(uriString)
            val folder = DocumentFile.fromTreeUri(context, uri)
            selectedFolder = folder
            fileList = folder?.listFiles()?.filter {
                it.name?.endsWith(".json") == true
            }?.sortedByDescending { it.lastModified() } ?: emptyList()
        }
    }

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            val folder = DocumentFile.fromTreeUri(context, uri)
            selectedFolder = folder
            Log.d("FolderPath", "Selected folder: ${folder?.uri}")

            // Save the selected folder URI
            context.getSharedPreferences("ElectricityAppPrefs", Context.MODE_PRIVATE)
                .edit()
                .putString("lastFolderUri", uri.toString())
                .apply()

            fileList = folder?.listFiles()?.filter {
                it.name?.endsWith(".json") == true
            }?.sortedByDescending { it.lastModified() } ?: emptyList()

            Log.d("FileList", "Files found: ${fileList.joinToString { it.name ?: "unnamed" }}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedFile == null) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Button(
                    onClick = { folderPickerLauncher.launch(null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        "Seleccionar Carpeta",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fileList) { file ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedFile = file },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                file.name ?: "unnamed",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        } else {
            ElectricityDetailsScreen(file = selectedFile!!, onBack = { selectedFile = null })
        }
    }
}

@Composable
fun ElectricityDetailsScreen(file: DocumentFile, onBack: () -> Unit) {
    val context = LocalContext.current
    val jsonData = remember(file) {
        context.contentResolver.openInputStream(file.uri)?.use {
            it.bufferedReader().readText().let { text -> JSONObject(text) }
        } ?: JSONObject()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ElevatedButton(
            onClick = onBack,
            modifier = Modifier.padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Volver")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(jsonData.keys().asSequence().toList().sorted()) { key ->
                val data = jsonData.getJSONObject(key)
                val priceColor = when (data.getString("horario")) {
                    "low" -> Color.Green
                    "default" -> Color.Yellow
                    "high" -> Color.Red
                    else -> MaterialTheme.colorScheme.primary
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${data.getString("precio")} €/kWh",
                            style = MaterialTheme.typography.bodyLarge,
                            color = priceColor
                        )
                    }
                }
            }
        }
    }
}