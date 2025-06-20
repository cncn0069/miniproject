package edu.pnu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagePermitRequestDTO {
	private String jobid;
	private List<Integer> selectedIdx;
	private List<String> selectedname;
}
