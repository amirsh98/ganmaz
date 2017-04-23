package ir.toolki.ganmaz.classification

import ir.toolki.ganmaz.sample.News
import com.google.common.math.Stats
import org.junit.Test
import org.assertj.core.api.Assertions.*
import java.util.*


class KotlinJunitTest {

    @Test
    fun randomPrecisionTest() {
        val docs = News.readSamples()
        val list = mutableListOf<Double>()
        for (i in 1..10) {
            Collections.shuffle(docs)
            val train = docs.subList(0, docs.size * 2 / 3)
            val test = docs.subList(docs.size * 2 / 3, docs.size)
            val precision = DataSet.evaluate(train, test)
            list.add(precision)
            println("ROUND-$i: The precision is $precision")
        }

        val stats = Stats.of(list)
        println("AVERAGE PRECISION: ${stats.mean()} \u00B1 ${stats.sampleStandardDeviation()}")
        println("MAX PRECISION: ${stats.max()}")
        println("MIN PRECISION: ${stats.min()}")
        assertThat(stats.mean()).isGreaterThan(0.85)
    }

    @Test
    fun tenFoldTest() {
        val docs = News.readSamples()
        val list = mutableListOf<Double>()
        Collections.shuffle(docs)
        val folds = mutableListOf<List<Document>>()
        for (j in 0..9)
            folds.add(docs.subList(j * docs.size / 10, (j + 1) * docs.size / 10))

        for (i in 0..9) {
            val train = mutableListOf<Document>()
            for (j in 0..9)
                if (j != i)
                    train.addAll(folds[j])
            val test = folds[i]
            val precision = DataSet.evaluate(train, test)
            list.add(precision)
            println("FOLD-$i: The precision is $precision")
        }

        val stats = Stats.of(list)
        println("10-FOLD PRECISION: ${stats.mean()} \u00B1 ${stats.sampleStandardDeviation()}")
        assertThat(stats.mean()).isGreaterThan(0.85)
    }

}