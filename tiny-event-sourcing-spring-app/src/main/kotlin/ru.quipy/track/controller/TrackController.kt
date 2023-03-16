package ru.quipy.track.controller

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.quipy.cart.api.CartAggregate
import ru.quipy.cart.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.logic.Papper
import ru.quipy.slots.SlotsRepository
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.dto.CancelTrackDTO
import ru.quipy.track.dto.ChangeTrackDTO
import ru.quipy.track.dto.TrackListDTO
import ru.quipy.track.logic.Track
import ru.quipy.user.JWTUtil.UserSecurity
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.logic.User
import ru.quipy.user.service.UserRepository
import java.util.*
import java.util.concurrent.locks.ReentrantLock

@RestController
@RequestMapping("/track")
class TrackController(
    val userEsService: EventSourcingService<UUID, UserAggregate, User>,
    val cartEsService: EventSourcingService<UUID, CartAggregate, Cart>,
    val papperEsService: EventSourcingService<UUID, PapperAggregate, Papper>,
    val trackEsService: EventSourcingService<UUID, TrackAggregate, Track>,
    val usersRepository: UserRepository,
    val slotsRepository: SlotsRepository
) {
    private val mutexChangeSlot = ReentrantLock()
    @GetMapping("/list")
    fun trackList(): ArrayList<TrackListDTO> {
        val result = ArrayList<TrackListDTO>()
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)

        for (item in userEsService.getState(userLogged.aggregateId)!!.getTracks()) {
            val track = trackEsService.getState(item)!!
            result.add(
                TrackListDTO(
                    id = item.toString(),
                    state = track.getTrackState(),
                    time = slotsRepository.findOneById(ObjectId(track.getSlotId())).time

            ))
        }
        return result
    }

    @PostMapping("/cancel")
    fun cancelTrack(@RequestBody cancelTrackDTO: CancelTrackDTO) {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)

        for (item in userEsService.getState(userLogged.aggregateId)!!.getTracks()) {
            if (item == UUID.fromString(cancelTrackDTO.id)) {
                if (trackEsService.getState(item)!!.getTrackState() != Track.TrackState.CANCELED) {
                    trackEsService.update(item) { it.createCancelTrack(item) }

                    val slot = slotsRepository.findOneById(ObjectId(trackEsService.getState(item)!!.getSlotId()))
                    slot.busy = false
                    slotsRepository.save(slot)
                }
                break
            }
        }
    }

    @PostMapping("/change_time")
    fun changeTimeTrack(@RequestBody changeTrackDTO: ChangeTrackDTO): Any {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)

        for (item in userEsService.getState(userLogged.aggregateId)!!.getTracks()) {
            if (item == UUID.fromString(changeTrackDTO.id)) {
                if (trackEsService.getState(item)!!.getTrackState() == Track.TrackState.IN_DELIVERY) {
                    mutexChangeSlot.lock()
                    val newSlot = slotsRepository.findOneById(ObjectId(changeTrackDTO.slotId))
                    if (newSlot.busy) {
                        mutexChangeSlot.unlock()
                        return ResponseEntity<Any>(null, HttpStatus.CONFLICT)
                    }
                    val slot = slotsRepository.findOneById(ObjectId(trackEsService.getState(item)!!.getSlotId()))
                    slot.busy = false
                    trackEsService.update(item) { it.createChangeTrack(id = item, slotId = changeTrackDTO.slotId) }

                    newSlot.busy = true
                    slotsRepository.save(newSlot)
                    mutexChangeSlot.unlock()
                }
                break
            }
        }
        return "Success"
    }
}