package com.example.unilink.routes

import com.example.unilink.models.LoginResponse
import com.example.unilink.models.User
import com.example.unilink.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing


fun Application.configureAuthRoutes() {

    routing {

        route("/auth") {


            // Registrar usuario
            post("/register") {

                val userRequest = call.receive<User>()


                val existingUser =
                    UserRepository.getByEmail(
                        userRequest.email
                    )


                if (existingUser != null) {

                    call.respond(
                        HttpStatusCode.Conflict,
                        mapOf(
                            "message" to "El usuario ya existe"
                        )
                    )

                    return@post
                }


                val newUser =
                    UserRepository.add(
                        userRequest
                    )


                call.respond(
                    HttpStatusCode.Created,
                    newUser
                )
            }



            // Login
            post("/login") {

                val loginRequest =
                    call.receive<User>()


                val user =
                    UserRepository.getByEmail(
                        loginRequest.email
                    )


                if (
                    user != null &&
                    user.password == loginRequest.password
                ) {


                    call.respond(
                        HttpStatusCode.OK,
                        LoginResponse(
                            message = "Login correcto",
                            user = user
                        )
                    )


                } else {


                    call.respond(
                        HttpStatusCode.Unauthorized,
                        mapOf(
                            "message" to "Correo o contraseña incorrectos"
                        )
                    )
                }
            }
        }
    }
}