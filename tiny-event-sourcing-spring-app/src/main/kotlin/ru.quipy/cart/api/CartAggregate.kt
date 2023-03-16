package ru.quipy.cart.api

import ru.quipy.core.annotations.AggregateType
import ru.quipy.domain.Aggregate

@AggregateType(aggregateEventsTableName = "carts")
class CartAggregate: Aggregate