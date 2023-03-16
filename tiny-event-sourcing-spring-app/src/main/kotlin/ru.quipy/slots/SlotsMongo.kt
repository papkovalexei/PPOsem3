package ru.quipy.slots

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId
import java.util.*

@Document
data class SlotsMongo(
    @Id
    val id: ObjectId = ObjectId.get(),
    val time: String,
    var busy: Boolean
)
