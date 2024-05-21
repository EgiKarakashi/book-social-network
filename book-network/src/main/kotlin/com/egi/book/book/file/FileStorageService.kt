package com.egi.book.book.file

import com.egi.book.auth.AuthenticationController
import com.egi.book.book.Book
import jakarta.annotation.Nonnull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileStorageService(
    @Value("\${application.file.upload.photos-output-path}")
    private val fileUploadPath: String,
) {
    fun saveFile(
        @Nonnull sourceFile: MultipartFile,
        @Nonnull book: Book?,
        @Nonnull userId: Int,
    ): String? {
        val fileUploadSubPath = "users" + File.separator + userId
        return uploadFile(sourceFile, fileUploadSubPath)
    }

    fun uploadFile(
        @Nonnull sourceFile: MultipartFile,
        @Nonnull fileUploadSubPath: String,
    ): String? {
        val finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath
        val targetFolder = File(finalUploadPath)

        if (!targetFolder.exists()) {
            val folderCreated = targetFolder.mkdirs()
            if (!folderCreated) {
                log.warn("Failed to create the target folder: $targetFolder")
                return null
            }
        }
        val fileExtension = getFileExtension(sourceFile.originalFilename)
        val targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension
        val targetPath = Paths.get(targetFilePath)
        try {
            Files.write(targetPath, sourceFile.bytes)
            log.info("File saved to: $targetFilePath")
            return targetFilePath
        } catch (ex: IOException) {
            log.error("File was not save $ex")
        }
        return null
    }

    private fun getFileExtension(fileName: String?): String {
        if (fileName === null || fileName.isEmpty()) {
            return ""
        }
        val lastDotIndex = fileName.lastIndexOf(".")
        if (lastDotIndex == -1) {
            return ""
        }
        return fileName.substring(lastDotIndex + 1).lowercase(Locale.getDefault())
    }

    companion object {
        private val log = LoggerFactory.getLogger(AuthenticationController::class.java)
    }
}
