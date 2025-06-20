package edu.pnu.domain;

import java.time.LocalDateTime;

import edu.pnu.dto.ItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(
		name = "user_recent_page",
		indexes = {
				@Index(name = "idx_user_created_at", columnList = "username, created_at")
			}
		)
public class UserRecentPage {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	@Column(name = "dash_id")
	private Long dashId;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
}
