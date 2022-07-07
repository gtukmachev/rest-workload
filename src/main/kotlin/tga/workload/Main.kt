package tga.workload

import io.ktor.client.*
import io.ktor.client.request.*

fun main() {
    val workloadProducer = object : WorkloadProducer(
        threadsNumber = 2,
        threadsStartDelayMilliseconds = 5,
        requestsNumber = 3,
        requestsDelayMilliseconds = 7
    ) {
        override suspend fun doRequest(client: HttpClient, nThread: Int, nRequest: Int, log: (String) -> Unit) {

            client.get("https://ya.ru?search=$nThread.$nRequest")

        }
    }

    workloadProducer.runWorkload()

}

