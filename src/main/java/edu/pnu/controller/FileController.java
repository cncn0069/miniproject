package edu.pnu.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
   
   @GetMapping("/api/image/processed/{imageName}")
   public ResponseEntity<ApiResponseDTO<ImagePermitResultImageDTO>> getProcessedImage(@PathVariable String imageName) throws IOException{
	   log.info("최종 결과 이미지요청",imageName);
//       ResponseEntity<byte[]> result = fileService.getImage(imageName);
      return imageProcessService.getImageFromFastApiFinalResult(imageName);
      
   }
}
