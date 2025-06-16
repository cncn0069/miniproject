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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class DashBoardDto {
	private Long dashId;
    private String title;
    private String content;
    private String username; // Member의 username (문자열)
    private String nickname; // Member의 nickname (문자열)
    private LocalDateTime createdAt;
    private boolean enabled;
}