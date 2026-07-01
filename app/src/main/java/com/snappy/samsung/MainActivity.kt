package com.snappy.samsung

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.snappy.samsung.ui.collection.CollectionScreen
import com.snappy.samsung.ui.home.HomeScreen
import com.snappy.samsung.ui.navigation.Screen
import com.snappy.samsung.ui.settings.SettingsScreen
import com.snappy.samsung.ui.theme.SnappyTheme
import com.snappy.samsung.ui.viewer.FullScreenViewerScreen
import com.snappy.samsung.viewmodel.*

class MainActivity : ComponentActivity() {

    private val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkThemeOverride by remember { mutableStateOf(systemTheme) }

            SnappyTheme(darkTheme = isDarkThemeOverride) {
                var permissionGranted by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            requiredPermission
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    permissionGranted = isGranted
                }

                LaunchedEffect(permissionGranted) {
                    if (!permissionGranted) {
                        permissionLauncher.launch(requiredPermission)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (permissionGranted) {
                        val navController = rememberNavController()
                        val app = application as SnappyApplication
                        val repository = app.repository

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route
                        ) {
                            composable(Screen.Home.route) {
                                val homeViewModel: HomeViewModel = viewModel(
                                    factory = HomeViewModelFactory(repository)
                                )
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onCollectionClick = { appName ->
                                        navController.navigate(Screen.Collection.createRoute(appName))
                                    },
                                    onSettingsClick = {
                                        navController.navigate(Screen.Settings.route)
                                    }
                                )
                            }

                            composable(
                                route = Screen.Collection.route,
                                arguments = listOf(navArgument("appName") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val appName = backStackEntry.arguments?.getString("appName") ?: "Others"
                                val collectionViewModel: CollectionViewModel = viewModel(
                                    factory = CollectionViewModelFactory(appName, repository)
                                )
                                CollectionScreen(
                                    appName = appName,
                                    viewModel = collectionViewModel,
                                    onBackClick = { navController.popBackStack() },
                                    onScreenshotClick = { id ->
                                        navController.navigate(Screen.FullScreenViewer.createRoute(appName, id))
                                    }
                                )
                            }

                            composable(
                                route = Screen.FullScreenViewer.route,
                                arguments = listOf(
                                    navArgument("appName") { type = NavType.StringType },
                                    navArgument("initialId") { type = NavType.LongType }
                                )
                            ) { backStackEntry ->
                                val appName = backStackEntry.arguments?.getString("appName") ?: "Others"
                                val initialId = backStackEntry.arguments?.getLong("initialId") ?: 0L
                                val viewerViewModel: ViewerViewModel = viewModel(
                                    factory = ViewerViewModelFactory(appName, initialId, repository)
                                )
                                FullScreenViewerScreen(
                                    viewModel = viewerViewModel,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable(Screen.Settings.route) {
                                val settingsViewModel: SettingsViewModel = viewModel(
                                    factory = SettingsViewModelFactory(repository)
                                )
                                SettingsScreen(
                                    viewModel = settingsViewModel,
                                    isDarkTheme = isDarkThemeOverride,
                                    onThemeChange = { isDarkThemeOverride = it },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    } else {
                        PermissionRationaleScreen(
                            onGrantClick = {
                                permissionLauncher.launch(requiredPermission)
                            },
                            onOpenSettingsClick = {
                                openSettings()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}

@Composable
fun PermissionRationaleScreen(
    onGrantClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Security,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Storage Permission Required",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Snappy needs read access to your device's photos and media to scan, filter, and display your screenshots locally. No files are uploaded to any server.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onGrantClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Grant Permission", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onOpenSettingsClick) {
            Text("Open Settings", fontSize = 14.sp)
        }
    }
}
