package com.rsupport.mobile1.test.domain.usecase

import com.rsupport.mobile1.test.domain.model.HtmlParseResult
import com.rsupport.mobile1.test.domain.model.MainList
import com.rsupport.mobile1.test.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {

    operator fun invoke(): Flow<HtmlParseResult<MainList>> = mainRepository.getMainList()
}