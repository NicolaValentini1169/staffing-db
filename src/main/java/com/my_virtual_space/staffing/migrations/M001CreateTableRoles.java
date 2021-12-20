package com.my_virtual_space.staffing.migrations;

import net.fasolato.jfmigrate.JFMigrationClass;
import net.fasolato.jfmigrate.Migration;

import java.sql.JDBCType;

@Migration(number = 1)
public class M001CreateTableRoles extends JFMigrationClass {

    private static final String tableName = "roles";

    @Override
    public void up() {
        migration.createTable(tableName)
                .addColumn("id").asString(36).primaryKey()
                .addColumn("value").asString(255).notNullable()
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
