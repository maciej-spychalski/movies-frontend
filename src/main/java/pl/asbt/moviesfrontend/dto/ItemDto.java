package pl.asbt.moviesfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    private String movieTitle;
    private Long movieId;
    private Integer quantity;
    private BigDecimal price;
}
