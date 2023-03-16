package ru.quipy.admin.dto

import java.beans.ConstructorProperties

data class UpdatePapperDTO
@ConstructorProperties("aggregateId", "count")
constructor(val aggregateId: String, val count: Int)
