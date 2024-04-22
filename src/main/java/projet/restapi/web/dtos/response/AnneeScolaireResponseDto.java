package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.AnneeScolaire;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnneeScolaireResponseDto {
    private Long id;
    private String libelle;
    private Boolean isActive;

    public static AnneeScolaireResponseDto toDto(AnneeScolaire anneeScolaire){
        return AnneeScolaireResponseDto.builder()
                .id(anneeScolaire.getId())
                .libelle(anneeScolaire.getLibelle())
                .isActive(anneeScolaire.getIsActive())
                .build();
    }
}
