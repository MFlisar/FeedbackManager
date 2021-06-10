package com.michaelflisar.feedbackmanager

import android.net.Uri
import java.io.File
import java.util.*

/**
 * Created by flisar on 08.01.2018.
 */

interface IFeedbackFile {
    val uri: Uri
    val cacheFileName: String
    val checkIfFileIsInCache: Boolean
}

sealed class FeedbackFile : IFeedbackFile {

    class DefaultName(override val uri: Uri) : FeedbackFile() {
        constructor(file: File) : this(Uri.fromFile(file))

        override val cacheFileName: String = File(uri.path).name

        override val checkIfFileIsInCache = true
    }

    class CustomName(override val uri: Uri, override val cacheFileName: String) : FeedbackFile() {
        constructor(file: File, cacheFileName: String) : this(
            Uri.fromFile(file),
            cacheFileName
        )

        override val checkIfFileIsInCache = false
    }

    class UniqueName(override val uri: Uri) : FeedbackFile() {
        constructor(file: File, cacheFileName: String) : this(Uri.fromFile(file))

        override val cacheFileName: String by lazy {
            val file = File(uri.path)
            var nameWithoutExtension = file.name
            var extension = ""
            val i = file.name.lastIndexOf('.')
            if (i > 0) {
                extension = "." + file.name.substring(i + 1)
                nameWithoutExtension = file.name.substring(0, i)
            }
            nameWithoutExtension + "_" + UUID.randomUUID().toString() + extension
        }

        override val checkIfFileIsInCache = false
    }
}