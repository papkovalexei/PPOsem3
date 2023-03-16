package ru.quipy.papper.api

import ru.quipy.cart.api.CartAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CREATE_NEW_PAPPER = "CREATE_NEW_PAPPER"
const val UPDATE_PAPPER = "UPDATE_PAPPER"

@DomainEvent(name = CREATE_NEW_PAPPER)
data class PapperCreatedEvent(
    val papperId: UUID,
    val count: Int
) : Event<PapperAggregate>(
    name = CREATE_NEW_PAPPER,
)

@DomainEvent(name = UPDATE_PAPPER)
data class UpdatePapperEvent(
    val papperId: UUID,
    val count: Int
) : Event<PapperAggregate>(
    name = UPDATE_PAPPER,
)