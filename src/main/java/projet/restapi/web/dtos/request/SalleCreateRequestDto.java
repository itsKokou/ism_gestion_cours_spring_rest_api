package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.AnneeScolaire;
import projet.core.data.entities.Salle;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalleCreateRequestDto {
    private Long id;
    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;
    @NotNull
    @Min(value = 10, message = "Le nombre de place doit être supérieur à 10")
    private Integer nbrePlace;
    private Boolean isPlanned;
    private Boolean isArchived;

    public Salle toEntity(){
        Salle salle = Salle.builder()
                .libelle(this.libelle)
                .nbrePlace(this.nbrePlace)
                .isPlanned(this.isPlanned)
                .build();
        salle.setIsArchived(isArchived);
        return salle;
    }

}
