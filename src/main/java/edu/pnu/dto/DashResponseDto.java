package edu.pnu.dto;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashResponseDto {
	private DashBoardDto dashboard;
	private List<DashBoardDto> dashboards;
	
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private int numberofElements;
	private boolean first;
	private boolean last;
	
}
