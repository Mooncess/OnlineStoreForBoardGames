package ru.mooncess.onlinestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String description;
    @Column(name = "quantity_of_players")
    private Short quantityOfPlayers;
    @Column(name = "game_time")
    private String gameTime;
    @Column(name = "year_of_release")
    private String yearOfRelease;
    private String producer;
    @Column(name = "recommended_age")
    private Short recommendedAge;
    @Column(name = "old_price")
    private Double oldPrice;
    @Column(name = "actual_price")
    private Double actualPrice;
    private String imageURN;
    private Integer reserves;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;
}
