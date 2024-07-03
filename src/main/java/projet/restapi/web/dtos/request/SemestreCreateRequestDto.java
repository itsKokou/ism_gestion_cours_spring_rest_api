package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemestreCreateRequestDto {
    private Long id;
    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;
    private Boolean isActive;
    private Boolean isArchived;
    private Long niveau;

    public Semestre toEntity(Niveau niveau){
        Semestre semestre = Semestre.builder()
                .libelle(this.libelle)
                .isActive(this.isActive)
                .niveau(niveau)
                .build();
        semestre.setIsArchived(isArchived);
        return semestre;
    }

}
