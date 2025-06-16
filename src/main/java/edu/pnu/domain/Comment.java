package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(nullable = false)
	private Long comment_id;
	@ManyToOne
	@JoinColumn(name = "dash_id")
	private DashBoard dashId;
	private Boolean enabled;
	@ManyToOne
	@JoinColumn(name = "username")
	private Member username;
	private String nickname;
	private String content;
	private Long parent_id;
	private LocalDateTime created_at;
	private int depth;
}
