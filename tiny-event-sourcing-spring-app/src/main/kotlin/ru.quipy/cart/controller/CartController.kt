package ru.quipy.cart.controller

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.quipy.cart.api.CartAggregate
import ru.quipy.cart.dto.*
import ru.quipy.cart.logic.Cart
import ru.quipy.core.EventSourcingService
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.logic.Papper
import ru.quipy.slots.SlotsRepository
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.logic.Track
import ru.quipy.user.JWTUtil.UserSecurity
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.logic.User
import ru.quipy.user.service.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/cart")
class CartController(
    val userEsService: EventSourcingService<UUID, UserAggregate, User>,
    val cartEsService: EventSourcingService<UUID, CartAggregate, Cart>,
    val papperEsService: EventSourcingService<UUID, PapperAggregate, Papper>,
    val trackEsService: EventSourcingService<UUID, TrackAggregate, Track>,
    val usersRepository: UserRepository,
    val slotsRepository: SlotsRepository
) {
    private val mutexAddPapper = ReentrantLock()
    private val mutexSelectSlot = ReentrantLock()

    @PostMapping("/add_papper")
    fun addToCart(@RequestBody addPapperDTO: AddPapperDTO): Any {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)
        if (!userEsService.getState(userLogged.aggregateId)!!.existCart()) {
            userEsService.update(userLogged.aggregateId) {
                it.createNewCart(id = userLogged.aggregateId,
                    cartId = cartEsService.create { it.createNewCart() }.cartId
                )
            }
        }
        mutexAddPapper.lock()
        val cartId = userEsService.getState(userLogged.aggregateId)!!.getCart()
        val papperId = UUID.fromString(addPapperDTO.aggregateId)
        val countPapper = papperEsService.getState(papperId)!!.getCount()

        if (countPapper >= 1) {
            papperEsService.update(papperId){it.updatePapper(id=papperId, countPapper - 1)}
            var tmp = cartEsService.update(cartId) {
                it.createAddPapper(
                    id = cartId,
                    papperId = UUID.fromString(addPapperDTO.aggregateId)
                )
            }
            mutexAddPapper.unlock()
            return tmp
        } else {
            mutexAddPapper.unlock()
            return ResponseEntity<Any>(null, HttpStatus.CONFLICT)
        }
    }

    @PostMapping("/delete_papper")
    fun deleteFromCart(@RequestBody addPapperDTO: AddPapperDTO): Any {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)
        if (!userEsService.getState(userLogged.aggregateId)!!.existCart()) {
            userEsService.update(userLogged.aggregateId) {
                it.createNewCart(id = userLogged.aggregateId,
                    cartId = cartEsService.create { it.createNewCart() }.cartId
                )
            }
        }

        val cartId = userEsService.getState(userLogged.aggregateId)!!.getCart()
        return cartEsService.update(cartId){it.createDeletePapper(id = cartId, papperId = UUID.fromString(addPapperDTO.aggregateId))}
    }

    @PostMapping("/checkout")
    fun checkout(@RequestBody checkoutDTO: CheckoutDTO): Any {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)
        if (userEsService.getState(userLogged.aggregateId)!!.existCart()) {
            val cartId = userEsService.getState(userLogged.aggregateId)!!.getCart()
            mutexSelectSlot.lock()
            var slot = slotsRepository.findOneById(ObjectId(checkoutDTO.id))
            if (slot.busy) {
                mutexSelectSlot.unlock()
                return ResponseEntity<Any>(null, HttpStatus.CONFLICT)
            }
            slot.busy = true
            slotsRepository.save(slot)
            mutexSelectSlot.unlock()

            System.out.println(slot.id.toString())
            val trackId = trackEsService.create { it.createNewTrack(slotId = slot.id.toString()) }.trackId
            userEsService.update(userLogged.aggregateId){it.createAddTrack(id = userLogged.aggregateId, trackId = trackId)}
            userEsService.update(userLogged.aggregateId){it.createResetCart(id = userLogged.aggregateId, cartId)}

            return trackId
        } else {
            return ResponseEntity<Any>("Empty card", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/slots")
    fun getSlots(): ArrayList<SlotsDTO> {
        val result = ArrayList<SlotsDTO>()

        for (item in slotsRepository.findAll()) {
            val current = LocalDateTime.now()
            val dateTime = LocalDateTime.parse(item.time, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            if (dateTime > current && !item.busy) {
                result.add(
                    SlotsDTO(
                        time = dateTime, id = item.id.toString()
                    )
                )
            }
        }

        result.sortedWith(compareBy({ it.time }))
        return result
    }

    @GetMapping("/list")
    fun listCart(): ListCartWithIdDTO {
        val userLoggedId = (SecurityContextHolder.getContext().authentication.principal as UserSecurity).id
        val userLogged = usersRepository.findOneByEmail(userLoggedId)
        if (!userEsService.getState(userLogged.aggregateId)!!.existCart()) {
            userEsService.update(userLogged.aggregateId) {
                it.createNewCart(id = userLogged.aggregateId,
                    cartId = cartEsService.create { it.createNewCart() }.cartId
                )
            }
        }

        val resultItems = ArrayList<ListCartDTO>()
        val cartId = userEsService.getState(userLogged.aggregateId)!!.getCart()
        for (item in cartEsService.getState(cartId)!!.getPappers()) {
            resultItems.add(ListCartDTO(
                name = item.key.toString(),
                count = item.value
            ))
        }
        return ListCartWithIdDTO(id = cartId.toString(), items = resultItems)
    }
}