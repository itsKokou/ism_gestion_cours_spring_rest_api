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
public class DetailDossierResponseDto {
    private List<InscriptionResponseDto> inscriptions;
    private EtudiantResponseDto etudiant;
    private String classeActuelle;

    public static DetailDossierResponseDto toDto(List<InscriptionResponseDto> inscriptions, EtudiantResponseDto etudiant, String classe){
        return DetailDossierResponseDto.builder()
                .inscriptions(inscriptions)
                .etudiant(etudiant)
                .classeActuelle(classe)
                .build();
    }
}
