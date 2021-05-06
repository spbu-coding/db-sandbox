package neo4j

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

/*
CREATE (:City{name:"Saint Petersburg"});
CREATE (:City{name:"Moscow"});

MATCH (c:City{name:"Saint Petersburg"}) CREATE (u:User{name:"Vasya", phone:762042})-[:LIVES_IN]->(c);
CREATE (u:User{name:"Jack", phone:893843});
MATCH (c:City{name:"Saint Petersburg"}) CREATE (u:User{name:"Boris", phone:777293})-[:LIVES_IN]->(c);
MATCH (c:City{name:"Moscow"}) CREATE (u:User{name:"Dima", phone:111111})-[:LIVES_IN]->(c);
CREATE (u:User{name:"Frank", phone:213501});
MATCH (c:City{name:"Saint Petersburg"}) CREATE (u:User{name:"James", phone:893843})-[:LIVES_IN]->(c);
*/
fun main() {
    DbConnection("bolt://localhost:7687", "neo4j", "qwerty" ).use {
        val rybinsk = City("Rybinsk")
        it.addUser(User("Vova", 987654, null))
        it.addCity(rybinsk)
        it.addUser(User("Ann", 630474, rybinsk))
        it.getAllUsers().forEach { user -> println(user) }
    }
}