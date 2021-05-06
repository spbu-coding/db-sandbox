package sqlite.exposed.city

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import sqlite.exposed.user.User
import sqlite.exposed.user.Users

class City(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<City>(Cities)

    var name by Cities.name
    val users by User optionalReferrersOn Users.city

    override fun toString(): String = "City(name = $name)"
}