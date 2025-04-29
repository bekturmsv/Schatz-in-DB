package com.prog.datenbankspiel.initializer;

import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicInitializer {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private LevelRepository levelRepository;

    @PostConstruct
    public void initializeTopics() {
        addTopicIfNotExists("SELECT Basics", LevelDifficulty.EASY);
        addTopicIfNotExists("JOINs and Filtering", LevelDifficulty.MEDIUM);
    }

    private void addTopicIfNotExists(String name, LevelDifficulty difficulty) {
        boolean exists = topicRepository.existsByNameAndDifficulty(name, difficulty);
        if (!exists) {
            Topic topic = new Topic();
            topic.setName(name);
            Level level = levelRepository.findByDifficulty(difficulty);
            topic.setLevel(level);
            topic.setDifficulty(difficulty);
            topicRepository.save(topic);
        }
    }
}

