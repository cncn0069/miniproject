package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.DashBoard;

public interface DashBoardRepository extends JpaRepository<DashBoard, Long>{
	@Query("SELECT d FROM DashBoard d where d.enabled = true")
	Page<DashBoard> getDashBoardAllEnabledNotFalse(Pageable pageable);
	
	@Query("SELECT d FROM DashBoard d where d.title Like CONCAT(:q,'%') and d.enabled = true")
	Page<DashBoard> getDashBoardByQAndEnabledNotFalse(Pageable pageable,String q);
	
	@Query("SELECT d FROM DashBoard d where d.nickname Like :writer and d.enabled = true")
	Page<DashBoard> getDashBoardByNickNameAndEnabledNotFalse(Pageable pageable,String writer);
	
	@Query("SELECT d FROM DashBoard d where d.title Like CONCAT(:q,'%') and d.nickname Like :writer and d.enabled = true")
	Page<DashBoard> getDashBoardByQAndNickNameAndEnabledNotFalse(Pageable pageable,String q,String writer);
	
//	@Query("select new edu.pnu.dto.DashBoardDto(d.dashId, d.title, d.content, d.username, d.nickname, d.createdAt) from DashBoard d")
	@Query("SELECT d FROM DashBoard d where d.dashId = :id and d.enabled = true")
	DashBoard getByIdEnabledNotFalse(Long id);
	
	@Query("select distinct d.nickname from DashBoard d")
	List<String> getNickNames();
}
