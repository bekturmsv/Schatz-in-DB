package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TableDataInitializer {

    private final JdbcTemplate jdbcTemplate;
    String testSQL = "SELECT name FROM verdaechtiger WHERE haarfarbe LIKE 'braun'";

    public TableDataInitializer(@Qualifier("taskJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initData() {
        if (isTableEmpty("verdaechtiger")) {
            insertVerdaechtiger();
        }
        if (isTableEmpty("zeuge")) {
            insertZeugen();
        }
        if (isTableEmpty("fall")) {
            insertFaelle();
        }
        if (isTableEmpty("fahrzeug")) {
            insertFahrzeuge();
        }
        if (isTableEmpty("fall_verdaechtiger")) {
            insertFallVerdaechtiger();
        }
        if (isTableEmpty("fall_zeuge")) {
            insertFallZeuge();
        }
        if (isTableEmpty("fall_fahrzeug")) {
            insertFallFahrzeug();
        }
    }

    private boolean isTableEmpty(String tableName) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + tableName, Long.class
        );
        return count != null && count == 0;
    }

    private void insertVerdaechtiger() {
        jdbcTemplate.update( "INSERT INTO verdaechtiger (name, geschlecht, alter, haarfarbe, schuhgroesse, wohnort) VALUES " +
                "('Max Berger', 'm', 35, 'braun', 44, 'Tiefenweg 3')," +
                "('Anna Müller', 'w', 29, 'blond', 39, 'Blumenstraße 7')," +
                "('Tom Schulz', 'm', 42, 'schwarz', 45, 'Marktgasse 12')," +
                "('Julia Horn', 'w', 50, 'rot', 38, 'Sonnenallee 5');"
        );
    }

    private void insertZeugen() {
        jdbcTemplate.update("INSERT INTO zeuge (id, name, alter) VALUES " +
                "(201, 'Laura Schmidt', 45)," +
                "(202, 'Peter Meier', 38)," +
                "(203, 'Maria Winter', 52);"
        );
    }

    private void insertFaelle() {
        jdbcTemplate.update( "INSERT INTO fall (id, tatort, schadenssumme, ermittler, anzahl_gegenstaende) VALUES " +
                "(101, 'Table-Town Museum', 50000.00, 'Detective Smith',3)," +
                "(102, 'Fahrradladen Hauptstr.', 1500.00, NULL, 1)," +
                "(103, 'Antiquitätengeschäft', 8000.00, NULL, 5);"
        );
    }

    private void insertFahrzeuge() {
        jdbcTemplate.update("INSERT INTO fahrzeug (id, kennzeichen) VALUES " +
                "(1, 'TT-CL 007')," +
                "(2, 'TT-FA 404');"
        );
    }

    private void insertFallVerdaechtiger() {
        jdbcTemplate.update("INSERT INTO fall_verdaechtiger (fall_id, verdaechtiger_id) VALUES " +
                "(101, 1)," +
                "(102, 2)," +
                "(103, 3);"
        );
    }

    private void insertFallZeuge() {
        jdbcTemplate.update( "INSERT INTO fall_zeuge (fall_id, zeuge_id) VALUES " +
                "(101, 201)," +
                "(102, 202)," +
                "(103, 203);"
        );
    }

    private void insertFallFahrzeug() {
        jdbcTemplate.update("INSERT INTO fall_fahrzeug (fall_id, fahrzeug_id) VALUES " +
                "(101, 1), (102, 2);");
    }
}
