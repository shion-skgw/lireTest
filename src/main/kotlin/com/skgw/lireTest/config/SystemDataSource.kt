package com.skgw.lireTest.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class SystemDataSource {

    /**
     * システム用（JobRepository）の DataSource を取得する
     *
     * @return システム用 DataSource
     */
    @Bean("SystemDataSource")
    @ConfigurationProperties(prefix = "system.jdbc")
    fun systemDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

}
