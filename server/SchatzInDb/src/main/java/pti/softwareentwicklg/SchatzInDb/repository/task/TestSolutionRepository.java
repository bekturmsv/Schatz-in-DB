package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.task.TestSolution;

import java.util.List;

public interface TestSolutionRepository extends JpaRepository<TestSolution, Long> {
    List<TestSolution> findByCorrectTrue();
}
