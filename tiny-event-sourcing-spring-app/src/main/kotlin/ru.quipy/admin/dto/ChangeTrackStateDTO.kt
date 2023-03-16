package ru.quipy.admin.dto

import java.beans.ConstructorProperties

data class ChangeTrackStateDTO
@ConstructorProperties("id", "state")
constructor(val id: String, val state: String)
