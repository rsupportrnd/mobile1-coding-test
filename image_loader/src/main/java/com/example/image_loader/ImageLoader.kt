package com.example.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.image_loader.cache.disk.DiskImageLruCache
import com.example.image_loader.cache.memory.MemoryImageLruCache
import com.example.image_loader.request.ImageRequest
import com.example.image_loader.request.ImageRequestJob
import com.example.image_loader.util.hashSHA256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import kotlin.coroutines.coroutineContext

class ImageLoader private constructor(
    private val memoryImageLruCache: MemoryImageLruCache,
    private val diskImageLruCache: DiskImageLruCache
) {

    fun enqueue(imageRequest: ImageRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            execute(imageRequest)
        }
    }

    private suspend fun execute(imageRequest: ImageRequest) {
        val requestDelegate = ImageRequestJob(
            imageRequest,
            coroutineContext.job,
            restartCallback = { request -> enqueue(request) },
        )
        try {
            requestDelegate.setNewRequest()
            requestDelegate.awaitStarted()
            imageRequest.listener?.onStart()

            val bitmap = withContext(Dispatchers.IO) {
                loadImage(imageRequest)
            }

            bitmap?.let {
                imageRequest.target.into(it)
                imageRequest.listener?.onSuccess(it)
            }
        } finally {
            requestDelegate.complete()
        }
    }

    /**
     * 1. Memory Cache에서 hit 시 해당 bitmap 사용
     * 2. Memory Cache miss 시 Disk Cache에서 hit 시 해당 bitmap 사용 후 Memory Cache에 저장
     * 3. Cache miss 시 url 소스를 통해 bitmap 생성 후 cache에 저장
     * */
    private suspend fun loadImage(request: ImageRequest): Bitmap? {
        val imageUrl = request.data
        val key = imageUrl.hashSHA256()

        memoryImageLruCache.get(key)?.let { bitmap ->
            return bitmap
        }

        diskImageLruCache.get(key)?.let { bitmap ->
            memoryImageLruCache.put(key, bitmap)
            return bitmap
        }

        return try {
            downloadImage(imageUrl)?.also { bitmap ->
                memoryImageLruCache.put(key, bitmap)
                diskImageLruCache.put(key, bitmap)
            }
        } catch (e: IOException) {
            request.listener?.onError(e)
            null
        }
    }

    private suspend fun downloadImage(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        val url = URL(imageUrl)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        BitmapFactory.decodeStream(connection.inputStream)
    }

    companion object {
        @Volatile
        private var instance: ImageLoader? = null

        fun getInstance(context: Context): ImageLoader {
            return instance ?: synchronized(this) {
                instance ?: ImageLoader(MemoryImageLruCache(), DiskImageLruCache(context))
                    .also { instance = it }
            }
        }
    }
}
