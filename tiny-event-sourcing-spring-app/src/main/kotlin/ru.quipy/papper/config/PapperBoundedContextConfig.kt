package ru.quipy.papper.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.logic.Papper
import java.util.*
@Configuration
class PapperBoundedContextConfig {
    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun papperEsService(): EventSourcingService<UUID, PapperAggregate, Papper> =
        eventSourcingServiceFactory.create()
}