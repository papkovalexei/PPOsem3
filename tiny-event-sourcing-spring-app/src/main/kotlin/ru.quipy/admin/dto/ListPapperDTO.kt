package ru.quipy.admin.dto

import java.beans.ConstructorProperties

data class ListPapperDTO
@ConstructorProperties("name", "count")
constructor(val name: String, val count: Int, val aggregateId: String)
