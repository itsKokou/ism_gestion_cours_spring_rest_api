package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Module;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleCreateRequestDto {
    private Long id;
    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;
    private Boolean isArchived;

    public Module toEntity(){
        Module module = Module.builder()
                .libelle(this.libelle)
                .build();
        module.setIsArchived(isArchived);
        return module;
    }

}
