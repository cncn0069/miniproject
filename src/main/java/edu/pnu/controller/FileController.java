package edu.pnu.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.pnu.service.FileService;

@RestController
public class FileController {

	
	@Autowired
	private FileService fileService;
	
	@PostMapping("api/upload/image")
	public String uploadImage(MultipartFile image) throws Exception{
		String fileUrl = fileService.uploadImage(image);
		return fileUrl;
	}
	
	@GetMapping("/images/{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException{
		 ResponseEntity<byte[]> result = fileService.getImage(imageName);
		
		return result;
		
	}
	
	@GetMapping("/api/image/processed/{imagePath}")
	public ResponseEntity<byte[]> getProcessedImage(@PathVariable String imagePath) throws IOException{
		 ResponseEntity<byte[]> result = fileService.getImage(imagePath);
		return result;
		
	}
	
}
