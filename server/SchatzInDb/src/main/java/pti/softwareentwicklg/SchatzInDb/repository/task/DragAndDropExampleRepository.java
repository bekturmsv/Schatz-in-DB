package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.task.DragAndDropExample;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;

public interface DragAndDropExampleRepository extends JpaRepository<DragAndDropExample, Long> {

    DragAndDropExample getByTask(Task task);
}
