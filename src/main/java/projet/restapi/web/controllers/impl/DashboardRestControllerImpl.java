package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.entities.*;
import projet.core.data.entities.Module;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.web.controllers.DashboardRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.response.AbsenteisteResponseDto;
import projet.restapi.web.dtos.response.ClasseResponseDto;
import projet.restapi.web.dtos.response.ProfHomeResponseDto;
import projet.restapi.web.dtos.response.RPHomeResponseDto;

import java.util.*;
import java.util.stream.Stream;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class DashboardRestControllerImpl implements DashboardRestController {

    private final ProfesseurService professeurService;
    private final DeclarationService declarationService;
    private final CoursService coursService;
    private final InscriptionService inscriptionService;
    private final ModuleService moduleService;
    private final ClasseService classeService;
    private final EtudiantService etudiantService;
    private final AbsenceService absenceService;
    private final AnneeScolaireService anneeScolaireService;

    @Override
    public ResponseEntity<Map<Object, Object>> profHome(Long id) {
        Professeur professeur = professeurService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Professeur not found"));
        List<Module> modules = professeurService.getModuleByProfesseur(professeur, PageRequest.of(0,1000)).getContent();
        List<Classe> classes = professeurService.getClasseByProfesseur(professeur, PageRequest.of(0,1000)).getContent();
        List<Declaration> declarations =declarationService.getDeclarationsByUser(professeur, PageRequest.of(0,1000)).getContent();
        List<Cours> courss = coursService.getAllByProfesseur(professeur, PageRequest.of(0,1000)).getContent();
        Map<Object, Object> response = RestResponse.response(ProfHomeResponseDto.toDto(classes.stream().map(ClasseResponseDto::toDto).toList(),
                courss.size(),classes.size(),modules.size(),declarations.size()),HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> rpHome(Long id) {
        List<Inscription> inscriptions = inscriptionService.getAllByAnneActiveAndClasse(null,PageRequest.of(0,1000)).getContent();
        List<Classe> classes = classeService.getAll(PageRequest.of(0,1000)).getContent();
        List<Module> modules = moduleService.getAll(PageRequest.of(0,1000)).getContent();
        List<Professeur> professeurs = professeurService.getAll(PageRequest.of(0,1000)).getContent();
        RPHomeResponseDto dataDtos = RPHomeResponseDto.toDto(getFiveAbsents(),inscriptions.size(),classes.size(),modules.size(),professeurs.size());
        Map<Object, Object> response = RestResponse.response(dataDtos,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public  List<AbsenteisteResponseDto> getFiveAbsents(){
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        List<Etudiant> etudiants = etudiantService.getEtudiantWithAbsence();
        Map<Integer,Integer> donnees = new HashMap<>();
        for (Etudiant etu : etudiants){
            int nbreAbsence = absenceService.getAllByAnneeAndEtudiant(PageRequest.of(0,1000),anneeScolaire,etu).getContent().size();
            donnees.put(Integer.parseInt(etu.getId().toString()),nbreAbsence);
        }
        // Trier le Map par valeurs (nombre d'absences)
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(donnees.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int i = 0;
        List<AbsenteisteResponseDto> datas = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : list) {
            if (i == 5) {
                break;
            }
            Integer etudiantId = entry.getKey();
            Etudiant etudiant = etudiantService.show(Long.parseLong(etudiantId.toString()))
                    .orElseThrow(()->new EntityNotFoundException("Etudiant not found"));
            Inscription inscription = inscriptionService.getByAndAnneeScolaireAndEtudiant(anneeScolaire,etudiant)
                    .orElseThrow(()->new EntityNotFoundException("Inscription not found"));
            AbsenteisteResponseDto absenteiste = AbsenteisteResponseDto.toDto(inscription,entry.getValue());
            datas.add(absenteiste);
            i++;
        }
        return datas;
    }

}
