package ru.quipy.cart.dto

import java.beans.ConstructorProperties

data class ListCartDTO
@ConstructorProperties("name", "count")
constructor(val name: String, val count: Int)

data class ListCartWithIdDTO(
    val id: String,
    val items: ArrayList<ListCartDTO>
)