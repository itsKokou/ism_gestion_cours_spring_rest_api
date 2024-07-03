package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.ModuleResponseDto;
import projet.core.data.dto.ProfesseurResponseDto;
import projet.core.data.entities.Classe;
import projet.core.data.entities.Enseignement;
import projet.core.data.entities.Filiere;
import projet.core.data.entities.Niveau;
import projet.restapi.web.dtos.response.ClasseResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnseignementCreateRequestDto {
    private Long id;
    @NotNull(message = "Le prof est obligatoire")
    private ProfesseurResponseDto professeur;
    @NotNull(message = "La classe est obligatoire")
    private ClasseResponseDto classe;
    @NotEmpty(message = "Au moins un module")
    private List<ModuleResponseDto> modules;

}
