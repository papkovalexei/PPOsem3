package ru.quipy.user.logic

import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.user.api.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class User : AggregateState<UUID, UserAggregate> {
    private lateinit var userId: UUID
    private lateinit var cartId: UUID
    private var trackerIds = ArrayList<UUID>()

    override fun getId() = userId
    fun getCart() = cartId
    fun existCart(): Boolean {
        return this::cartId.isInitialized
    }

    fun getTracks() = trackerIds

    fun resetField(target: Any, fieldName: String) {
        val field = target.javaClass.getDeclaredField(fieldName)

        with (field) {
            isAccessible = true
            set(target, null)
        }
    }


    fun createNewUser(id: UUID = UUID.randomUUID()) : UserCreatedEvent {
        return UserCreatedEvent(id)
    }

    fun createNewCart(id: UUID, cartId: UUID): UserCreatedCartEvent {
        return UserCreatedCartEvent(userId = id, cartId = cartId)
    }

    fun createResetCart(id: UUID, cartId: UUID): UserResetCartEvent {
        return UserResetCartEvent(userId = id, cartId = cartId)
    }
    fun createAddTrack(id: UUID, trackId: UUID): UserAddTrackEvent {
        return UserAddTrackEvent(userId = id, trackId = trackId)
    }
    @StateTransitionFunc
    fun createNewUser(event: UserCreatedEvent) {
        userId = event.userId
    }

    @StateTransitionFunc
    fun createNewCart(event: UserCreatedCartEvent) {
        userId = event.userId
        cartId = event.cartId
    }

    @StateTransitionFunc
    fun createResetCart(event: UserResetCartEvent) {
        userId = event.userId
        resetField(this, "cartId")
    }

    @StateTransitionFunc
    fun createAddTrack(event: UserAddTrackEvent) {
        userId = event.userId
        trackerIds.add(event.trackId)
    }
}