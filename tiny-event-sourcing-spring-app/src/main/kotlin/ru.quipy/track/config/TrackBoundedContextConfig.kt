package ru.quipy.track.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.logic.Track
import java.util.*
@Configuration
class TrackBoundedContextConfig {
    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun trackEsService(): EventSourcingService<UUID, TrackAggregate, Track> =
        eventSourcingServiceFactory.create()
}