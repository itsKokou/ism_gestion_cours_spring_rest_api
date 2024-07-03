package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.FiliereResponseDto;
import projet.core.data.dto.NiveauResponseDto;
import projet.core.data.entities.Classe;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClasseResponseDto {
    private Long id;
    private String libelle;
    private Integer effectif;
    private NiveauResponseDto niveau;
    private FiliereResponseDto filiere;

    public static ClasseResponseDto toDto(Classe classe){
        return ClasseResponseDto.builder()
                .id(classe.getId())
                .libelle(classe.getLibelle())
                .effectif(classe.getEffectif())
                .niveau(NiveauResponseDto.toDto(classe.getNiveau()))
                .filiere(FiliereResponseDto.toDto(classe.getFiliere()))
                .build();
    }
}
