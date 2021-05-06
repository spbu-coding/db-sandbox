package neo4j

import mu.KotlinLogging
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import java.io.Closeable

private val logger = KotlinLogging.logger { }

class DbConnection(uri: String, user: String, password: String) : Closeable {

    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    private val session = driver.session()

    fun addUser(user: User) {
        session.writeTransaction { tx ->
            try {
                if (user.city != null) {
                    tx.run(
                        "CREATE (u:User{name:\$name, phone:\$phone}) " +
                                "MERGE (c:City{name:\$city}) " +
                                "CREATE (u)-[:LIVES_IN]->(c)",
                        mutableMapOf(
                            "name" to user.name,
                            "phone" to user.phone,
                            "city" to user.city.name
                        ) as Map<String, Any>?
                    )
                } else {
                    tx.run(
                        "CREATE (:User{name:\$name, phone:\$phone})",
                        mutableMapOf(
                            "name" to user.name,
                            "phone" to user.phone
                        ) as Map<String, Any>?
                    )
                }
                logger.info { "User $user added" }
            } catch (ex: Exception) {
                tx.rollback()
                logger.error(ex) { "Cannot add user" }
            }
        }
    }

    fun addCity(city: City) {
        session.writeTransaction { tx ->
            try {
                tx.run(
                    "MERGE (:City{name:\$city})",
                    mutableMapOf(
                        "city" to city.name
                    ) as Map<String, Any>?
                )
                logger.info { "City $city added" }
            } catch (ex: Exception) {
                tx.rollback()
                logger.error(ex) { "Cannot add city" }
            }
        }
    }

    fun getAllUsers(): Iterable<User> {
        val users = ArrayList<User>()
        session.readTransaction { tx ->
            try {
                val result =
                    tx.run("MATCH (u:User) OPTIONAL MATCH (u)-->(c) RETURN u.name AS name, u.phone AS phone, c.name AS city")
                result.forEach { rec ->
                    val city = rec["city"]
                    users += User(
                        rec["name"].asString(),
                        rec["phone"].asInt(),
                        if (city.isNull) null else City(city.asString())
                    )
                }
                logger.info { "Got all users" }
            } catch (ex: Exception) {
                tx.rollback()
                logger.error(ex) { "Cannot get all users" }
            }
        }

        return users
    }

    override fun close() {
        session.close()
        driver.close()
    }
}