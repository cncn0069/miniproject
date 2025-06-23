package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Furniture;
import lombok.extern.slf4j.Slf4j;

public interface FurnitureRepository extends JpaRepository<Furniture, Long>{
	
	@Query(value =
		    "SELECT * FROM (" +
		    "   SELECT * FROM `가구류` WHERE 품명 LIKE CONCAT('%', :itemName, '%') " +
		    "   UNION ALL " +
		    "   SELECT * FROM `가전제품` WHERE 품명 LIKE CONCAT('%', :itemName, '%') " +
		    "   UNION ALL " +
		    "   SELECT * FROM `생활용품 및 기타` WHERE 품명 LIKE CONCAT('%', :itemName, '%') " +
		    ") AS sub " +
		    "WHERE sub.규격 =	 :type " +
		    "LIMIT 1",
		    nativeQuery = true)
	Furniture getAllFurnitureByItemName(String itemName, String type);
}



