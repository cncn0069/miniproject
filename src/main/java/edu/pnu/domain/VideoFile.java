package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoFile {
	@Id
	@Column(name = "order_id")
	private String orderId;
	@OneToOne
	@MapsId
	@JoinColumn(name="order_id")
	private OrderTable orderTable;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "create_at")
	private LocalDateTime createAt;
}
