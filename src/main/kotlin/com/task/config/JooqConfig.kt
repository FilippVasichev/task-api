package zionweeds.com.task.config

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class JooqConfig(
    private val dataSource: DataSource,
) {
    @Bean
    fun jooqDSLContext(): DSLContext = DSL.using(dataSource, SQLDialect.POSTGRES).dsl()
}
