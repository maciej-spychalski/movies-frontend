package pl.asbt.moviesfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageItemDto {

    private Long id;
    private String movieTitle;
    private Long movieId;
    private Integer quantity;
}

