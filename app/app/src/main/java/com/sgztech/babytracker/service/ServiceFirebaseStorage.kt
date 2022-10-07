package com.sgztech.babytracker.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.sgztech.babytracker.arch.Result
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ServiceFirebaseStorage(
    private val firebaseStorage: FirebaseStorage,
) {

    suspend fun uploadPhoto(
        uriFile: Uri,
        fileName: String,
    ): Result<Uri, Throwable> {
        if (uriFile.toString().isEmpty())
            return Result.Failure(Throwable("Empty Uri"))
        val ref = firebaseStorage.getReference(fileName)
        val task = ref.putFile(uriFile)
        return generateUrlDownload(ref, task)
    }

    private suspend fun generateUrlDownload(
        reference: StorageReference,
        task: StorageTask<UploadTask.TaskSnapshot>,
    ): Result<Uri, Throwable> {
        return suspendCoroutine { continuation ->
            task.continueWithTask { taskExecuted ->
                if (taskExecuted.isSuccessful) {
                    reference.downloadUrl
                } else {
                    taskExecuted.exception?.let {
                        throw it
                    }
                }
            }.addOnCompleteListener { taskSuccess ->
                if (taskSuccess.isSuccessful) {
                    taskSuccess.result?.let { uri ->
                        continuation.resume(Result.Success(uri))
                    } ?: run {
                        continuation.resume(Result.Failure(Throwable("Unknown Error")))
                    }
                } else {
                    continuation.resume(Result.Failure(Throwable("Unknown Error")))
                }
            }.addOnFailureListener { exception ->
                continuation.resume(Result.Failure(exception))
            }
        }
    }
}