package com.my_virtual_space.staffing;

import com.my_virtual_space.staffing.migrations.M001CreateTableRoles;
import net.fasolato.jfmigrate.JFMigrate;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;

@SpringBootApplication
public class StaffingDbApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StaffingDbApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(StaffingDbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting migrations");

        Options opts = new Options();
        opts.addOption("u", false, "Migrate up");
        opts.addOption("d", false, "Migrate down");
        opts.addOption("b", true, "Migration starting number (0 = initial state). Useful for the script output: can decide from which version to start (number passed is included).");
        opts.addOption("e", true, "Migration ending number (0 = initial state). The program stops at this migration regardless of the presence of other ones (number passed is excluded).");
        opts.addOption("f", true, "Create migration script in this file");
        opts.addOption("s", true, "Database schema name (optional)");
        opts.addOption("h", false, "Help");

        CommandLineParser cmdPrs = new DefaultParser();
        CommandLine cmd = cmdPrs.parse(opts, args);

        if (cmd.hasOption("h") || (!cmd.hasOption("u") && !cmd.hasOption("d"))) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("database-migrations", opts);
            return;
        }

        FileWriter out = null;
        if (cmd.hasOption("f")) {
            out = new FileWriter(cmd.getOptionValue("f"));
        }

        int beginnigMigration = -1;
        if (cmd.hasOption("b")) {
            try {
                beginnigMigration = Integer.parseInt(cmd.getOptionValue("b"));
            } catch (Exception e) {
            }
        }

        int endMigration = -1;
        if (cmd.hasOption("e")) {
            try {
                endMigration = Integer.parseInt(cmd.getOptionValue("e"));
            } catch (Exception e) {
            }
        }

        if (cmd.hasOption(("u")) && !cmd.hasOption("f") && cmd.hasOption("b")) {
            log.error("A start migration can only be specified when writing to a script file (-f option)");
            HelpFormatter help = new HelpFormatter();
            help.printHelp("database-migrations", opts);
            System.exit(-1);
        }

        JFMigrate migrate = new JFMigrate();
        migrate.registerPackage(M001CreateTableRoles.class);
        if (cmd.hasOption("u")) {
            //Migration UP
            log.info("Migrating UP");
            try {
                if (out == null) {
                    migrate.migrateUp(beginnigMigration, null, true);
                } else {
                    migrate.migrateUp(beginnigMigration, out, beginnigMigration <= 0);
                    out.close();
                }
            } catch (Exception e) {
                log.error("Error performing migration", e);
            }
        } else if (cmd.hasOption("d")) {
            try {
                if (out == null) {
                    migrate.migrateDown(endMigration);
                } else {
                    migrate.migrateDown(endMigration, out);
                    out.close();
                }
            } catch (Exception e) {
                log.error("Error performing migration", e);
            }
        }

        log.info("Ended migrations");
    }

}
