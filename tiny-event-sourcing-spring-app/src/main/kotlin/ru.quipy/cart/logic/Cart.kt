package ru.quipy.cart.logic

import ru.quipy.cart.api.CartAddPapperEvent
import ru.quipy.cart.api.CartAggregate
import ru.quipy.cart.api.CartCreatedEvent
import ru.quipy.cart.api.CartDeletePapperEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.user.api.UserCreatedEvent
import java.util.*
import kotlin.collections.HashMap

class Cart : AggregateState<UUID, CartAggregate> {
    private lateinit var cartId: UUID
    private var pappers = HashMap<UUID, Int>()

    override fun getId() = cartId

    fun getPappers() = pappers

    fun createNewCart(id: UUID = UUID.randomUUID()) : CartCreatedEvent {
        return CartCreatedEvent(id)
    }

    fun createAddPapper(id: UUID, papperId: UUID) : CartAddPapperEvent {
        return CartAddPapperEvent(cartId = id, papperId = papperId)
    }

    fun createDeletePapper(id: UUID, papperId: UUID) : CartDeletePapperEvent {
        return CartDeletePapperEvent(cartId = id, papperId = papperId)
    }

    @StateTransitionFunc
    fun createNewCart(event: CartCreatedEvent) {
        cartId = event.cartId
    }

    @StateTransitionFunc
    fun createAddPapper(event: CartAddPapperEvent) {
        cartId = event.cartId

        if (pappers.containsKey(event.papperId)) {
            pappers[event.papperId] = pappers[event.papperId]!! + 1
        } else {
            pappers.put(event.papperId, 1)
        }
    }

    @StateTransitionFunc
    fun createDeletePapper(event: CartDeletePapperEvent) {
        cartId = event.cartId

        if (pappers.containsKey(event.papperId) && pappers[event.papperId]!! > 0) {
            if (pappers[event.papperId]!! == 1) {
                pappers.remove(event.papperId)
            } else {
                pappers[event.papperId] = pappers[event.papperId]!! - 1
            }
        }
    }
}