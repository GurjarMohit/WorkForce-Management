package com.railse.hiring.Railse.Backed.mapper;

import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.dto.TaskManagementDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-03T12:17:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ITaskManagementMapperImpl implements ITaskManagementMapper {

    @Override
    public TaskManagement dtoToModel(TaskManagementDto dto) {
        if ( dto == null ) {
            return null;
        }

        TaskManagement taskManagement = new TaskManagement();

        taskManagement.setId( dto.getId() );
        taskManagement.setReferenceId( dto.getReferenceId() );
        taskManagement.setStartDate( dto.getStartDate() );
        taskManagement.setReferenceType( dto.getReferenceType() );
        taskManagement.setTask( dto.getTask() );
        taskManagement.setStatus( dto.getStatus() );
        taskManagement.setAssigneeId( dto.getAssigneeId() );
        taskManagement.setTaskDeadlineTime( dto.getTaskDeadlineTime() );
        taskManagement.setTaskPriority( dto.getTaskPriority() );

        return taskManagement;
    }

    @Override
    public TaskManagementDto modelToDto(TaskManagement model) {
        if ( model == null ) {
            return null;
        }

        TaskManagementDto taskManagementDto = new TaskManagementDto();

        taskManagementDto.setId( model.getId() );
        taskManagementDto.setReferenceId( model.getReferenceId() );
        taskManagementDto.setReferenceType( model.getReferenceType() );
        taskManagementDto.setTask( model.getTask() );
        taskManagementDto.setStatus( model.getStatus() );
        taskManagementDto.setAssigneeId( model.getAssigneeId() );
        taskManagementDto.setTaskDeadlineTime( model.getTaskDeadlineTime() );
        taskManagementDto.setTaskPriority( model.getTaskPriority() );
        taskManagementDto.setStartDate( model.getStartDate() );

        return taskManagementDto;
    }

    @Override
    public List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models) {
        if ( models == null ) {
            return null;
        }

        List<TaskManagementDto> list = new ArrayList<TaskManagementDto>( models.size() );
        for ( TaskManagement taskManagement : models ) {
            list.add( modelToDto( taskManagement ) );
        }

        return list;
    }
}
