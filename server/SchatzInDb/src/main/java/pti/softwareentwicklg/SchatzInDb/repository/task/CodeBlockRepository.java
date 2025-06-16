package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pti.softwareentwicklg.SchatzInDb.model.task.CodeBlock;

import java.util.List;

@Repository
public interface CodeBlockRepository extends JpaRepository<CodeBlock, Long> {
    List<CodeBlock> findBySectionIdOrderByPositionAsc(Long sectionId);
}