package ru.quipy.papper.logic

import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.api.PapperCreatedEvent
import ru.quipy.papper.api.UpdatePapperEvent
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.api.TrackCreatedEvent
import java.util.*

class Papper : AggregateState<UUID, PapperAggregate> {
    private lateinit var papperId: UUID
    private var count: Int = 0

    override fun getId() = papperId
    fun getCount() = count

    fun createNewPapper(id: UUID = UUID.randomUUID(), count: Int) : PapperCreatedEvent {
        return PapperCreatedEvent(id, count)
    }

    fun updatePapper(id: UUID, count: Int) : UpdatePapperEvent {
        return UpdatePapperEvent(id, count)
    }

    @StateTransitionFunc
    fun updatePapper(event: UpdatePapperEvent) {
        papperId = event.papperId
        count = event.count
    }

    @StateTransitionFunc
    fun createNewPapper(event: PapperCreatedEvent) {
        papperId = event.papperId
        count = event.count
    }
}