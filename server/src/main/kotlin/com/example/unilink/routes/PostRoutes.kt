package com.example.unilink.routes

import com.example.unilink.models.Post
import com.example.unilink.repository.PostRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configurePostRoutes() {

    routing {

        route("/posts") {


            get {

                call.respond(
                    PostRepository.getAll()
                )

            }


            get("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()


                val post =
                    id?.let {
                        PostRepository.getById(it)
                    }


                if(post != null){

                    call.respond(post)

                }else{

                    call.respond(
                        HttpStatusCode.NotFound
                    )
                }
            }



            post {

                val post =
                    call.receive<Post>()


                val newPost =
                    PostRepository.add(post)


                call.respond(
                    HttpStatusCode.Created,
                    newPost
                )
            }



            patch("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()


                val updatedPost =
                    call.receive<Post>()


                val result =
                    id?.let {
                        PostRepository.update(
                            it,
                            updatedPost
                        )
                    }


                if(result != null){

                    call.respond(result)

                }else{

                    call.respond(
                        HttpStatusCode.NotFound
                    )
                }
            }



            delete("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()


                val deleted =
                    id?.let {
                        PostRepository.delete(it)
                    }


                if(deleted == true){

                    call.respond(
                        HttpStatusCode.OK
                    )

                }else{

                    call.respond(
                        HttpStatusCode.NotFound
                    )
                }
            }
        }
    }
}