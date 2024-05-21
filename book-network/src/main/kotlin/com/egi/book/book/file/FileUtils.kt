package com.egi.book.book.file

import com.egi.book.auth.AuthenticationController
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files

class FileUtils {
    companion object {
        fun readFileFromLocation(fileUrl: String?): ByteArray? {
            if (StringUtils.isBlank(fileUrl)) {
                return null
            }

            try {
                val filePath = fileUrl?.let { File(it).toPath() }
                return filePath?.let { Files.readAllBytes(it) }
            } catch (ex: IOException) {
                log.warn("No file found in the path {} ", fileUrl)
            }
            return null
        }

        private val log = LoggerFactory.getLogger(AuthenticationController::class.java)
    }
}
