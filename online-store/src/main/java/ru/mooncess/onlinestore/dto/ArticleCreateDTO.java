package ru.mooncess.onlinestore.dto;

import lombok.Data;
import ru.mooncess.onlinestore.entity.Category;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
public class ArticleCreateDTO {
    private String name;
    private String description;
    private String quantityOfPlayers;
    private String gameTime;
    private String yearOfRelease;
    private String producer;
    private Short recommendedAge;
    private Double oldPrice;
    private Double actualPrice;
    private Integer reserves;
    private List<Long> category;
}
