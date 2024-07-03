package projet.restapi.web.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Salle;
import projet.core.data.entities.Seance;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeanceCreateRequestDto {
    private Long id;
    @NotNull
    @Min(value = 1, message = "L'id du cours est obligatoire")
    private Long idCours;
    @NotBlank(message = "La date est obligatoire")
    private String date;
    @NotBlank(message = "L'heure de début est obligatoire")
    private String heureD;
    @NotBlank(message = "L'heure de fin est obligatoire")
    private String heureF;
    private String code;
    private Long salle;
    private Long professeur;


    public Seance toEntity(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Seance seance = Seance.builder()

                .build();
        seance.setIsArchived(false);
        return seance;
    }

}
