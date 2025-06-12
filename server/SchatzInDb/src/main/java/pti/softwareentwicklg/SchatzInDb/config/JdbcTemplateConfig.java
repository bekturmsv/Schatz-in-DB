package pti.softwareentwicklg.SchatzInDb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {

    @Bean(name = "userJdbcTemplate")
    public JdbcTemplate userJdbcTemplate(@Qualifier("userDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "taskJdbcTemplate")
    public JdbcTemplate taskJdbcTemplate(@Qualifier("taskDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}