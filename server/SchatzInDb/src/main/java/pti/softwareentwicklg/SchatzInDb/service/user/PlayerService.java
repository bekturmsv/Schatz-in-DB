package pti.softwareentwicklg.SchatzInDb.service.user;

import org.springframework.stereotype.Service;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.service.task.TaskService;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TaskService taskService;

    public PlayerService(PlayerRepository playerRepository, TaskService taskService) {
        this.playerRepository = playerRepository;
        this.taskService = taskService;
    }

    public Player getById(User user) {
        Player player = playerRepository.findPlayerById(user.getId());
        player.setTotal_points((long) taskService.recalcAndSaveUserPoints(user.getId()));
        return player;
    }
}
