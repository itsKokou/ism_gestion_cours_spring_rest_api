package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.FiliereResponseDto;
import projet.core.data.dto.NiveauResponseDto;
import projet.core.data.entities.Classe;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfHomeResponseDto {
    private Integer nbreCours;
    private Integer nbreClasse;
    private Integer nbreModule;
    private Integer nbreDeclaration;
    private List<ClasseResponseDto> classes;

    public static ProfHomeResponseDto toDto(List<ClasseResponseDto> classes, int nbreCours,int nbreClasse,int nbreModule,int nbreDeclaration){
        return ProfHomeResponseDto.builder()
                .nbreCours(nbreCours)
                .nbreClasse(nbreClasse)
                .nbreModule(nbreModule)
                .nbreDeclaration(nbreDeclaration)
                .classes(classes)
                .build();
    }
}
