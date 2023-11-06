package com.michaelflisar.feedbackmanager

import android.net.Uri
import java.io.File

/**
 * Created by flisar on 08.01.2018.
 */

class FeedbackFile(
    val uri: Uri,
    val cacheFileName: String = uri.lastPathSegment ?: "feedback"
) {
    constructor(file: File, cacheFileName: String = file.name) : this(
        Uri.fromFile(file),
        cacheFileName
    )
}