package tga.workload

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class WorkloadProducer(
    val threadsNumber: Int,
    val threadsStartDelayMilliseconds: Long,
    val requestsNumber: Int,
    val requestsDelayMilliseconds: Long
) {

    private val log = LoggerFactory.getLogger(this::class.qualifiedName)

    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    fun runWorkload() = runBlocking {
        for (threadN in 1..threadsNumber) {
            async { requestsThread(threadN) }
            delay(threadsStartDelayMilliseconds)
        }

    }

    private suspend fun requestsThread(threadIndex: Int) = runBlocking {
        fun info(msg: String) = log.info("${threadIndex.toString().padStart(2)}   : $msg")
        fun error(t: Throwable) = log.error("${threadIndex.toString().padStart(2)}   : ",t)
        info("Starting the thread")
        try {
            for (i in 1..requestsNumber) {
                async { req(threadIndex, i) }
                delay(requestsDelayMilliseconds)
            }
        } catch (t: Throwable) {
            error(t)
        } finally {
            info("Ending the thread")
        }
    }

    private suspend fun req(threadIndex: Int, i: Int) {
        fun info(msg: String) = log.info("${threadIndex.toString().padStart(2)}.${i.toString().padEnd(2)}: $msg")
        fun error(t: Throwable) = log.error("${threadIndex.toString().padStart(2)}.${i.toString().padEnd(2)}: ",t)
        info("Starting the request -------------------------------")
        try {
            client.get("https://ya.ru?search=$i")
        } catch (t: Throwable) {
            error(t)
        } finally {
            info("Ending the request")
        }


    }
}