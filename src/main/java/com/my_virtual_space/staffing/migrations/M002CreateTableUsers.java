package com.my_virtual_space.staffing.migrations;

import net.fasolato.jfmigrate.JFMigrationClass;
import net.fasolato.jfmigrate.Migration;

import java.sql.JDBCType;

@Migration(number = 2)
public class M002CreateTableUsers extends JFMigrationClass {

    private static final String tableName = "users";
    private static final String foreignTable = "roles";
    private static final String foreignColumn = "role";
    private static final String foreignKey = String.format("fk_%s", foreignColumn);

    @Override
    public void up() {
        migration.createTable(tableName)
                .addColumn("id").asString(36).primaryKey()
                .addColumn("username").asString(255).notNullable()
                .addColumn("password").asString(255).notNullable()
                .withColumn(foreignColumn).asString(36).notNullable()
                .foreignKey(foreignKey)
                .fromTable(tableName).foreignColumn(foreignColumn)
                .toTable(foreignTable).primaryColumn("id")
                .addColumn("created_by").asString(36)
                .addColumn("created_date").as(JDBCType.TIMESTAMP)
                .addColumn("updated_by").asString(36)
                .addColumn("updated_date").as(JDBCType.TIMESTAMP);
    }

    @Override
    public void down() {
        migration.deleteTable(tableName);
    }
}

