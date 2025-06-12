package pti.softwareentwicklg.SchatzInDb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}