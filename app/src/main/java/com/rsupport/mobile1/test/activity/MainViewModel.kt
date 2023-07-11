package com.rsupport.mobile1.test.activity

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rsupport.mobile1.test.model.ImageSrc
import com.rsupport.mobile1.test.repository.GettyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val gettyRepository: GettyRepository
): ViewModel() {
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isError: MutableLiveData<Boolean> = MutableLiveData(false)
    private val imageFetchIndex: MutableStateFlow<Int> = MutableStateFlow(1)

    private val imageFlow: MutableStateFlow<List<ImageSrc>> = MutableStateFlow(emptyList())
    val imageLiveData = imageFlow.asLiveData()

    init {
        fetchImageList()
    }
    fun fetchImageList() {
        viewModelScope.launch {
            imageFetchIndex.flatMapLatest { page ->
                Timber.d("page index : $page")
                gettyRepository.getImages(
                    page = page,
                    onStart = {
                        isLoading.postValue(true)
                    },
                    onComplete = {
                        isLoading.postValue(false)
                        isError.postValue(false)
                    },
                    onError = {
                        Timber.d("Exception Occurred : $it")
                        isLoading.postValue(false)
                        isError.postValue(true)
                        coroutineContext.cancel()
                    })
            }.collectLatest {
                Timber.d("collected list : $it")
                imageFlow.value = it
            }
        }
    }

    @MainThread
    fun fetchNextImageList() {
        imageFetchIndex.value++
    }

    @MainThread
    fun fetchIndexInit() {
        imageFetchIndex.value = 0
    }
}