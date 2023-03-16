package ru.quipy.admin.dto

import java.beans.ConstructorProperties

data class NewPapperDTO
@ConstructorProperties("name", "count")
constructor(val name: String, val count: Int)
