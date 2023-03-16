package ru.quipy.papper.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.admin.dto.NewPapperDTO
import ru.quipy.admin.dto.UpdatePapperDTO
import ru.quipy.core.EventSourcingService
import ru.quipy.papper.api.PapperAggregate
import ru.quipy.papper.dto.ListPapperDTO
import ru.quipy.papper.logic.Papper
import ru.quipy.papper.service.PapperMongo
import ru.quipy.papper.service.PapperRepository
import ru.quipy.track.api.TrackAggregate
import ru.quipy.track.logic.Track
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.logic.User
import ru.quipy.user.service.UserRepository
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/papper")
class PapperController (
    val userEsService: EventSourcingService<UUID, UserAggregate, User>,
    val trackEsService: EventSourcingService<UUID, TrackAggregate, Track>,
    val papperEsService: EventSourcingService<UUID, PapperAggregate, Papper>,
    val usersRepository: UserRepository,
    val papperRepository: PapperRepository
) {
    @GetMapping("/list")
    fun listPapper(): ArrayList<ListPapperDTO> {
        var result = ArrayList<ListPapperDTO>()
        for (papper in papperRepository.findAll()) {
            result.add(
                ListPapperDTO(
                    count = papperEsService.getState(papper.aggregateId)!!.getCount(),
                    name = papper.name,
                    aggregateId = papper.aggregateIdStr
                )
            )
        }
        return result
    }
}