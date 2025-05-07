package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Topic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T23:53:52+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setTopicId( taskTopicId( task ) );
        taskDto.setTopicName( taskTopicName( task ) );
        taskDto.setHint( taskHintDescription( task ) );
        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setTaskAnswer( task.getTaskAnswer() );
        taskDto.setPoints( task.getPoints() );
        taskDto.setLevelDifficulty( task.getLevelDifficulty() );
        taskDto.setTaskType( task.getTaskType() );

        return taskDto;
    }

    @Override
    public Task fromDto(TaskDto dto) {
        if ( dto == null ) {
            return null;
        }

        Task task = new Task();

        task.setLevelDifficulty( dto.getLevelDifficulty() );
        task.setTaskType( dto.getTaskType() );
        task.setTitle( dto.getTitle() );
        task.setDescription( dto.getDescription() );
        task.setTaskAnswer( dto.getTaskAnswer() );
        task.setPoints( dto.getPoints() );

        return task;
    }

    @Override
    public void updateTaskFromDto(TaskDto dto, Task task) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            task.setId( dto.getId() );
        }
        if ( dto.getLevelDifficulty() != null ) {
            task.setLevelDifficulty( dto.getLevelDifficulty() );
        }
        if ( dto.getTaskType() != null ) {
            task.setTaskType( dto.getTaskType() );
        }
        if ( dto.getTitle() != null ) {
            task.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            task.setDescription( dto.getDescription() );
        }
        if ( dto.getTaskAnswer() != null ) {
            task.setTaskAnswer( dto.getTaskAnswer() );
        }
        if ( dto.getPoints() != null ) {
            task.setPoints( dto.getPoints() );
        }
    }

    private Long taskTopicId(Task task) {
        Topic topic = task.getTopic();
        if ( topic == null ) {
            return null;
        }
        return topic.getId();
    }

    private String taskTopicName(Task task) {
        Topic topic = task.getTopic();
        if ( topic == null ) {
            return null;
        }
        return topic.getName();
    }

    private String taskHintDescription(Task task) {
        Hint hint = task.getHint();
        if ( hint == null ) {
            return null;
        }
        return hint.getDescription();
    }
}
