package io.github.feliperce.protekt

import Greeting
import SERVER_PORT
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
                .withIntervalInSeconds(10)
                //.withIntervalInHours(12) // Executar a cada 12 horas
                .repeatForever())
        .build()

    scheduler.scheduleJob(job, trigger)

    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)


}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
}