package edu.pnu.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Entity는 DB 저장이 필요한 클래스에만
@Getter@Setter@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageProcessResultDTO {
	private String image_base64;
	private String message;
	private List<?> poly;
	private List<?> names;
	private List<?> type;
	private List<?> viewSize;
}
