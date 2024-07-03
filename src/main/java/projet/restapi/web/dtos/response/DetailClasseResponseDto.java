package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailClasseResponseDto {
    private List<ClasseResponseDto> classes;
    private String professeur;

    public static DetailClasseResponseDto toDto(List<ClasseResponseDto> classeDtos, String professeur){
        return DetailClasseResponseDto.builder()
                .classes(classeDtos)
                .professeur(professeur)
                .build();
    }
}
