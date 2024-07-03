package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Classe;
import projet.core.data.entities.Filiere;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClasseCreateRequestDto {
    private Long id;
    @NotBlank(message = "Le libelle est obligatoire")
    private String libelle;
    private Integer effectif;
    private Boolean isPlanned;
    private Boolean isArchived;
    @NotNull(message = "Le niveau est obligatoire")
    private Long niveau;
    @NotNull(message = "La filière est obligatoire")
    private Long filiere;

    public Classe toEntity(Niveau niveau, Filiere filiere){
        Classe classe =  Classe.builder()
                .libelle(libelle)
                .effectif(effectif)
                .isPlanned(isPlanned)
                .niveau(niveau)
                .filiere(filiere)
                .build();
        classe.setIsArchived(isArchived);
        return classe;
    }

}
