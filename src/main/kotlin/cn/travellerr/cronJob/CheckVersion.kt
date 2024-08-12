package cn.travellerr.cronJob

import cn.travellerr.utils.Log
import cn.travellerr.version.CheckLatestVersion
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory

fun cronJob() {


    val scheduler = StdSchedulerFactory.getDefaultScheduler()
    scheduler.start()

    val job = JobBuilder.newJob(CheckVersion::class.java)
        .withIdentity("checkRepoJob", "Favorability")
        .build()

    val trigger = TriggerBuilder.newTrigger()
        .withIdentity("checkRepo", "Favorability")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
        //.withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))  // USE FOR DEBUG
        .build()

    scheduler.scheduleJob(job, trigger)
    Log.info("定时任务已注册")
}

class CheckVersion : Job {
    override fun execute(context: JobExecutionContext) {
        CheckLatestVersion.init()
    }
}