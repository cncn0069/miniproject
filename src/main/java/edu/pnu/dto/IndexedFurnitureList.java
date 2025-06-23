package edu.pnu.dto;

import java.util.List;

import edu.pnu.domain.Furniture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexedFurnitureList {
    private int index;
    private Furniture furnitureList;
}
