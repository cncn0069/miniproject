package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.APILog;



public interface APILogRepository extends JpaRepository<APILog, Long>{

}
