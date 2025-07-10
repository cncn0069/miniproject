package edu.pnu.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.pnu.domain.ImageFile;
import edu.pnu.persistence.ImageFileRepository;
import edu.pnu.util.ServerIpUtil;

@Service
public class FileService {
   @Value("${file.upload.directory}")
   private String fileUploadPath;
   @Value("${file.permit.directory}")
   private String permitedImagePath;
   @Autowired
   private ImageFileRepository imageFileRepository;
   
   
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
