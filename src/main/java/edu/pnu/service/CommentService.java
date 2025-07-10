package edu.pnu.service;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Comment;
import edu.pnu.domain.DashBoard;
import edu.pnu.domain.Member;
import edu.pnu.dto.CommentDto;
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
		DashBoard dashboard = dashBoardRepository.findById(dto.getDashId()).orElseThrow(()->new IllegalArgumentException("게시글이 존재하지 않습니다!"));
		Member member;
		try {
			member = memberRepository.findById(dto.getUsername()).orElseThrow(()->new UsernameNotFoundException("NoUserName"));
			//그냥 새로 댓글을 만든다면
			if(dto.getParent_id() == null) {
				commentRepo.save(Comment.builder()
						.dashId(dashboard)
						.content(dto.getContent())
						.username(member)
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.enabled(true)
						.build());
			}else {
			//대 댓글
				
				Comment parent = commentRepo.findById(dto.getParent_id()).orElseThrow(()->new IllegalArgumentException("댓글이 존재하지 않습니다!"));

				
				commentRepo.save(Comment.builder()
						.dashId(dashboard)
						.content(dto.getContent())
						.username(member)
						.nickname(dto.getNickname())
						.created_at(LocalDateTime.now())
						.parent_id(dto.getParent_id())
						.depth(parent.getDepth()+1)
						.enabled(true)
						.build());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public List<CommentDto> readComment(CommentDto dto){

		List<CommentDto> results = new ArrayList<>();
		//같은 게시글이면서
		//제일 최상단의 댓글들
		 List<Comment> comments = commentRepo.getCommentByParentIdIsNullAndDashId(dto.getDashId());
		 Deque<Comment> dfsCom = new ArrayDeque<>();
		 
		 for(Comment comment:comments) {
			 dfsCom.offer(comment);
		 }
		 //최상단의 댓글들을 돌면서
		while(!dfsCom.isEmpty()) {
			Comment comment = dfsCom.poll();
	
			results.add(CommentDto.builder()
					.content(comment.getEnabled() ? comment.getContent() : "삭제된 댓글입니다.")
					.parent_id(comment.getParent_id())
					.username(comment.getUsername().getUsername())
					.nickname(comment.getNickname())
					.created_at(comment.getCreated_at())
					.dashId(comment.getDashId().getDashId())
					.comment_id(comment.getComment_id())
					.depth(comment.getDepth())
					.enabled(comment.getEnabled())
					.build());
			
			//상단 comment와 같은 comment들 즉 하위 comment 찾기
			List<Comment> subcomments = commentRepo.getCommentDashIdAndParentIdOrderByCreatedAtDesc(dto.getDashId(),comment.getComment_id());
			//추가
			
			for(Comment subcomment:subcomments) {
				 dfsCom.offerFirst(subcomment);
			 }
		}		
		
		return results;
	}
	public void deleteComment(Long id) {
		
		Comment comment = commentRepo.findById(id).orElseThrow(()->new IllegalAccessError("없는 게시글입니다."));
		comment.setEnabled(false);
		
		
		commentRepo.save(comment);
	}
	
	
}
