package projet.restapi.web.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.AnneeScolaire;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnneeScolaireCreateRequestDto {
    private Long id;
    private String libelle;
    private boolean isActive;
    private boolean isArchived;

    public AnneeScolaire toEntity(){
        AnneeScolaire annee = AnneeScolaire.builder()
                .libelle(this.libelle)
                .isActive(this.isActive)
                .build();
        annee.setIsArchived(isArchived);
        return annee;
    }

}
