package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Furniture;

public interface FurnitureRepository extends JpaRepository<Furniture, Long>{
	
	@Query( value =   "SELECT id,`연번`,`품명`,`규격`,`수수료` FROM `가구류` WHERE `품명` LIKE :itemName " +
			          "UNION ALL " +
			          "SELECT id,`연번`,`품명`,`규격`,`수수료` FROM `가전제품` WHERE `품명` LIKE :itemName "+
			          "UNION ALL " +
			          "SELECT id,`연번`,`품명`,`규격`,`수수료` FROM `생활용품 및 기타` WHERE `품명` LIKE :itemName",
			  nativeQuery = true)
	List<Furniture> getAllFurnitureByItemName(String itemName);
}
