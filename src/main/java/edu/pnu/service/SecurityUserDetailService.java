package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;

@Service
public class SecurityUserDetailService implements UserDetailsService {
	
	@Autowired
	private MemberRepository mrp;

	@Autowired
	private LogService logservice;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member m = mrp.findById(username).orElseThrow(() -> new UsernameNotFoundException("에러 : "+ username + " 를 찾지 못하였습니다."));
		
		//로그인 로그
		logservice.setloginLog(m.getUsername());
		
		return new User(m.getUsername(),m.getPassword(),AuthorityUtils.createAuthorityList(m.getRole().toString()));
	}

}
