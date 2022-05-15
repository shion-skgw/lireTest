package com.skgw.lireTest.batch.index

import com.skgw.lireTest.config.LireConfig
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.nio.file.Path

/**
 * 画像管理ディレクトリ内のインデックスを作成する
 */
@Configuration
class CreateJobConfig {

    @Autowired
    lateinit var jobRepository: JobRepository
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory
    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory
    @Autowired
    lateinit var config: LireConfig

    @Bean
    fun indexCreateJobLauncher(): JobLauncher {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 1
        taskExecutor.initialize()

        val jobLauncher = SimpleJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(taskExecutor)
        return jobLauncher
    }

    @Bean
    fun indexCreateJob(): Job {
        return jobBuilderFactory["IndexCreateJob"]
            .incrementer(RunIdIncrementer())
            .start(indexCreateJobStep())
            .build()
    }

    private fun indexCreateJobStep(): Step {
        return stepBuilderFactory["IndexCreateJobStep"]
            .chunk<Path, Path>(30)
            .reader(indexCreateJobReader())
            .processor(indexCreateJobProcessor())
            .writer(indexCreateJobWriter())
            .build()
    }

    private fun indexCreateJobReader(): CreateReader {
        return CreateReader(config.imageStore)
    }

    private fun indexCreateJobProcessor(): ItemProcessor<Path, Path> {
        return ItemProcessor { return@ItemProcessor it }
    }

    private fun indexCreateJobWriter(): CreateWriter {
        return CreateWriter(config.index)
    }

}
