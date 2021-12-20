package com.example.demo.migrations;

import net.fasolato.jfmigrate.JFMigrationClass;
import net.fasolato.jfmigrate.Migration;

import java.sql.JDBCType;

@Migration(number = 3)
public class M003CreateTableFigures extends JFMigrationClass {

    private static final String tableName = "figures";

    @Override
    public void up() {
        migration.createTable(tableName)
                .addColumn("id").asString(36).primaryKey()
                .addColumn("name").asString(255).notNullable()
                .addColumn("hourly_cost").asDecimal(10, 3).notNullable().defaultValue(0)
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
