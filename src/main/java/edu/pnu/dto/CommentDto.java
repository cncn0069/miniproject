package edu.pnu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
	private String username;
	private String nickname;
	private String content;
	private Long parent_id;
	private Long dashId;
	private int depth;
	private Long comment_id;
	private Boolean enabled;
	private LocalDateTime created_at;
}
