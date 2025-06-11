package edu.pnu.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Comment;
import edu.pnu.domain.DashBoard;
import edu.pnu.domain.Member;
import edu.pnu.dto.CommentDto;
import edu.pnu.dto.DashBoardDto;
import edu.pnu.persistence.CommentRepository;
import edu.pnu.persistence.DashBoardRepository;
import edu.pnu.persistence.MemberRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository commentRepo;
	
	@Autowired
	DashBoardRepository dashBoardRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	public void writeComment(CommentDto dto){
		
		DashBoard dashboard = dashBoardRepository.findById(dto.getDashId()).get();
		Member member;
		try {
			member = memberRepository.findById(dto.getUsername()).orElseThrow(()->new UserPrincipalNotFoundException("NoUserName"));
			//그냥 새로 댓글을 만든다면
			if(dto.getParentId() == null) {
				commentRepo.save(Comment.builder()
						.dash_id(dashboard)
						.content(dto.getContent())
						.username(member)
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.enable(true)
						.build());
			}else {
			//대 댓글
				commentRepo.save(Comment.builder()
						.dash_id(dashboard)
						.content(dto.getContent())
						.username(member)
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.parent_id(dto.getParentId())
						.enable(true)
						.build());
			}
			
		} catch (UserPrincipalNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public List<CommentDto> readComment(DashBoardDto dto){

		List<CommentDto> results = new ArrayList<>();
//		//같은 게시글이면서
//		//제일 최상단의 댓글들
//		 List<Comment> comments = commentRepo.getCommentByParentIdIsNullAndDashId(dto.getDash_id());
//		 Deque<Comment> dfsCom = new ArrayDeque<>();
//		 
//		 for(Comment comment:comments) {
//			 dfsCom.offer(comment);
//		 }
//		 //최상단의 댓글들을 돌면서
//		while(!dfsCom.isEmpty()) {
//			Comment comment = dfsCom.poll();
//			results.add(CommentDto.builder()
//					.content(comment.getContent())
//					.parentId(comment.getParent_id())
//					.username(comment.getUsername().getUsername())
//					.nickname(comment.getNickname())
//					.created_at(comment.getCreated_at())
//					.dashId(comment.getDash_id().getDash_id())
//					.comment_id(comment.getComment_id())
//					.depth(comment.getDepth())
//					.enable(comment.getEnable())
//					.build());
//			System.out.println(comment.getContent());
//			//상단 comment와 같은 comment들 즉 하위 comment 찾기
//			List<Comment> subcomments = commentRepo.getCommentDashIdAndParentIdOrderByCreatedAtDesc(dto.getDash_id(),comment.getComment_id());
//			for(Comment subcomment :subcomments) {
//				subcomment.setDepth(comment.getDepth() + 1);
//				dfsCom.addFirst(subcomment);
//			}
//			
//		}		
		
		return results;
	}
}
