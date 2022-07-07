package tga.workload

fun main() {
    val workloadProducer = WorkloadProducer(
        threadsNumber = 2,
        threadsStartDelayMilliseconds = 10,
        requestsNumber = 5,
        requestsDelayMilliseconds = 100
    )

    workloadProducer.runWorkload()

}

