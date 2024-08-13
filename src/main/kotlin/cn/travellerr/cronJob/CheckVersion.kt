package cn.travellerr.cronJob

import cn.travellerr.utils.Log
import cn.travellerr.version.CheckLatestVersion
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory

/**
 * 正确的，中肯的，一针见血的。
 * 版本检查应该在启动时检查而不是每小时检查一次浪费系统资源
 * 鬼知道我之前怎么想的
 *
 * @Author: Traveller
 */
@Deprecated("2024-08-13")
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

@Deprecated("2024-08-13")
class CheckVersion : Job {
    override fun execute(context: JobExecutionContext) {
        CheckLatestVersion.init()
    }
}