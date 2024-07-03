package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.ProfesseurResponseDto;
import projet.core.data.entities.*;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.service.Disponibilite;
import projet.restapi.web.controllers.SeanceRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.SeanceCreateRequestDto;
import projet.restapi.web.dtos.response.SalleResponseDto;
import projet.restapi.web.dtos.response.SeanceResponseDto;
import projet.restapi.web.dtos.response.SeanceResponseDtoFlutter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SeanceRestControllerImpl implements SeanceRestController {
    private final ProfesseurService professeurService;
    private final ClasseService classeService;
    private final SeanceService seanceService;
    private final SalleService salleService;
    private final CoursService coursService;
    private final AnneeScolaireService anneeScolaireService;
    private final InscriptionService inscriptionService;
    private final EtudiantService etudiantService;
    private final PresenceService presenceService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerSeance(int page, int size, Long classe, Long professeur, int couleur) {
        Classe classeFilter = classeService.show(classe).orElse(null);
        Professeur profFilter = professeurService.show(professeur).orElse(null);
        List<Seance> datas = seanceService.getAllByClasseAndProfesseur(classeFilter, profFilter);
        List<SeanceResponseDto> datasDtos = datas.stream().map(SeanceResponseDto::toDto).toList();
        List<SeanceResponseDto> dataDtosFilter = new ArrayList<>();
        if (couleur==0){
            dataDtosFilter = datasDtos;
        }else{
            for (SeanceResponseDto seance : datasDtos){
                if (couleur==1 && seance.getColor().equals("#F53558")){
                    dataDtosFilter.add(seance);
                } else if (couleur==2 && seance.getColor().equals("#35F5C1")){
                    dataDtosFilter.add(seance);
                } else if (couleur==3 && seance.getColor().equals("#359EF5")){
                    dataDtosFilter.add(seance);
                }
            }
        }
        Map<Object, Object> model = RestResponse.response(dataDtosFilter,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> checkProfDisponibilite(Long idCours, String strDate, String strHeureD, String strHeureF) {
        boolean isDispo;
        Cours cours = coursService.show(idCours)
                .orElseThrow(() -> new EntityNotFoundException("Cours not found"));
        Professeur professeur = cours.getProfesseur();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            String[] listHD = strHeureD.split(":");
            String[] listHF = strHeureF.split(":");
            LocalTime heureD = LocalTime.of(Integer.parseInt(listHD[0]),Integer.parseInt(listHD[1]));
            LocalTime heureF = LocalTime.of(Integer.parseInt(listHF[0]),Integer.parseInt(listHF[1]));
            List<Seance> seances = seanceService.getAllByClasseAndProfesseur(null, professeur);
            isDispo = Disponibilite.isDisponible(seances,date,heureD,heureF);
        } catch (ParseException e) {
            isDispo = false;
            throw new RuntimeException(e);
        }
        Map<Object, Object> model = RestResponse.response(isDispo,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Map<Object, Object>> checkClasseDisponibilite(Long idCours, String strDate, String strHeureD, String strHeureF) {
        boolean isDispoFinal=true;
        Cours cours = coursService.show(idCours)
                .orElseThrow(() -> new EntityNotFoundException("Cours not found"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            String[] listHF = strHeureF.split(":");
            String[] listHD = strHeureD.split(":");
            LocalTime heureD = LocalTime.of(Integer.parseInt(listHD[0]),Integer.parseInt(listHD[1]));
            LocalTime heureF = LocalTime.of(Integer.parseInt(listHF[0]),Integer.parseInt(listHF[1]));

            List<Classe> classes = cours.getClasses();
            for (Classe classe : classes) {
                List<Seance> seances = seanceService.getAllByClasseAndProfesseur(classe,null);
                Boolean isDispo = Disponibilite.isDisponible(seances,date,heureD,heureF);
                isDispoFinal = isDispoFinal && isDispo;
            }
        } catch (ParseException e) {
            isDispoFinal = false;
            throw new RuntimeException(e);
        }

        Map<Object, Object> model = RestResponse.response(isDispoFinal,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> findProfesseurDisponible(Long idCours, String strDate, String strHeureD, String strHeureF) {
        Cours cours = coursService.show(idCours)
                .orElseThrow(() -> new EntityNotFoundException("Cours not found"));
        List<Professeur> professeurs = professeurService.getAllByModule(cours.getModule(), PageRequest.of(0,1000)).getContent();
        List<Professeur> professeursDisponibles = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            String[] listHD = strHeureD.split(":");
            String[] listHF = strHeureF.split(":");
            LocalTime heureD = LocalTime.of(Integer.parseInt(listHD[0]),Integer.parseInt(listHD[1]));
            LocalTime heureF = LocalTime.of(Integer.parseInt(listHF[0]),Integer.parseInt(listHF[1]));
            boolean isDispo;
            for (Professeur professeur : professeurs){
                List<Seance> seances = seanceService.getAllByClasseAndProfesseur(null, professeur);
                isDispo = Disponibilite.isDisponible(seances,date,heureD,heureF);
                if (isDispo){
                    professeursDisponibles.add(professeur);
                }
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<ProfesseurResponseDto> datas = professeursDisponibles.stream().map(ProfesseurResponseDto::toDto).toList();
        Map<Object, Object> model = RestResponse.response(datas,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> findSalleDisponible(Long idCours, String strDate, String strHeureD, String strHeureF) {
        Cours cours = coursService.show(idCours)
                .orElseThrow(() -> new EntityNotFoundException("Cours not found"));
        List<Classe> classes = cours.getClasses();
        int capacite = 0;
        for (Classe classe : classes) {
            capacite+= classe.getEffectif();
        }
        List<Salle> salles = salleService.getAll(PageRequest.of(0,1000)).getContent();
        List<Salle> salleDisponibles = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
            String[] listHD = strHeureD.split(":");
            String[] listHF = strHeureF.split(":");
            LocalTime heureD = LocalTime.of(Integer.parseInt(listHD[0]),Integer.parseInt(listHD[1]));
            LocalTime heureF = LocalTime.of(Integer.parseInt(listHF[0]),Integer.parseInt(listHF[1]));
            boolean isDispo;
            for (Salle salle : salles){
                List<Seance> seances = seanceService.getAllBySalle(salle);
                isDispo = Disponibilite.isDisponible(seances,date,heureD,heureF);
                if (isDispo && salle.getNbrePlace()>=capacite){
                    salleDisponibles.add(salle);
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<SalleResponseDto> datas = salleDisponibles.stream().map(SalleResponseDto::toDto).toList();
        Map<Object, Object> model = RestResponse.response(datas,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveSeance(SeanceCreateRequestDto seance, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Cours cours = coursService.show(seance.getIdCours())
                        .orElseThrow(()-> new EntityNotFoundException("Cours not found"));
                Professeur professeur = null;
                if(seance.getProfesseur()!=0){
                    professeur = professeurService.show(seance.getProfesseur())
                            .orElseThrow(()-> new EntityNotFoundException("Professeur not found"));
                }
                Salle salle = null;
                if(seance.getSalle()!=0){
                    salle = salleService.show(seance.getSalle())
                            .orElseThrow(()-> new EntityNotFoundException("Salle not found"));
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(seance.getDate());
                    String[] listHD = seance.getHeureD().split(":");
                    String[] listHF = seance.getHeureF().split(":");
                    LocalTime heureD = LocalTime.of(Integer.parseInt(listHD[0]),Integer.parseInt(listHD[1]));
                    LocalTime heureF = LocalTime.of(Integer.parseInt(listHF[0]),Integer.parseInt(listHF[1]));

                    Seance seanceCree = Seance.builder()
                            .date(date)
                            .heureD(heureD)
                            .heureF(heureF)
                            .codeSeance(seance.getCode())
                            .cours(cours)
                            .isAbsence(false)
                            .professeur(professeur)
                            .salle(salle)
                            .build();
                    seanceCree.setIsArchived(false);
                    //Mis à jour du cours
                    int nbreHeure = seanceCree.getHeureF().getHour() - seanceCree.getHeureD().getHour();
                    cours.setNbreHeurePlanifie(cours.getNbreHeurePlanifie()+nbreHeure);
                    cours.setNbreHeureRestantPlan(cours.getNbreHeureRestantPlan()-nbreHeure);

                    seanceService.save(seanceCree);
                    coursService.save(cours);
                    response= RestResponse.response(seance,HttpStatus.CREATED);
                } catch (ParseException e) {
                    response= RestResponse.response(seance,HttpStatus.NOT_FOUND);
                    throw new RuntimeException(e);
                }

            }catch (Exception e) {
                response= RestResponse.response(seance,HttpStatus.NOT_ACCEPTABLE);
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Flutter

    @Override
    public ResponseEntity<Map<Object, Object>> listerSeanceOfEtudiant(Long id) {
        Etudiant etudiant = etudiantService.show(id)
                .orElseThrow(()->new EntityNotFoundException("L'etudiant n'existe pas"));
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        Inscription inscription = inscriptionService.getByAndAnneeScolaireAndEtudiant(anneeScolaire,etudiant)
                .orElseThrow(()->new EntityNotFoundException("L'inscription de l'étudiant n'existe pas"));
        Classe classe = inscription.getClasse();
        List<Seance> datas = seanceService.getAllByClasseAndProfesseur(classe, null);
        List<SeanceResponseDtoFlutter> datasDtos = datas.stream().map(SeanceResponseDtoFlutter::toDto).toList();
        Map<Object, Object> model = RestResponse.response(datasDtos,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerSeanceByClasse(Long id) {
        Classe classe = classeService.show(id)
                .orElseThrow(()->new EntityNotFoundException("La classe n'existe pas"));
        List<Seance> datas = seanceService.getAllByClasseAndProfesseur(classe, null);
        List<SeanceResponseDtoFlutter> datasDtos = datas.stream().map(SeanceResponseDtoFlutter::toDto).toList();
        Map<Object, Object> model = RestResponse.response(datasDtos,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> SavePresenceOfEtudiant(Long seanceId, Long etuId) {
        Seance seance = seanceService.show(seanceId)
                .orElseThrow(()->new EntityNotFoundException("La séance n'existe pas"));
        Etudiant etudiant = etudiantService.show(etuId)
                .orElseThrow(()->new EntityNotFoundException("L'etudiant n'existe pas"));
        Optional<Presence> presenceExist = presenceService.getBySeanceAndEtudiant(seance,etudiant);
        if(presenceExist.isEmpty()){
            Presence presence = Presence.builder()
                    .etudiant(etudiant)
                    .seance(seance)
                    .build();
            presence.setIsArchived(false);
            presenceService.save(presence);
        }
        Map<Object, Object> model = RestResponse.response("Presence enregistrée",HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> IsEtudiantPresence(Long seanceId, Long etuId) {
        Seance seance = seanceService.show(seanceId)
                .orElseThrow(()->new EntityNotFoundException("La séance n'existe pas"));
        Etudiant etudiant = etudiantService.show(etuId)
                .orElseThrow(()->new EntityNotFoundException("L'etudiant n'existe pas"));
        Optional<Presence> presenceExist = presenceService.getBySeanceAndEtudiant(seance,etudiant);
        boolean isPresence = presenceExist.isPresent();
        Map<Object, Object> model = RestResponse.response(isPresence,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
