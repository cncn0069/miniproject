package edu.pnu.dto;

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
public class ImageUploadResponseDTO {
	private String jobid;
	private String username;
}
