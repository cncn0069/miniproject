package edu.pnu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class APIDto {
	private Long id;
	private String log_type;
	private String username;
	private String api_endpoint;
	private String http_method;
	private String message;
	private String id_address;
	private LocalDateTime created_at;
	private Integer status;
}
