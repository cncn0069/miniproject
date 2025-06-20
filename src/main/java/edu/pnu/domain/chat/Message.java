package edu.pnu.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	private String type;
	private String sender;
	private String receiver;
	private Object data;
	
	public void newConnect() {
		this.type = "new";
	}
	
	public void closeConnect() {
		this.type = "close";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Message{" +
				"type='" + type + '\'' +
				", sender='" + sender + '\''+
				", receiver'" + receiver + '\'' +
				", data=" + data +
				'}';
	}
}
