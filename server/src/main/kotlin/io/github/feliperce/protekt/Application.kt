package io.github.feliperce.protekt

import Greeting
import SvProperty
import SvProperty.SERVER_PORT
import io.github.feliperce.protekt.job.UpdateAvDbJob
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.quartz.JobBuilder
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import java.io.File

fun main() {

    val scheduler = StdSchedulerFactory.getDefaultScheduler()
    scheduler.start()

    val job = JobBuilder.newJob(UpdateAvDbJob::class.java)
        .withIdentity("comandoJob", "grupo")
        .build()

    val trigger = TriggerBuilder.newTrigger()
        .withIdentity("comandoTrigger", "grupo")
        .withSchedule(
            SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(50000)
                //.withIntervalInHours(12)
                .repeatForever())
        .build()

    scheduler.scheduleJob(job, trigger)

    embeddedServer(Netty, port = SERVER_PORT, host = "192.168.1.9", module = Application::module)
        .start(wait = true)


}

fun Application.module() {

    data class Aii(
        val abc: String = "dfsafsfsdfsfsdfsd"
    )

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        get("/aa") {
            val id = call.parameters["id"]
            val customer: Aii = Aii()
            call.respond(customer)
        }

        get("/getmd5db") {
            call.respondFile(
                File("/home/felipe/clamav-db/${SvProperty.MD5_BF_FILE_NAME}")
            )
        }

        get("/getsha1db") {
            call.respondFile(
                File("/home/felipe/clamav-db/${SvProperty.SHA1_BF_FILE_NAME}")
            )
        }

        get("/getsha256db") {
            call.respondFile(
                File("/home/felipe/clamav-db/${SvProperty.SHA256_BF_FILE_NAME}")
            )
        }
    }
}