package ru.quipy.cart.dto

import org.bson.types.ObjectId
import java.beans.ConstructorProperties
import java.time.LocalDateTime

data class SlotsDTO
@ConstructorProperties("id", "time")
constructor(val id: String, val time: LocalDateTime)
