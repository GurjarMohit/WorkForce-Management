package com.railse.hiring.Railse.Backed.mapper;

import com.railse.hiring.Railse.Backed.Entity.TaskManagement;
import com.railse.hiring.Railse.Backed.dto.TaskManagementDto;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE)
public interface ITaskManagementMapper {
    ITaskManagementMapper INSTANCE = Mappers.getMapper(ITaskManagementMapper.class);
    TaskManagement dtoToModel(TaskManagementDto dto);
    TaskManagementDto modelToDto(TaskManagement model);
    List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);
}