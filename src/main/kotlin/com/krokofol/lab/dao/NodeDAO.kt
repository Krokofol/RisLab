package com.krokofol.lab.dao

import java.sql.Date
import com.krokofol.lab.Node
import com.krokofol.lab.config.Properties
import com.krokofol.lab.service.ConnectionPool

object NodeDAO {
    private var counter = 0
    private val connection = ConnectionPool.getConnection().apply { autoCommit = false }
    private val prepareStatement = connection.prepareStatement("INSERT INTO node (id, lat, lon, username, uid, version, changeset, date_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")

    fun insertNode(node: Node) {
        prepareStatement
            .apply {
                setLong(1, node.id.toLong())
                setDouble(2, node.lat)
                setDouble(3, node.lon)
                setString(4, node.user)
                setLong(5, node.uid.toLong())
                setLong(6, node.version.toLong())
                setLong(7, node.changeset.toLong())
                setDate(8, Date(node.timestamp.toGregorianCalendar().time.time))
            }
            .let { preparedStatement ->
                if (counter < Properties.BATCH_SIZE) {
                    preparedStatement.addBatch()
                    counter++
                } else {
                    preparedStatement.addBatch()
                    preparedStatement.executeBatch()
                    counter = 0
                    connection.commit()
                }
            }


    }

    fun closeConnection() {
        this.connection.close()
    }
}