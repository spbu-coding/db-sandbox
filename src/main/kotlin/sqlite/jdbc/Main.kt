package sqlite.jdbc

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }
private const val dbPath = "jdbc_database.db"

// Основанно на статье https://habr.com/en/sandbox/88039/
fun main() {
    try {
        DbConnection(dbPath).use { it ->
            it.createDb()
            it.executeQuery("INSERT INTO users (name, phone, cityId) VALUES ('Vova', 987654, null);")
            val rybinsk = City("Rybinsk")
            it.addCity(rybinsk)
            it.addUser(User("Ann", 630474, rybinsk))

            /*
            read name
            read phone
            it.executeQuery("INSERT INTO 'users' ('name', 'phone') VALUES ('${name}', ${phone});")
            name = "X', 0); SELECT * FROM 'users'; INSERT INTO 'users' ('name', 'phone') VALUES ('X"

            "INSERT INTO 'users' ('name', 'phone') VALUES ('${name}', ${phone});"

            https://2019.hackerdom.ru/board/ (Несколько задач на SQL-инъекции)
            */
            it.getAllUsers().forEach { user -> println(user) }
        }
    } catch (ex: Exception) {
        logger.error(ex) {}
    }
}