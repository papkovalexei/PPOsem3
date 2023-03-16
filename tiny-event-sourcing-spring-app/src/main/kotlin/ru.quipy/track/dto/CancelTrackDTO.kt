package ru.quipy.track.dto

import java.beans.ConstructorProperties

data class CancelTrackDTO
@ConstructorProperties("id")
constructor(val id: String)
