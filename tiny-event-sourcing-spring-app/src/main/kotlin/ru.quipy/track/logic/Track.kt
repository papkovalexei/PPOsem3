package ru.quipy.track.logic

import org.bson.types.ObjectId
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.slots.SlotsRepository
import ru.quipy.track.api.*
import java.util.*

class Track : AggregateState<UUID, TrackAggregate> {
    private lateinit var trackId: UUID
    private lateinit var slotId: String
    private lateinit var trackState: TrackState

    enum class TrackState {
        IN_DELIVERY,
        DELIVERED,
        CANCELED
    }

    override fun getId() = trackId

    fun getTrackState() = trackState

    fun getSlotId() = slotId

    fun createNewTrack(id: UUID = UUID.randomUUID(), slotId: String) : TrackCreatedEvent {
        return TrackCreatedEvent(trackId = id, slotId = slotId)
    }

    fun createCancelTrack(id: UUID) : CancelTrackEvent {
        return CancelTrackEvent(trackId = id)
    }

    fun createChangeTrack(id: UUID, slotId: String) : ChangeTrackEvent {
        return ChangeTrackEvent(trackId = id, slotId = slotId)
    }

    fun createChangeTrackState(id: UUID, state: TrackState) : ChangeTrackStateEvent {
        return ChangeTrackStateEvent(trackId = id, state = state)
    }

    @StateTransitionFunc
    fun createNewTrack(event: TrackCreatedEvent) {
        trackId = event.trackId
        slotId = event.slotId
        trackState = TrackState.IN_DELIVERY
    }

    @StateTransitionFunc
    fun createCancelTrack(event: CancelTrackEvent) {
        trackId = event.trackId
        trackState = TrackState.CANCELED
    }

    @StateTransitionFunc
    fun createChangeTrack(event: ChangeTrackEvent) {
        trackId = event.trackId
        slotId = event.slotId
    }

    @StateTransitionFunc
    fun createChangeTrackState(event: ChangeTrackStateEvent) {
        trackId = event.trackId
        trackState = event.state
    }
}