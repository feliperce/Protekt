package io.github.feliperce.protekt.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import java.io.File

class UpdateAvDbJob : Job {
    override fun execute(context: JobExecutionContext) {
        val clamAvDbFolder = File("/home/felipe/clamav-db")
        clamAvDbFolder.mkdir()

        println("JOB START")
        execAndWait(
            command = "sigtool -u ~/clamav-db/main.cvd",
            dir = clamAvDbFolder
        )
    }
}

fun execAndWait(command: String, dir: File? = null): Int {
    return ProcessBuilder("/bin/sh", "-c", command)
        .redirectErrorStream(true)
        .inheritIO()
        .directory(dir)
        .start()
        .waitFor()
}

// sigtool -u ~/clamav-db/main.cvd