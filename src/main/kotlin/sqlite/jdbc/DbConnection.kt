package sqlite.jdbc

import mu.KotlinLogging
import java.io.Closeable
import java.sql.*

private val logger = KotlinLogging.logger { }

class DbConnection(private val dbPath: String) : Closeable {
    private val connection = DriverManager.getConnection("$DB_DRIVER:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val addUserStatement by lazy { connection.prepareStatement("INSERT INTO users (name, phone, cityId) VALUES (?, ?, ?);") }
    private val addCityStatement by lazy { connection.prepareStatement("INSERT INTO cities (name) VALUES (?);") }
    private val getCityIdByName by lazy { connection.prepareStatement("SELECT id FROM cities WHERE cities.name = ?;") }
    private val getAllUsersStatement by lazy { connection.prepareStatement("SELECT users.name as name, users.phone as phone, cities.name as city FROM users LEFT JOIN cities ON cities.id = users.cityId;") }

    init {
        logger.info { "Connected to database: $dbPath" }
    }

    fun createDb() {
        connection.createStatement()
            .also { stmt ->
                try {
                    stmt.execute("CREATE TABLE if not exists cities(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name text);")
                    stmt.execute("CREATE TABLE if not exists users(userId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name varchar(255), phone INTEGER, cityId INTEGER, FOREIGN KEY (cityId) REFERENCES cities(id) ON DELETE SET NULL);")
                    logger.info { "Tables created or already exists" }
                } catch (ex: Exception) {
                    logger.error(ex) { "Cannot create table in database" }
                } finally {
                    stmt.close()
                }
            }
    }

    fun executeQuery(query: String) {
        connection.createStatement().also { stmt ->
            try {
                stmt.execute(query)
                logger.info { "Executed query: \"$query\"" }
            } catch (ex: Exception) {
                logger.error(ex) { "Cannot execute query: \"$query\"" }
            } finally {
                stmt.close()
            }
        }
    }

    fun addUser(user: User) {
        var cityId: Int? = null
        if (user.city != null) {
            cityId = findCityId(user.city)
            if (cityId == null) {
                addCity(user.city)
                cityId = findCityId(user.city)
            }
        }
        try {
            addUserStatement.setString(1, user.name)
            addUserStatement.setInt(2, user.phone)
            if (cityId == null) {
                addUserStatement.setNull(3, Types.INTEGER)
            } else {
                addUserStatement.setInt(3, cityId)
            }

            addUserStatement.execute()
            logger.info { "Added user: $user" }
        } catch (ex: Exception) {
            logger.error(ex) { "Cannot add user: $user" }
        }
    }

    fun getAllUsers(): Iterable<User> {
        val users = ArrayList<User>()
        try {
            val resSet = getAllUsersStatement.executeQuery()
            while (resSet.next()) {
                users += User(
                    resSet.getString("name"),
                    resSet.getInt("phone"),
                    resSet.getString("city")?.let { City(it) }
                )
            }
            logger.info { "Got all users from database" }
        } catch (ex: Exception) {
            logger.error(ex) { "Cannot get users from database" }
        }
        return users
    }

    fun addCity(city: City) {
        try {
            addCityStatement.setString(1, city.name)
            addCityStatement.execute()
            logger.info { "Added city: $city" }
        } catch (ex: Exception) {
            logger.error(ex) { "Cannot add city: $city" }
        }
    }

    private fun findCityId(city: City): Int? {
        var id: Int? = null
        try {
            getCityIdByName.setString(1, city.name)
            val resSet = getCityIdByName.executeQuery()
            while (resSet.next()) {
                id = resSet.getInt("id")
            }
            logger.info { "Got all users from database" }
        } catch (ex: Exception) {
            logger.error(ex) { "Cannot get users from database" }
        }
        return id
    }

    override fun close() {
        addUserStatement.close()
        getAllUsersStatement.close()
        connection.close()
        logger.info { "Connection closed" }
    }

    companion object {
        private const val DB_DRIVER = "jdbc:sqlite"
    }
}