package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.AbsenceResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailAbsenceResponseDto {
    private List<AbsenceResponseDto> absences;
    private String etudiant;

    public static DetailAbsenceResponseDto toDto(List<AbsenceResponseDto> absencesDtos, String etudiant){
        return DetailAbsenceResponseDto.builder()
                .absences(absencesDtos)
                .etudiant(etudiant)
                .build();
    }
}
