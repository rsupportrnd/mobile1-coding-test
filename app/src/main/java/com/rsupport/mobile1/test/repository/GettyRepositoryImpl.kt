package com.rsupport.mobile1.test.repository

import com.rsupport.mobile1.test.model.ImageSrc
import com.rsupport.mobile1.test.network.GettyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class GettyRepositoryImpl @Inject constructor(
    private val gettyService: GettyService
): GettyRepository {
    override fun getImages(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (cause: Throwable) -> Unit
    ): Flow<List<ImageSrc>> = flow {
        emit(gettyService.getImages(page = page))
    }
        .onStart { onStart()  }
        .onCompletion { onComplete() }
        .catch { cause -> onError(cause) }
        .flowOn(Dispatchers.IO)
}