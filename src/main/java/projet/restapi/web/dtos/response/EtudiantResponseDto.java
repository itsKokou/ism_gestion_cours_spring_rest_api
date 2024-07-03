package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Etudiant;
import projet.core.data.entities.Salle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtudiantResponseDto {
    private Long id;
    private String matricule;
    private String email;
    private String nomComplet;
    private String tuteur;
    private String photo;


    public static EtudiantResponseDto toDto(Etudiant etudiant){
        return EtudiantResponseDto.builder()
                .id(etudiant.getId())
                .matricule(etudiant.getMatricule())
                .email(etudiant.getLogin())
                .nomComplet(etudiant.getNomComplet())
                .tuteur(etudiant.getTuteur())
                .photo(etudiant.getPhoto())
                .build();
    }
}
