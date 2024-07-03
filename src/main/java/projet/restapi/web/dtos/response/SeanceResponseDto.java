package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import projet.core.data.entities.Professeur;
import projet.core.data.entities.Salle;
import projet.core.data.entities.Seance;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeanceResponseDto {
    private Long id;
    private String start;
    private String end;
    private String title;
    private String description;
    private String location;
    private String textColor;
    private String color;

    public static SeanceResponseDto toDto(Seance seance){
        List<String> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Professeur prof = seance.getProfesseur() !=null ? seance.getProfesseur() : seance.getCours().getProfesseur();
        String li = seance.getSalle() != null ? "SALLE " + seance.getSalle().getLibelle() : seance.getCodeSeance();
        String lieu = "Lieu : " + li;
        String desc = prof.getNomComplet();
        // Création du format de date
        String start = sdf.format(seance.getDate()) + ' ' + seance.getHeureD().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String end = sdf.format(seance.getDate())  + ' ' + seance.getHeureF().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String color;
        if (seance.getIsAbsence()){
            color = "#359EF5";
        }else {
            String date2 = sdf.format(seance.getDate());
            int year = Integer.parseInt(date2.split("-")[0]);
            int month = Integer.parseInt(date2.split("-")[1]);
            int day = Integer.parseInt(date2.split("-")[2]);
            LocalDateTime dateToday  = LocalDateTime.now();
            LocalDateTime dateFinSession = LocalDateTime.of(year,month,day,seance.getHeureF().getHour(),seance.getHeureF().getMinute(),seance.getHeureF().getSecond());
            if (dateToday.isBefore(dateFinSession)){
                color = "#F53558";
            }else{
                color ="#35F5C1";
            }
        }

        return SeanceResponseDto.builder()
                .id(seance.getId())
                .start(start)
                .end(end)
                .title(seance.getCours().getModule().getLibelle())
                .description(desc)
                .location(lieu)
                .textColor("#000000")
                .color(color)
                .build();
    }
}
