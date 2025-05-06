package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.AbstractTask;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T13:58:06+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto toDto(AbstractTask task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setHint( map( task.getHint() ) );
        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setPoints( task.getPoints() );
        taskDto.setDifficulty( task.getDifficulty() );
        taskDto.setTaskType( task.getTaskType() );
        taskDto.setTaskPosition( task.getTaskPosition() );

        return taskDto;
    }
}
