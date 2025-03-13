package com.accountmanager.smartledger.presentation

import PhotoPickerAndCameraScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.accountmanager.smartledger.presentation.screens.AccountScreen
import com.accountmanager.smartledger.presentation.screens.PdfViewerScreen
import com.accountmanager.smartledger.presentation.viewmodel.AccountViewModel
import com.accountmanager.smartledger.ui.theme.SmartLedgerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<AccountViewModel>()
        setContent {
            SmartLedgerTheme {
                Surface {
                    AppNavigator(this, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(context: Context, viewModel: AccountViewModel) {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf("accounts") }
    val systemUiController = rememberSystemUiController()

    val primaryColor = MaterialTheme.colorScheme.primary

    // Status Bar Color
    SideEffect {
        systemUiController.setStatusBarColor(primaryColor)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = getTitle(currentScreen)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, onScreenSelected = { currentScreen = it }, color = primaryColor)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavigationGraph(context, navController, viewModel)
        }
    }
}

@Composable
fun NavigationGraph(context: Context, navController: NavHostController, viewModel: AccountViewModel) {
    NavHost(navController, startDestination = "accounts") {
        composable("accounts") { AccountScreen(context, viewModel) }
        composable("pdfViewer") { PdfViewerScreen() }
        composable("imagePicker") { PhotoPickerAndCameraScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, onScreenSelected: (String) -> Unit, color: Color) {
    NavigationBar(containerColor = color) {
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "accounts",
            onClick = { navController.navigate("accounts"); onScreenSelected("accounts") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Accounts") },
            label = { Text("Accounts") }
        )
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "pdfViewer",
            onClick = { navController.navigate("pdfViewer"); onScreenSelected("pdfViewer") },
            icon = { Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF") },
            label = { Text("PDF Viewer") }
        )
        BottomNavigationItem(
            selected = navController.currentDestination?.route == "imagePicker",
            onClick = { navController.navigate("imagePicker"); onScreenSelected("imagePicker") },
            icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = "Image Picker") },
            label = { Text("Image Picker") }
        )
    }
}


fun getTitle(screen: String): String {
    return when (screen) {
        "accounts" -> "Accounts"
        "pdfViewer" -> "PDF Viewer"
        "imagePicker" -> "Image Picker"
        else -> "Smart Ledger"
    }
}
