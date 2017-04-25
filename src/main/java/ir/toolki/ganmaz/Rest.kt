package ir.toolki.ganmaz

import ir.toolki.ganmaz.classification.DataSet
import ir.toolki.ganmaz.classification.Document
import ir.toolki.ganmaz.sample.News
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import spark.Response
import spark.Spark.*


fun main(args: Array<String>) {
    val opts = CmdOpts()
    val parser = CmdLineParser(opts)
    try {
        parser.parseArgument(*args)
    } catch (e: CmdLineException) {
        parser.printUsage(System.err)
    }

    port(opts.port)

    var dataset: DataSet
    if (opts.json.isEmpty()) {
        println("No JSON given! Reading default json....")
        dataset = DataSet(News.readSamples())
    } else
        dataset = DataSet(News.readFile(opts.json))


    fun doResponse(text: String, resp: Response): String {
        resp.status(200)
        resp.type("text/plain")
        val doc = object : Document {
            override val id = -1
            override val label = ""
            override fun body() = text
        }
        return dataset.classify(doc, opts.k)
    }


    post("/classify") { req, resp -> doResponse(req.body(), resp) }
    get("/classify/:q") { req, resp -> doResponse(req.params(":q"), resp) }

}

class CmdOpts(
        @Option(name = "port", usage = "The port to listen")
        val port: Int = 5050,
        @Option(name = "k", usage = "Number of Neighbors for classifier.")
        val k: Int = 5,
        @Option(name = "json", usage = "The path of json file")
        val json: String = ""
)