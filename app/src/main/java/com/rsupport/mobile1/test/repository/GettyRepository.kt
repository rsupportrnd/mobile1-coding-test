package com.rsupport.mobile1.test.repository

import com.rsupport.mobile1.test.model.ImageSrc
import kotlinx.coroutines.flow.Flow

interface GettyRepository {
    fun getImages(page: Int, onStart: () -> Unit, onComplete: () -> Unit, onError: (cause: Throwable) -> Unit): Flow<List<ImageSrc>>
}