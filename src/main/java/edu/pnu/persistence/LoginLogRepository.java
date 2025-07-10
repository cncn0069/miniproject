package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long>{

}
