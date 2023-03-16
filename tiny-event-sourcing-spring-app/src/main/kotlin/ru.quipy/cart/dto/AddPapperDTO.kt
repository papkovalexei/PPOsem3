package ru.quipy.cart.dto

import java.beans.ConstructorProperties

data class AddPapperDTO
@ConstructorProperties("aggregateId")
constructor(val aggregateId: String)