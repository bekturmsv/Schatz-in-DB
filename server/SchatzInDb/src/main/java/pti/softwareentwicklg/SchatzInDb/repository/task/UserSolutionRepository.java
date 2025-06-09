package pti.softwareentwicklg.SchatzInDb.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserSolutionRepository extends JpaRepository<UserSolution, Long> {

    List<UserSolution> findByUserId(Long id);

    UserSolution getByUserIdAndTaskCode(Long id, String taskCode);

}