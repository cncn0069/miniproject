package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ObjectDto {
	private String caller;
	private String receiver;
	private int status;
	private String method;
	private String URL;
	private String message;
	private MainDto content;
}
