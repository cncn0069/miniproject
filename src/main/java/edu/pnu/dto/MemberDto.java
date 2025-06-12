package edu.pnu.dto;

import java.time.LocalDateTime;

import edu.pnu.domain.Role;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDto {
	private String username;
	private String password;
	private String nickname;
	private Role role;
	private boolean enabled;
	private LocalDateTime created_at;
	private String jwtToken;
}
