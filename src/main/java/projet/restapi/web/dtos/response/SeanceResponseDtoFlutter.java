package projet.restapi.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.core.data.entities.Professeur;
import projet.core.data.entities.Seance;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeanceResponseDtoFlutter {
    private Long id;
    private String eventName;
    private String from;
    private String to;
    private String background;
    private Boolean isAllDay;

    public static SeanceResponseDtoFlutter toDto(Seance seance){
        List<String> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Professeur prof = seance.getProfesseur() !=null ? seance.getProfesseur() : seance.getCours().getProfesseur();
        String li = seance.getSalle() != null ? "SALLE " + seance.getSalle().getLibelle() : seance.getCodeSeance();
        String lieu = "Lieu : " + li;

        String date2 = sdf.format(seance.getDate());
        int year = Integer.parseInt(date2.split("-")[0]);
        int month = Integer.parseInt(date2.split("-")[1]);
        int day = Integer.parseInt(date2.split("-")[2]);

        LocalDateTime start = LocalDateTime.of(year,month,day,seance.getHeureD().getHour(),seance.getHeureD().getMinute(),seance.getHeureD().getSecond());
        LocalDateTime end = LocalDateTime.of(year,month,day,seance.getHeureF().getHour(),seance.getHeureF().getMinute(),seance.getHeureF().getSecond());

        String color;
        if (seance.getIsAbsence()){
            color = "0xFF1E74FF";
        }else {
            LocalDateTime dateToday  = LocalDateTime.now();
            LocalDateTime dateFinSession = LocalDateTime.of(year,month,day,seance.getHeureF().getHour(),seance.getHeureF().getMinute(),seance.getHeureF().getSecond());
            if (dateToday.isBefore(dateFinSession)){
                color = "0xFF17C651";
            }else{
                color ="0xFFF51B64";
            }
        }

        String infos = seance.getCours().getModule().getLibelle() + " \n" +
                prof.getNomComplet() + " \n" + lieu + " \n" +
                seance.getHeureD().format(DateTimeFormatter.ofPattern("HH:mm")) + "-" +
                seance.getHeureF().format(DateTimeFormatter.ofPattern("HH:mm"));

        return SeanceResponseDtoFlutter.builder()
                .id(seance.getId())
                .from(start.toString())
                .to(end.toString())
                .eventName(infos)
                .background(color)
                .isAllDay(false)
                .build();
    }
}
