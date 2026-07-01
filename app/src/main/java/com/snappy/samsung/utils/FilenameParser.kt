package com.snappy.samsung.utils

object FilenameParser {
    private val KNOWN_APPS = listOf(
        "Instagram", "WhatsApp", "Chrome", "YouTube", "Gallery",
        "Telegram", "Reddit", "X", "Facebook", "Snapchat",
        "TikTok", "Twitter", "Pinterest", "Messenger", "LinkedIn",
        "Gmail", "Maps", "Spotify", "Netflix"
    )

    fun parseAppName(filename: String): String {
        val nameWithoutExtension = filename.substringBeforeLast('.')
        
        // 1. Check standard Samsung pattern: Screenshot_YYYYMMDD_HHMMSS_AppName
        if (nameWithoutExtension.startsWith("Screenshot_", ignoreCase = true)) {
            val parts = nameWithoutExtension.split('_')
            if (parts.size >= 4) {
                val datePart = parts[1]
                val timePart = parts[2]
                val isSamsungPattern = datePart.all { it.isDigit() } && timePart.all { it.isDigit() } && datePart.length >= 6 && timePart.length >= 6
                if (isSamsungPattern) {
                    val appName = parts.subList(3, parts.size).joinToString("_").trim()
                    if (appName.isNotEmpty()) {
                        return appName
                    }
                }
            }
        }
        
        // 2. Fallback: Search filename for known app keywords (case-insensitive)
        for (app in KNOWN_APPS) {
            if (nameWithoutExtension.contains(app, ignoreCase = true)) {
                return app
            }
        }
        
        // Special case mappings (e.g. if file contains 'fb' -> 'Facebook')
        if (nameWithoutExtension.contains("fb_", ignoreCase = true) || nameWithoutExtension.contains("facebook", ignoreCase = true)) {
            return "Facebook"
        }
        if (nameWithoutExtension.contains("insta", ignoreCase = true)) {
            return "Instagram"
        }
        if (nameWithoutExtension.contains("wa_", ignoreCase = true)) {
            return "WhatsApp"
        }
        
        return "Others"
    }
}
