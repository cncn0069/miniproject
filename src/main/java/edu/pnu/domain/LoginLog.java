package edu.pnu.domain;

import java.time.LocalDateTime;
import java.util.List;

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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loginLog_id;
	@ManyToOne
	@JoinColumn(name = "username")
	private Member username;
	private List<Role> role;
	private boolean enabled;
	private LocalDateTime created_at;
}
