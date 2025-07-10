package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long>{

}
