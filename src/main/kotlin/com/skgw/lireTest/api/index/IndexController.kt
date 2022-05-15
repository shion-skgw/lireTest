package com.skgw.lireTest.api.index

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date

@RestController
class IndexController {

    @Autowired
    @Qualifier("indexCreateJobLauncher")
    lateinit var jobLauncher: JobLauncher
    @Autowired
    @Qualifier("indexCreateJob")
    lateinit var indexCreateJob: Job

    @GetMapping("/index")
    private fun index() {
        // パラメーター
        val param = JobParametersBuilder()
            .addDate("req", Date())
            .toJobParameters()

        // 実行
        jobLauncher.run(indexCreateJob, param)
    }

}
