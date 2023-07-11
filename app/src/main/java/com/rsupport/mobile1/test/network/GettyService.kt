package com.rsupport.mobile1.test.network

import com.rsupport.mobile1.test.model.ImageSrc
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class GettyService {
    val BASE_URL = "https://www.gettyimages.com"

    fun getImages(page: Int): List<ImageSrc> {
        val doc = Jsoup.connect("$BASE_URL/photos/collaboration?assettype=image&sort=mostpopular&phrase=collaboration&license=rf,rm&page=$page").get()
        val contents = doc
            .select("body > div.content_wrapper > section > div > main > div > div > div:nth-child(4) > div.CUAucfZwr8YyEhr4USsh > div.zF13TZBfnku4pwItorUU")
            .select("img")

        return contents.map{ element -> ImageSrc(element.attr("src")) }
    }
}