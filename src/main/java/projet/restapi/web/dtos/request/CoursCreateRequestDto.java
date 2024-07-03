package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursCreateRequestDto {
    private Long id;
    @NotNull
    @Min(value = 1, message = "Le semestre est obligatoire")
    private Long semestre;
    @NotNull
    @Min(value = 1, message = "Le module est obligatoire")
    private Long module;
    @NotNull
    @Min(value = 1, message = "Le professeur est obligatoire")
    private Long professeur;
    @NotNull
    @Min(value = 12, message = "L'heure ttale minimun est de 12")
    private Integer nbreHeureTotal;
    private Boolean isArchived;
    @NotEmpty(message = "Au moins une classe est requise pour le cours")
    private List<Long> classes;

    public Cours toEntity(){
        Cours cours =  Cours.builder()
                .build();
        cours.setIsArchived(isArchived);
        return cours;
    }

}
