package projet.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import projet.core.data.entities.Classe;
import projet.core.data.entities.Professeur;
import projet.core.data.entities.Salle;
import projet.core.services.ClasseService;
import projet.core.services.ProfesseurService;
import projet.core.services.SalleService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication(scanBasePackages = {
		"projet.core",
		"projet.restapi"
})
@RequiredArgsConstructor
public class RestApiApplication implements CommandLineRunner {
	private final ProfesseurService professeurService;
	private final ClasseService classeService;
	private final SalleService salleService;

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Date today = new Date();

		if (today.getMonth()== Calendar.AUGUST){
			List<Professeur> professeurList = professeurService.getAll(PageRequest.of(0,1000)).getContent();
			for (Professeur professeur : professeurList) {
				professeur.setIsPlanned(false);
				professeurService.save(professeur);
			}

			List<Classe> classeList = classeService.getAll(PageRequest.of(0,1000)).getContent();
			for (Classe classe : classeList) {
				classe.setIsPlanned(false);
				classeService.save(classe);
			}

			List<Salle> salleList = salleService.getAll(PageRequest.of(0,1000)).getContent();
			for (Salle salle : salleList) {
				salle.setIsPlanned(false);
				salleService.save(salle);
			}
		}

	}
}
