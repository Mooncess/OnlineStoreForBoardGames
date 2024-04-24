package ru.mooncess.onlinestore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentCreateDTO {
    private String content;
    private Long articleId;
}
