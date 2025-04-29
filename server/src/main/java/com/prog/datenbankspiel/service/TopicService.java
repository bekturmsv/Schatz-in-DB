package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TopicDto;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.Topic;

import java.util.List;

public interface TopicService {

    Topic createTopic(TopicDto topicDto);

    Topic getTopicById(Long id);

    List<Topic> getAllTopics();

    void deleteTopic(Long id);

}
