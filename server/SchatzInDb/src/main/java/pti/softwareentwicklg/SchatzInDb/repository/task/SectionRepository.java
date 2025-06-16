package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pti.softwareentwicklg.SchatzInDb.model.task.Section;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByArticleIdOrderByPositionAsc(Long articleId);
}
