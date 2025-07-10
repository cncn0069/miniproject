package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
@Entity@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
		name = "order_table",
		indexes = {
				@Index(name = "idx_order_user_created_at", columnList = "username, created_at")
			}
		)
public class OrderTable {
	@Id
	@Column(nullable = false,name = "order_id")
	private String orderId;
	@Column(nullable = false,name = "file_path")
	private String filePath;
	@ManyToOne
	@JoinColumn(name = "username")
	private Member member;
	@Column(nullable = false)
	private Long totalPrice;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
}
