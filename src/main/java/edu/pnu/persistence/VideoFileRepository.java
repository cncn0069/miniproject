package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.VideoFile;

public interface VideoFileRepository extends JpaRepository<VideoFile, String>{

}
