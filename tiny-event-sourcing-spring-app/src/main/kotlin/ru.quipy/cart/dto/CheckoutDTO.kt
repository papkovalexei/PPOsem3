package ru.quipy.cart.dto

import java.beans.ConstructorProperties

data class CheckoutDTO
@ConstructorProperties("id")
constructor(val id: String)
