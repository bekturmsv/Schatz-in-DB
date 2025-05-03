package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.CreateTopicRequest;
import com.prog.datenbankspiel.model.task.Topic;

import java.util.List;

public interface TopicService {

    Topic createTopic(CreateTopicRequest createTopicRequest);

    Topic getTopicById(Long id);

    List<Topic> getAllTopics();

    void deleteTopic(Long id);

}
