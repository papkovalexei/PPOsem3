package ru.quipy.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import ru.quipy.core.EventSourcingService
import ru.quipy.user.api.UserAggregate
import ru.quipy.user.api.UserRegisterDTO
import ru.quipy.user.logic.User
import ru.quipy.user.service.UserMongo
import ru.quipy.user.service.UserRepository
import java.util.*

@RestController
class UserAuthController (
    val userEsService: EventSourcingService<UUID, UserAggregate, User>,
    val usersRepository: UserRepository,
    val passwordEncoder: BCryptPasswordEncoder
) {
    @PostMapping("/auth/create")
    fun createUser(@RequestBody userRegisterDTO: UserRegisterDTO): Any {
        if (usersRepository.findOneByEmail(userRegisterDTO.email) != null) {
            return ResponseEntity<Any>(null, HttpStatus.CONFLICT)
        }
        return usersRepository.save(UserMongo(
            email = userRegisterDTO.email,
            password =  passwordEncoder.encode(userRegisterDTO.password),
            aggregateId = userEsService.create { it.createNewUser() }.userId,
            role = "user"
        ))
    }
}