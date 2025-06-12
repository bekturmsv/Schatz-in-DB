package pti.softwareentwicklg.SchatzInDb.service.user;

import pti.softwareentwicklg.SchatzInDb.dto.request.CreateGroupRequest;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class GroupService {

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LEN = 6;
    private static final SecureRandom RNG = new SecureRandom();

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Group createGroup(CreateGroupRequest request, Authentication auth) {
        Group group = new Group();

        group.setName(request.getName());

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Teacher teacher)) {
            throw new RuntimeException("Only teachers can create groups");
        }

        group.setTeacher(teacher);

        for (int attempt = 0; attempt < 10_000; attempt++) {          // ~26⁶ вариантов
            String code = randomCode();
            if (!groupRepository.existsByCode(code)) {
                group.setCode(code);
                groupRepository.save(group);
                return group;
            }
        }
        throw new IllegalStateException("Couldn't create group");
    }

    private static String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LEN);
        for (int i = 0; i < CODE_LEN; i++) {
            sb.append(ALPHABET.charAt(RNG.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}