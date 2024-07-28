package sparespark.middleman.common

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit =
    suspendCoroutine { continuation ->
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(task.exception!!)
            }
        }
    }

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

internal suspend fun DatabaseReference.awaitQueryTaskResult(
    brandId: String,
    refQuery: String
): DataSnapshot? =
    suspendCancellableCoroutine { continuation ->
        val query = this.orderByChild(refQuery).equalTo(brandId)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                val exception = when (error.toException()) {
                    is FirebaseException -> error.toException()
                    else -> Exception("The Firebase call for reference $this was cancelled")
                }
                continuation.resumeWithException(exception)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    continuation.resume(snapshot)

                } catch (exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            }
        }
        continuation.invokeOnCancellation { query.removeEventListener(listener) }
        query.addListenerForSingleValueEvent(listener)
    }

