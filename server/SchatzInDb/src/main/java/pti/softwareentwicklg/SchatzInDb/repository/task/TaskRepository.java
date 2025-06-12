package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;

import java.util.Optional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task getTaskByTaskCode(String task_code);
    Optional<Task> findByTaskCode(String code);
    List<Task> findByKategorie(SqlKategorie kategorie);
    List<Task> findBySchwierigkeitsgrad(Schwierigkeit schwierigkeitsgrad);
    List<Task> findByTaskType(TaskType taskType);
    List<Task> findByAktivTrue();
    List<Task> findBySchwierigkeitsgradAndTaskType(Schwierigkeit schwierigkeitsgrad, TaskType taskType);
    List<Task> findBySchwierigkeitsgradAndTaskTypeAndKategorie(Schwierigkeit schwierigkeit, TaskType taskType, SqlKategorie sqlKategorie);
    boolean existsByTaskCode(String taskCode);
}