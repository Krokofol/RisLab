package com.krokofol.lab.service

import java.sql.Connection


object PreloadSchemaService {

    private val createTableScript = """
        create table if not exists node (
	id BIGSERIAL primary key,
	lat float8,
	lon float8,
	username VARCHAR(100),
	uid BIGINT,
	version BIGINT,
	changeset BIGINT,
	date_time TIMESTAMP
);
"""

    private val deleteTableScript = "drop table if exists node cascade;"

    fun preloadSchema() {
        ConnectionPool.getConnection()
            .let { connection ->
                clearAllData(connection)
                migrateNodeTable(connection)
                connection.close()
            }
    }

    private fun migrateNodeTable(connection: Connection) {
        connection.prepareStatement(createTableScript).execute()
    }

    private fun clearAllData(connection: Connection) {
        connection.prepareStatement(deleteTableScript).execute()
    }

}