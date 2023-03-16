package ru.quipy.slots

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SlotsRepository: MongoRepository<SlotsMongo, String> {
    @org.springframework.lang.Nullable
    fun findOneById(id: ObjectId): SlotsMongo
}