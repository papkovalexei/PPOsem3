package ru.quipy.cart.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.quipy.cart.api.CartAggregate
import ru.quipy.cart.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.core.EventSourcingServiceFactory
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.logic.User
import java.util.*

@Configuration
class CartBoundedContextConfig {

    @Autowired
    private lateinit var eventSourcingServiceFactory: EventSourcingServiceFactory

    @Bean
    fun cartEsService(): EventSourcingService<UUID, CartAggregate, Cart> =
        eventSourcingServiceFactory.create()
}
