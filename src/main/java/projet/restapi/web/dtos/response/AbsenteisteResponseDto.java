package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Inscription;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsenteisteResponseDto {
    private Long id;
    private String matricule;
    private String nomComplet;
    private String email;
    private String classe;
    private Integer nbreHeure;

    public static AbsenteisteResponseDto toDto(Inscription inscription, int nbre){
        return AbsenteisteResponseDto.builder()
                .id(inscription.getEtudiant().getId())
                .matricule(inscription.getEtudiant().getMatricule())
                .nomComplet(inscription.getEtudiant().getNomComplet())
                .email(inscription.getEtudiant().getLogin())
                .classe(inscription.getClasse().getLibelle())
                .nbreHeure(nbre)
                .build();
    }
}
