package ru.quipy.papper.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate
@AggregateType(aggregateEventsTableName = "papper")
class PapperAggregate: Aggregate