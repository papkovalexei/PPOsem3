package ru.quipy.admin.dto

import ru.quipy.track.logic.Track
import java.beans.ConstructorProperties

data class ListTrackDTO
@ConstructorProperties("track_id", "state", "slot_id", "time")
constructor(val track_id: String, val state: Track.TrackState, val slot_id: String, val time: String)
