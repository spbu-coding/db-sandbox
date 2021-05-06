package sqlite.exposed.user

import org.jetbrains.exposed.dao.id.IntIdTable
import sqlite.exposed.city.Cities

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val phone = integer("phone")
    val city = reference("city", Cities).nullable()
}