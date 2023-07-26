package com.example.routes

import com.example.models.Customer
import com.example.models.customerStorage
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText("No customers found", status = OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = BadRequest,
            )
            val customer = customerStorage.find {it.id == id} ?: return@get call.respondText(
                "No customer with this id",
                status = NotFound,
            )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored correctly", status = Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("No customers found", status = Accepted)
            } else {
                call.respondText("Not Found", status = NotFound)
            }
        }
    }
}