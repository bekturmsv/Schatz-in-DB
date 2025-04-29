package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.Topic;

import java.util.List;

public interface TopicService {

    Topic createTopic(Topic topic);

    Topic getTopicById(Long id);

    List<Topic> getAllTopics();

    List<Topic> getTopicsByDifficulty(LevelDifficulty difficulty);

    void deleteTopic(Long id);

}
