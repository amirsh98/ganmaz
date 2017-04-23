package ir.toolki.ganmaz

import ir.toolki.ganmaz.classification.DataSet
import ir.toolki.ganmaz.classification.Document
import ir.toolki.ganmaz.sample.News
import org.kohsuke.args4j.Option
import spark.Response
import spark.Spark.*


fun main(args: Array<String>) {
    val port = 1234
    val k = 5
    val dataset = DataSet(News.readSamples())
    port(port)

    fun doResponse(text: String, resp: Response): String {
        resp.status(200)
        resp.type("text/plain")
        val doc = object : Document {
            override val id = -1
            override val label = ""
            override fun body() = text
        }
        return dataset.classify(doc, k)
    }


    post("/classify") { req, resp -> doResponse(req.body(), resp) }
    get("/classify/:q") { req, resp -> doResponse(req.params(":q"), resp) }

}

class cmdOpts(
        @Option(name = "port", usage = "The port to listen")
        val port: Int = 5050,
        @Option(name = "port", usage = "The port to listen")
        json: String = ""
)