package com.developerxy.githubapp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
fun Date.toFormattedString() : String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this)
}