package ru.quipy.admin.controller

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.admin.dto.*
import ru.quipy.core.EventSourcingService
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.logic.Papper
import ru.quipy.papper.service.PapperMongo
import ru.quipy.papper.service.PapperRepository
import ru.quipy.slots.SlotsMongo
import ru.quipy.slots.SlotsRepository
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.logic.Track
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.logic.User
import ru.quipy.user.service.UserMongo
import ru.quipy.user.service.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/admin")
class AdminController (
    val userEsService: EventSourcingService<UUID, UserAggregate, User>,
    val trackEsService: EventSourcingService<UUID, TrackAggregate, Track>,
    val papperEsService: EventSourcingService<UUID, PapperAggregate, Papper>,
    val usersRepository: UserRepository,
    val papperRepository: PapperRepository,
    val slotsRepository: SlotsRepository
) {
    @PostMapping("/new_papper")
    fun addPapper(@RequestBody newPapperDTO: NewPapperDTO): Any {
        val id = papperEsService.create { it.createNewPapper(count = newPapperDTO.count) }.papperId
        return papperRepository.save(PapperMongo(
            name = newPapperDTO.name,
            aggregateId = id,
            aggregateIdStr = id.toString()
            ))
    }

    @PostMapping("/update_papper")
    fun updatePapper(@RequestBody updatePapperDTO: UpdatePapperDTO): Any {
        var id = UUID.fromString(updatePapperDTO.aggregateId)
        return papperEsService.update(id){ it.updatePapper(id = id, count = updatePapperDTO.count) }
    }

    @PostMapping("/add_slots")
    fun addSlots(@RequestBody addSlotsDTO: AddSlotsDTO): Any {
        for (i in 10 until 19) {
            slotsRepository.save(SlotsMongo(
                time = addSlotsDTO.date + " " + i.toString() + ":00",
                busy = false
            ))
        }
        return "Success"
    }
    @PostMapping("/change_state")
    fun changeState(@RequestBody changeTrackStateDTO: ChangeTrackStateDTO): Any {
        val id = UUID.fromString(changeTrackStateDTO.id)
        val state = Track.TrackState.valueOf(changeTrackStateDTO.state)

        return trackEsService.update(id){it.createChangeTrackState(id=id, state=state)}
    }
    @GetMapping("/list")
    fun listPapper(): ArrayList<ListPapperDTO> {
        var result = ArrayList<ListPapperDTO>()
        for (papper in papperRepository.findAll()) {
            result.add(
                ListPapperDTO(
                    count = papperEsService.getState(papper.aggregateId)!!.getCount(),
                    name = papper.name,
                    aggregateId = papper.aggregateIdStr
            ))
        }
        return result
    }

    @GetMapping("/list_track")
    fun listTrack(): Any {
        var result = ArrayList<ListTrackDTO>()
        for (user in usersRepository.findAll()) {
            for (track in userEsService.getState(user.aggregateId)!!.getTracks()) {
                val trackAg = trackEsService.getState(track)!!

                result.add(ListTrackDTO(track_id = track.toString(), slot_id = trackAg.getSlotId(), state = trackAg.getTrackState(), time = slotsRepository.findOneById(ObjectId(trackAg!!.getSlotId())).time))
            }
        }
        return result
    }
}