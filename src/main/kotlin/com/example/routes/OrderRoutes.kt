package com.example.routes

import com.example.models.orderStorage
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.listOrdersRoute() {
    get("/order") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        }
    }
}

fun Route.getOrderRoute() {
    get("/order/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = BadRequest)
        val order = orderStorage.find { it.number == id} ?: return@get call.respondText(
            "Not found",
            status = NotFound,
        )
        call.respond(order)
    }
}

fun Route.totalizeOrderRoute() {
    get("/order/{id?}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = BadRequest)
        val order = orderStorage.find { it.number == id} ?: return@get call.respondText(
            "Not found",
            status = NotFound,
        )
        val total = order.contents.sumOf { it.price * it.amount }
        call.respond(total)
    }
}
