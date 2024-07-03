package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Salle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalleResponseDto {
    private Long id;
    private String libelle;
    private Integer nbrePlace;

    public static SalleResponseDto toDto(Salle salle){
        return SalleResponseDto.builder()
                .id(salle.getId())
                .libelle(salle.getLibelle())
                .nbrePlace(salle.getNbrePlace())
                .build();
    }
}
