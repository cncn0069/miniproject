package edu.pnu.decoration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;

import edu.pnu.dto.MainDto;
import edu.pnu.dto.ObjectDto;

public class ObjectDeco {
	MainDto data;
	
	public ObjectDeco(MainDto data) {
		// TODO Auto-generated constructor stub
		this.data = data;
	}
	
	public ObjectDto getObjectDtoDeco(){
		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			return ObjectDto.builder()
					.caller("string")
//					.status(200)
					.content(this.data)
					.URL(ip+":8080")
					.message("")
					.build();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ObjectDto.builder()
				.caller("string")
//				.status(200)
				.content(this.data)
				.message("Error!")
				.build();
	}
}
