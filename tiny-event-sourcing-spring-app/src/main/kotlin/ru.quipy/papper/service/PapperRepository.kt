package ru.quipy.papper.service

import org.springframework.data.mongodb.repository.MongoRepository

interface PapperRepository : MongoRepository<PapperMongo, String> {
    @org.springframework.lang.Nullable
    fun findOneByAggregateId(aggregateIdStr: String): PapperMongo
}