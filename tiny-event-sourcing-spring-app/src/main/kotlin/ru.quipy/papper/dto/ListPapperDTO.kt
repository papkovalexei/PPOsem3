package ru.quipy.papper.dto

import java.beans.ConstructorProperties

data class ListPapperDTO
@ConstructorProperties("name", "aggregateId", "count")
constructor(val name: String, val aggregateId: String, val count: Int)
