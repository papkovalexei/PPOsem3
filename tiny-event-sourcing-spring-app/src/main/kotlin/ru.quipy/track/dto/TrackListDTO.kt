package ru.quipy.track.dto

import ru.quipy.track.logic.Track
import java.beans.ConstructorProperties
import java.time.LocalDateTime

data class TrackListDTO
@ConstructorProperties("id", "state", "time")
constructor(val id: String, val state: Track.TrackState, val time: String)
