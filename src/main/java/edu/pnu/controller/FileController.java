package edu.pnu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.pnu.dto.ApiResponseDTO;
import edu.pnu.dto.ImagePermitResultImageDTO;
import edu.pnu.service.FileService;
import edu.pnu.service.ImageProcessService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class FileController {

	@Autowired
   private ImageProcessService imageProcessService;
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
   
   @PostMapping("/video/upload")
   public ResponseEntity<String> uploadVideo(@RequestParam("orderId") String orderId,@RequestParam("file") MultipartFile uploadVideo) {
	   	return fileService.uploadVideo(uploadVideo,orderId);
   }

   @GetMapping("/download/vidio/{orderId}")
   public ResponseEntity<Resource> downloadVideo(@PathVariable String orderId) throws MalformedURLException {
	   return fileService.downloadVideo(orderId);
   }

   
   @GetMapping("/api/image/processed/{imageName}")
   public ResponseEntity<ApiResponseDTO<ImagePermitResultImageDTO>> getProcessedImage(@PathVariable String imageName) throws IOException{
	   log.info("최종 결과 이미지요청",imageName);
//       ResponseEntity<byte[]> result = fileService.getImage(imageName);
      return imageProcessService.getImageFromFastApiFinalResult(imageName);
      
   }
}
