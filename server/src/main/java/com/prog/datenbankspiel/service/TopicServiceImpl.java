package com.prog.datenbankspiel.service;


import com.prog.datenbankspiel.dto.task.TopicDto;
import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LevelRepository levelRepository;

    @Override
    public Topic createTopic(TopicDto topicDto) {
        Topic topic = new Topic();
        topic.setName(topicDto.getName());
        topic.setDifficulty(topicDto.getDifficulty());
        Level level = levelRepository.findByDifficulty(topicDto.getDifficulty());
        topic.setLevel(level);
        return topicRepository.save(topic);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }
}
