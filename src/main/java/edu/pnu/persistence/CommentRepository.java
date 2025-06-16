package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query("SELECT c FROM Comment c WHERE c.dashId.dashId = :boardId")
	List<Comment> getCommentByDashId(@Param("boardId") Long boardId);
	
	
	@Query("SELECT c FROM Comment c WHERE c.parent_id is null and c.dashId.dashId = :boardId")
	List<Comment> getCommentByParentIdIsNullAndDashId(Long boardId);
	
	@Query("SELECT c From Comment c	where c.parent_id = :parentId and c.dashId.dashId = :boardId")
	List<Comment> getCommentByDashIdAndParentIdOrderByCreatedAt(Long boardId, Long parentId);
	@Query("SELECT c From Comment c	where c.parent_id = :parentId and c.dashId.dashId = :boardId order by c.created_at desc")
	List<Comment> getCommentDashIdAndParentIdOrderByCreatedAtDesc(Long boardId, Long parentId);
}
