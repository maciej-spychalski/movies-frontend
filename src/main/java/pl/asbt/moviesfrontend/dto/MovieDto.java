package pl.asbt.moviesfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private DirectorDto directorDto;
    private List<WriterDto> writersDto = new ArrayList<>();
    private List<ActorDto> actorsDto = new ArrayList<>();
    private List<GenreDto> genresDto = new ArrayList<>();
    private Integer duration;
    private BigDecimal price;
}
