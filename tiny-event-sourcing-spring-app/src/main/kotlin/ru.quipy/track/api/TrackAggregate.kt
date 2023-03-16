package ru.quipy.track.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "tracks")
class TrackAggregate: Aggregate