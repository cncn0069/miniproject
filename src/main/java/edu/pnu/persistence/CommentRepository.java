package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query("SELECT c FROM Comment c WHERE c.dashBoard.dash_id = :dashId")
	List<Comment> getCommentByDashId(@Param("dashId") Long dashId);
	
	
	@Query("SELECT c FROM Comment c WHERE c.parent_id is null and c.dashBoard.dash_id = :dashId")
	List<Comment> getCommentByParentIdIsNullAndDashId(Long dashId);
	
	@Query("SELECT c From Comment c	where c.parent_id = :parentId and c.dashBoard.dash_id = :dashId")
	List<Comment> getCommentByDashIdAndParentIdOrderByCreatedAt(Long dashId, Long parentId);
	@Query("SELECT c From Comment c	where c.parent_id = :parentId and c.dashBoard.dash_id = :dashId order by c.created_at")
	List<Comment> getCommentDashIdAndParentIdOrderByCreatedAtDesc(Long dashId, Long parentId);
}
