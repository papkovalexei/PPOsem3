package ru.quipy.track.api

import org.bson.types.ObjectId
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.track.logic.Track
import java.util.*

const val CREATE_NEW_TRACK = "CREATE_NEW_TRACK"
const val CANCEL_TRACK = "CANCEL_TRACK"
const val CHANGE_TRACK = "CHANGE_TRACK"
const val CHANGE_TRACK_STATE = "CHANGE_TRACK_STATE"

@DomainEvent(name = CREATE_NEW_TRACK)
data class TrackCreatedEvent(
    val trackId: UUID,
    val slotId: String
) : Event<TrackAggregate>(
    name = CREATE_NEW_TRACK,
)

@DomainEvent(name = CANCEL_TRACK)
data class CancelTrackEvent(
    val trackId: UUID,
) : Event<TrackAggregate>(
    name = CANCEL_TRACK,
)

@DomainEvent(name = CHANGE_TRACK)
data class ChangeTrackEvent(
    val trackId: UUID,
    val slotId: String
) : Event<TrackAggregate>(
    name = CHANGE_TRACK,
)
@DomainEvent(name = CHANGE_TRACK_STATE)
data class ChangeTrackStateEvent(
    val trackId: UUID,
    val state: Track.TrackState
) : Event<TrackAggregate>(
    name = CHANGE_TRACK_STATE,
)