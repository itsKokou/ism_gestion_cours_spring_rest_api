package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Declaration;
import projet.core.data.entities.Salle;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeclarationCreateRequestDto {
    @NotNull
    @Min(value = 1, message = "L'utilisateur est obligatoire")
    private Long userId;
    @NotNull
    @Min(value = 1, message = "La seance est obligatoire")
    private Long seanceId;
    @NotBlank(message = "Le motif est obligatoire")
    private String motif;
    @NotBlank(message = "La description est obligatoire")
    private String description;



    public Declaration toEntity(){
        Declaration declaration = Declaration.builder()
                .createdAt(new Date())
                .motif(motif)
                .description(description)
                .build();
        return declaration;
    }

}
