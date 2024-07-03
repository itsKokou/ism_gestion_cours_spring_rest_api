package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.ClasseResponseDto;
import projet.core.data.dto.ModuleResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailModuleResponseDto {
    private List<ModuleResponseDto> modules;
    private String professeur;

    public static DetailModuleResponseDto toDto(List<ModuleResponseDto> modulesDtos, String professeur){
        return DetailModuleResponseDto.builder()
                .modules(modulesDtos)
                .professeur(professeur)
                .build();
    }
}
