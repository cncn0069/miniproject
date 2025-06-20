package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashBoard {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dash_id")
	private Long dashId;
	//@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name = "username")
	private Member username;
	private String nickname;
	private String title;
	@Lob
	private String content;
	@Column(name = "created_at")
	private LocalDateTime createdAt; 
	private boolean enabled;
}
