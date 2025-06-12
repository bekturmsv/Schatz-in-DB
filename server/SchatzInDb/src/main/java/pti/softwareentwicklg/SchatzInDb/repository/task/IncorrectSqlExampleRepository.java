package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.task.IncorrectSqlExample;

public interface IncorrectSqlExampleRepository extends JpaRepository<IncorrectSqlExample, Long> {
}
