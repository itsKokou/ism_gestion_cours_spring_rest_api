package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.InscriptionEtudiantResponseDto;
import projet.core.data.entities.Etudiant;
import projet.core.data.entities.Inscription;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InscriptionResponseDto {
    private Long id;
    private String date;
    private String etudiant;
    private String matricule;
    private String email;
    private String classe;
    private String anneeScolaire;

    public static InscriptionResponseDto toDto(Inscription inscription){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return InscriptionResponseDto.builder()
                .id(inscription.getId())
                .date(sdf.format(inscription.getCreatedAt()))
                .etudiant(inscription.getEtudiant().getNomComplet())
                .matricule(inscription.getEtudiant().getMatricule())
                .email(inscription.getEtudiant().getLogin())
                .classe(inscription.getClasse().getLibelle())
                .anneeScolaire(inscription.getAnneeScolaire().getLibelle())
                .build();
    }

}
