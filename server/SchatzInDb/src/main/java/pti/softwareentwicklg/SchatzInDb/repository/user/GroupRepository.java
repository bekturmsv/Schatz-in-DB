package pti.softwareentwicklg.SchatzInDb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;

import java.util.Optional;
import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
    Optional<Group> findByCode(String code);

    Set<Group> findAllByTeacher(Teacher teacher);
}