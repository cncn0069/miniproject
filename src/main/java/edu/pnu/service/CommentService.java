package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Comment;
import edu.pnu.domain.DashBoard;
import edu.pnu.dto.CommentDto;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.persistence.CommentRepository;
import edu.pnu.persistence.DashBoardRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository commentRepo;
	
	@Autowired
	DashBoardRepository dashBoardRepository;
	
	public void writeComment(CommentDto dto){
		
		DashBoard dashboard = dashBoardRepository.findById(dto.getDashId()).get();
			
			//그냥 새로 댓글을 만든다면
			if(dto.getParentId() == null) {
				commentRepo.save(Comment.builder()
						.dashBoard(dashboard)
						.content(dto.getContent())
						.username(dto.getUsername())
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.enable(true)
						.build());
			}else {
			//대 댓글
				commentRepo.save(Comment.builder()
						.dashBoard(dashboard)
						.content(dto.getContent())
						.username(dto.getUsername())
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.parent_id(dto.getParentId())
						.enable(true)
						.build());
				
			}
			

		
	}
	
	public List<Comment> readComment(DashBoardDto dto){

		List<Comment> result = new ArrayList<>();
		//같은 게시글이면서
		//제일 최상단의 댓글들
		 List<Comment> comments = commentRepo.getCommentByParentIdIsNullAndDashId(dto.getDash_id());
		 Deque<Comment> dfsCom = new ArrayDeque<>();
		 
		 for(Comment comment:comments) {
			 dfsCom.offer(comment);
		 }
		 //최상단의 댓글들을 돌면서
		while(!dfsCom.isEmpty()) {
			Comment comment = dfsCom.poll();
			result.add(comment);
			System.out.println(comment.getContent());
			//상단 comment와 같은 comment들 즉 하위 comment 찾기
			List<Comment> subcomments = commentRepo.getCommentDashIdAndParentIdOrderByCreatedAtDesc(dto.getDash_id(),comment.getComment_id());
			for(Comment subcomment :subcomments) {
				subcomment.setDepth(comment.getDepth() + 1);
				dfsCom.addFirst(subcomment);
			}
			
		}
		
		return result;
	}
}
