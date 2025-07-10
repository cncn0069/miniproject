package edu.pnu.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.pnu.domain.ImageFile;
import edu.pnu.domain.OrderTable;
import edu.pnu.domain.VideoFile;
import edu.pnu.persistence.ImageFileRepository;
import edu.pnu.persistence.OrderRepository;
import edu.pnu.persistence.VideoFileRepository;
import edu.pnu.util.ServerIpUtil;

@Service
public class FileService {
   @Value("${file.upload.directory}")
   private String fileUploadPath;
   @Value("${file.permit.directory}")
   private String permitedImagePath;
   @Autowired
   private ImageFileRepository imageFileRepository;
   @Autowired
   private VideoFileRepository fileRepository;
   @Autowired
   private OrderRepository orderRepository;
   
   public ResponseEntity<String> uploadVideo(MultipartFile uploadVideo,String orderId) {
	   String fileName = UUID.randomUUID() + ".mp4";
	   
	   OrderTable ot = orderRepository.findById(orderId).get();
	   
	   fileRepository.save(VideoFile.builder()
			   .orderTable(ot)
			   .fileName(fileName)
			   .createAt(LocalDateTime.now())
			   .build());
	
       Path destinationFile = Paths.get("/upload/video/", fileName);

       try (InputStream inputStream = uploadVideo.getInputStream()) {
           Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
           return ResponseEntity.ok("업로드 성공: " + fileName);
       } catch (IOException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
       }
   }
   
   public ResponseEntity<Resource> downloadVideo(String orderId){
	   
	   
	   VideoFile videoFile = fileRepository.findById(orderId).get();
	   
	   Path videoPath = Paths.get("/upload/video/dir", videoFile.getFileName());
       Resource resource;
	try {
		resource = new UrlResource(videoPath.toUri());
		if (!resource.exists() || !resource.isReadable()) {
	           throw new RuntimeException("파일을 찾을 수 없습니다.");
	       }

	       return ResponseEntity.ok()
	               .contentType(MediaType.APPLICATION_OCTET_STREAM)
	               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoFile.getFileName() + "\"")
	               .body(resource);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
   }
   
   public String uploadImage(MultipartFile image) throws Exception{
      String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
      String filePath = fileUploadPath + "/images/" +fileName;
      image.transferTo(new File(filePath));
      
      ImageFile imageFile = new ImageFile();
      imageFile.setFileName(fileName);
      imageFile.setFilePath(filePath);
      imageFile.setCreated_at(LocalDateTime.now());
      imageFileRepository.save(imageFile);
      
      ServerIpUtil ipUtil = new ServerIpUtil();
      
      return "http://"+ ipUtil.getServerIp() + ":8080"+ "/images/" + fileName;
   }
   
   public ResponseEntity<byte[]> getImage(String imageName) throws IOException{
      File imageFile = new File(fileUploadPath + "/images/" +imageName);
      byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
      
      String contentType = Files.probeContentType(imageFile.toPath());
      
      return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(imageBytes);
      
   }
   
   
   
   public ResponseEntity<byte[]> getProcessedImage(String imageName) throws IOException{
	   
      File imageFile = new File(permitedImagePath + "/permit/" +imageName);
      byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
      
      String contentType = Files.probeContentType(imageFile.toPath());
      
      return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(imageBytes);
      
   }
}
