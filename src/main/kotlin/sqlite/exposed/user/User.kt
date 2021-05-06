package sqlite.exposed.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import sqlite.exposed.city.City

class User(id:  EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var phone by Users.phone
    var city by City optionalReferencedOn Users.city

    override fun toString(): String = "User(name = $name, phone = $phone, city = $city)"
}