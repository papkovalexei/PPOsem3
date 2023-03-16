package ru.quipy.admin.dto

import java.beans.ConstructorProperties

data class AddSlotsDTO
@ConstructorProperties("date")
constructor(val date: String)
