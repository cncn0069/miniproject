package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APILog {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String log_type;
	@ManyToOne
	@JoinColumn(name="username")
	private int username;
	private String api_endpoint;
	private String http_method;
	private int status_code;
	private String message;
	private String id_address;
	private LocalDateTime created_at;
}
