package ir.toolki.ganmaz.sample

import ir.toolki.ganmaz.classification.Document
import com.google.common.io.CharStreams
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class News(val title: String,
           override val id: Int,
           @SerializedName("class")
           override val label: String,
           private val body: String) : Document {

    override fun body() = "$title\n$title\n$body"

    companion object Resources {
        fun readSamples(): List<News> {
            val gson = GsonBuilder().setLenient().create()
            var lines = CharStreams.readLines(News::class.java.getResourceAsStream("/sample.json").bufferedReader())
            // remove empty lines
            lines = lines.filter { it.trim().isNotEmpty() }.toList()
            return lines.map { gson.fromJson(it, News::class.java) }.toList()
        }
    }
}