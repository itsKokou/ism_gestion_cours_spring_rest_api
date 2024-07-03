package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.entities.*;
import projet.core.data.entities.Module;
import projet.core.data.enums.EtatCours;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.web.controllers.CoursRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.CoursCreateRequestDto;
import projet.restapi.web.dtos.response.CoursResponseDto;

import java.sql.SQLOutput;
import java.util.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CoursRestControllerImpl implements CoursRestController {
    private final CoursService coursService;
    private final ClasseService classeService;
    private final SemestreService semestreService;
    private final AnneeScolaireService anneeScolaireService;
    private final ModuleService moduleService;
    private final ProfesseurService professeurService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerCours(int page, int size, EtatCours etat, Long classe, Long semestre,Long professeur) {
        Classe classeFilter = classeService.show(classe).orElse(null);
        Semestre semestreFilter = semestreService.show(semestre).orElse(null);
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        Professeur prof = professeurService.show(professeur).orElse(null);
        Page<Cours> datas = coursService.getAllByAnneeAndEtatAndSemestreAndClasseAndProf(anneeScolaire,etat,semestreFilter,classeFilter,prof,PageRequest.of(page,size));
        Page<CoursResponseDto> dataDtos = datas.map(CoursResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showCours(Long id) {
        Cours cours = coursService.show(id).orElse(null);
        Map<Object, Object> response;
        if (cours == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(CoursResponseDto.toDto(cours),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateCours(String msg, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            //Update Cours
            Cours coursUpdate = coursService.show(id)
                    .orElseThrow(()->new EntityNotFoundException("Cours not found"));

            coursUpdate.setIsArchived(true);
            coursService.save(coursUpdate);
            response= RestResponse.response(msg,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveCours(CoursCreateRequestDto cours, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                //Creer le cours
                AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
                Module module = moduleService.show(cours.getModule())
                        .orElseThrow(()->new EntityNotFoundException("Module not found"));
                Professeur prof = professeurService.show(cours.getProfesseur())
                        .orElseThrow(()->new EntityNotFoundException("Professeur not found"));
                Semestre semestre = semestreService.show(cours.getSemestre())
                        .orElseThrow(()->new EntityNotFoundException("Semestre not found"));

                Cours coursCree = new Cours();
                coursCree.setCreatedAt(new Date());
                coursCree.setAnneeScolaire(anneeScolaire);
                coursCree.setIsArchived(false);
                coursCree.setModule(module);
                coursCree.setProfesseur(prof);
                coursCree.setSemestre(semestre);
                coursCree.setNbreHeureTotal(cours.getNbreHeureTotal());
                coursCree.setNbreHeurePlanifie(0);
                coursCree.setNbreHeureRestantPlan(cours.getNbreHeureTotal());
                coursCree.setNbreHeureRealise(0);
                coursCree.setEtat(EtatCours.Encours);
                coursCree.setClasses(new ArrayList<>());
                for (Long id : cours.getClasses()){
                    Classe classe = classeService.show(id)
                            .orElseThrow(()->new EntityNotFoundException("Classe not found"));
                    coursCree.getClasses().add(classe);
                }
                coursService.save(coursCree);
                System.out.println(coursCree.getClasses());
                response= RestResponse.response(cours,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(cours,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
