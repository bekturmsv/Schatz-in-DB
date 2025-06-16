package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.task.Articles;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Articles, Long> {
//    List<Articles> findByTeacherId(Long teacherId);
    List<Articles> findByTopic(SqlKategorie topic);
}
