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
public class RPHomeResponseDto {
    private Integer nbreInscription;
    private Integer nbreClasse;
    private Integer nbreModule;
    private Integer nbreProfesseur;
    private List<AbsenteisteResponseDto> absenteistes;

    public static RPHomeResponseDto toDto(List<AbsenteisteResponseDto> absents, int nbreInscription, int nbreClasse, int nbreModule, int nbreProfesseur){
        return RPHomeResponseDto.builder()
                .nbreInscription(nbreInscription)
                .nbreClasse(nbreClasse)
                .nbreModule(nbreModule)
                .nbreProfesseur(nbreProfesseur)
                .absenteistes(absents)
                .build();
    }
}
