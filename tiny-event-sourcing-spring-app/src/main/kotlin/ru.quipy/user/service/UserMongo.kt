package ru.quipy.user.service

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId
import java.util.UUID

@Document
data class UserMongo(
    @MongoId(value = FieldType.STRING)
    val email: String,

    val password: String,
    val aggregateId: UUID,
    val role: String
)
