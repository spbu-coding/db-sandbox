package sqlite.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import sqlite.exposed.city.Cities
import sqlite.exposed.city.City
import sqlite.exposed.user.User
import sqlite.exposed.user.Users

private const val dbPath = "exposed_database.db"

fun main() {
    Database.connect("jdbc:sqlite:$dbPath", driver = "org.sqlite.JDBC")
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Users)
        SchemaUtils.create(Cities)

        User.new {
            name = "Vova"
            phone = 987654
        }
        User.new {
            name = "Ann"
            phone = 630474
            city = City.new {
                name = "Rybinsk"
            }
        }

        User.all().forEach { user -> println(user) }
    }
}