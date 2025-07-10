package edu.pnu.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.JoinColumn;

@Getter@Setter@ToString
@Entity@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member{
	@Id
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(columnDefinition = "varchar(32)",nullable = false)
	private String nickname;
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "username"))
	@Column(columnDefinition = "varchar(32) default 'ROLE_MEMBER'",nullable = false)
	private List<Role> role;
	private String address;
	@Column(columnDefinition = "boolean default true")
	private boolean enabled;
	@Column(nullable = false)
	private LocalDateTime created_at;
}
