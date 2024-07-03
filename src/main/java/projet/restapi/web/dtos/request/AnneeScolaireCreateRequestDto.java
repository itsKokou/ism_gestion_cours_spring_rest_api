package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.dto.NiveauResponseDto;
import projet.core.data.entities.AnneeScolaire;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnneeScolaireCreateRequestDto {
    private Long id;
    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;
    private Boolean isActive;
    private Boolean isArchived;

    public AnneeScolaire toEntity(){
        AnneeScolaire annee = AnneeScolaire.builder()
                .libelle(this.libelle)
                .isActive(this.isActive)
                .build();
        annee.setIsArchived(isArchived);
        return annee;
    }

}
