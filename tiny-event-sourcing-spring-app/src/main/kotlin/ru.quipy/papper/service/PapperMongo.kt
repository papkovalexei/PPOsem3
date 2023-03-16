package ru.quipy.papper.service

import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId
import java.util.*

data class PapperMongo (
    @MongoId(value = FieldType.STRING)
    val aggregateIdStr: String,
    val aggregateId: UUID,
    val name: String,
)