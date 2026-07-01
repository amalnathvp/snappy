package com.snappy.samsung.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Collection : Screen("collection/{appName}") {
        fun createRoute(appName: String): String = "collection/$appName"
    }
    data object FullScreenViewer : Screen("viewer/{appName}/{initialId}") {
        fun createRoute(appName: String, initialId: Long): String = "viewer/$appName/$initialId"
    }
    data object Settings : Screen("settings")
}
