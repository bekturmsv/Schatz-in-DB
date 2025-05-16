package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.TopicDto;
import com.prog.datenbankspiel.model.task.Topic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDto toDto(Topic topic);
    Topic fromDto(TopicDto topicDto);
}
