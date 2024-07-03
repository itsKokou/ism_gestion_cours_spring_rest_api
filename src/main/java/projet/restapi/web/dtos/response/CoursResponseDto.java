package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.ModuleResponseDto;
import projet.core.data.dto.ProfesseurResponseDto;
import projet.core.data.dto.SemestreResponseDto;
import projet.core.data.entities.Classe;
import projet.core.data.entities.Cours;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursResponseDto {
    private Long id;
    private String date;
    private ModuleResponseDto module;
    private ProfesseurResponseDto professeur;
    private List<ClasseResponseDto> classes;
    private SemestreResponseDto semestre;
    private int nbreHeureTotal;
    private int nbreHeurePlanifie;
    private int nbreHeureRealise;
    private String etat;

    public static CoursResponseDto toDto(Cours cours) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return CoursResponseDto
                .builder()
                .id(cours.getId())
                .date(sdf.format(cours.getCreatedAt()))
                .module(ModuleResponseDto.toDto(cours.getModule()))
                .professeur(ProfesseurResponseDto.toDto(cours.getProfesseur()))
                .classes(cours.getClasses().stream().map(ClasseResponseDto::toDto).toList())
                .semestre(SemestreResponseDto.toDto(cours.getSemestre()))
                .nbreHeureTotal(cours.getNbreHeureTotal())
                .nbreHeurePlanifie(cours.getNbreHeurePlanifie())
                .nbreHeureRealise(cours.getNbreHeureRealise())
                .etat(cours.getEtat().name())
                .build();

    }
}


