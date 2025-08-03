package com.railse.hiring.Railse.Backed.Response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Pagination {
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

}
