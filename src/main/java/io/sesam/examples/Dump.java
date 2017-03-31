package io.sesam.examples;
import java.sql.*;
import java.io.*;
import com.opencsv.CSVWriter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dump {

    static Logger log = LoggerFactory.getLogger(Dump.class);

    public static void main(String[] args) throws Exception {

        String jdbcUrl = System.getenv("JDBC_URL");
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");
        String query = System.getenv("QUERY");
        String filename = System.getenv("OUTPUT_FILE");
        HikariConfig config = new HikariConfig();
        config.setInitializationFailTimeout(1);
        config.setConnectionTimeout(5000);
        config.setJdbcUrl(jdbcUrl);
        if (username != null) {
            config.setUsername(username);
        }
        if (password != null) {
            config.setPassword(password);
        }
        log.info("Opening connection to: " + jdbcUrl);

        HikariDataSource ds = new HikariDataSource(config);

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            log.info("Running query");
            try (Statement statement = conn.createStatement()) {
                statement.setFetchSize(1000);

                try (ResultSet rs = statement.executeQuery(query)) {
                    log.info("Writing file: {}", filename);
                    try (Writer bw = new BufferedWriter(new FileWriter(new File(filename)))) {
                        try (CSVWriter writer = new CSVWriter(bw)) {
                            writer.writeAll(rs, true);
                        }
                    }
                }
            }
        }
    }
}
