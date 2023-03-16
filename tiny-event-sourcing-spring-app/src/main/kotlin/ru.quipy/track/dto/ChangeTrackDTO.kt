package ru.quipy.track.dto

import java.beans.ConstructorProperties

data class ChangeTrackDTO
@ConstructorProperties("id", "slotId")
constructor(val id: String, val slotId: String)
