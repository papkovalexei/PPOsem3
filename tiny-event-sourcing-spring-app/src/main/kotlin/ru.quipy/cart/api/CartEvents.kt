package ru.quipy.cart.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val CREATE_NEW_CART = "CREATE_NEW_CART"
const val ADD_PAPPER = "ADD_PAPPER"
const val DELETE_PAPPER = "DELETE_PAPPER"

@DomainEvent(name = CREATE_NEW_CART)
data class CartCreatedEvent(
    val cartId: UUID,
) : Event<CartAggregate>(
    name = CREATE_NEW_CART,
)

@DomainEvent(name = ADD_PAPPER)
data class CartAddPapperEvent(
    val cartId: UUID,
    val papperId: UUID
) : Event<CartAggregate>(
    name = ADD_PAPPER,
)

@DomainEvent(name = DELETE_PAPPER)
data class CartDeletePapperEvent(
    val cartId: UUID,
    val papperId: UUID
) : Event<CartAggregate>(
    name = DELETE_PAPPER,
)