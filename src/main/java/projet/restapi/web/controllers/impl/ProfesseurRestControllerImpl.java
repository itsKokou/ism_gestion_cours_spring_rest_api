package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.ModuleResponseDto;
import projet.core.data.dto.ProfesseurResponseDto;
import projet.core.data.entities.*;
import projet.core.data.entities.Module;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.AnneeScolaireService;
import projet.core.services.ClasseService;
import projet.core.services.ModuleService;
import projet.core.services.ProfesseurService;
import projet.restapi.security.services.SecurityService;
import projet.restapi.web.controllers.ProfesseurRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.EnseignementCreateRequestDto;
import projet.restapi.web.dtos.request.ProfesseurCreateRequestDto;
import projet.restapi.web.dtos.response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProfesseurRestControllerImpl implements ProfesseurRestController {
    private final ProfesseurService professeurService;
    private final ModuleService moduleService;
    private final ClasseService classeService;
    private final AnneeScolaireService anneeScolaireService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public Page<Professeur> createPage(List<Professeur> professeurs, int page, int size) {
        // Créer un objet Pageable pour la pagination
        PageRequest pageRequest = PageRequest.of(page, size);

        // Calculer l'index de départ
        int start = (int) pageRequest.getOffset();
        // Calculer l'index de fin
        int end = Math.min((start + pageRequest.getPageSize()), professeurs.size());

        // Créer une sous-liste de professeurs pour la page demandée
        List<Professeur> sublist = professeurs.subList(start, end);

        // Créer une instance de PageImpl à partir de la sous-liste et du nombre total d'éléments
        return new PageImpl<>(sublist, pageRequest, professeurs.size());
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerProfesseur(int page, int size, Long module, String grade, String portable,Boolean planifier) {
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        Module moduleSearch = moduleService.show(module).orElse(null);
        if (grade.isEmpty()){grade=null;}
        if (portable.isEmpty()){portable=null;}
        Page<Professeur> professeurs = professeurService.getAllByGradeAndPortableAndPlanned(grade,portable ,planifier, PageRequest.of(0,1000));
        List<Professeur> professeurList = new ArrayList<>();
        if (moduleSearch != null) {
            for (Professeur professeur : professeurs.getContent()) {
                List<Module> modules = professeurService.getModuleByProfesseur(professeur,PageRequest.of(0,100)).getContent();
                if (modules.contains(moduleSearch)){
                    professeurList.add(professeur);
                }
            }
        }else{
            professeurList = professeurs.getContent();
        }
        Page<Professeur> professeurPage = createPage(professeurList,page,size);
        Page<ProfesseurResponseDto> dataDtos = professeurPage.map(ProfesseurResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllProfesseur() {
        Page<Professeur> professeurs = professeurService.getAll(PageRequest.of(0,1000));
        Page<ProfesseurResponseDto> dataDtos = professeurs.map(ProfesseurResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getProfesseurByModule(Long id) {
        Module module = moduleService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Module non trouvé"));
        Page<Professeur> professeurs = professeurService.getAllByModule(module,PageRequest.of(0,1000));
        Page<ProfesseurResponseDto> dataDtos = professeurs.map(ProfesseurResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showProfesseur(Long id) {
        Professeur professeur = professeurService.show(id).orElse(null);
        Map<Object, Object> response;
        if (professeur == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(ProfesseurResponseDto.toDto(professeur),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllGrades() {
        List<String> grades = professeurService.getAllGrades();
        Map<Object, Object> model = RestResponse.response(grades,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateProfesseur(ProfesseurCreateRequestDto prof, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Professeur professeur = professeurService.show(id)
                    .orElseThrow(()->new EntityNotFoundException("Professeur non trouvé !"));
            Professeur profCree = prof.toEntity();
            profCree.setId(id);
            profCree.setRoles(professeur.getRoles());
            profCree.setPassword(passwordEncoder.encode(prof.getPassword()));
            professeurService.save(profCree);
            response= RestResponse.response(prof,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveProfesseur(ProfesseurCreateRequestDto prof, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Professeur professeur = prof.toEntity();
                professeur.setPassword(passwordEncoder.encode(prof.getPassword()));
                AppRole profRole = securityService.getRoleByName("ROLE_PROFESSEUR");
                professeur.getRoles().add(profRole);
                professeurService.save(professeur);
                response= RestResponse.response(prof,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(prof,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> makeAffectation(List<EnseignementCreateRequestDto> enseignements, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
                for (EnseignementCreateRequestDto enseignement : enseignements) {
                    Professeur prof = professeurService.show(enseignement.getProfesseur().getId())
                            .orElseThrow(()->new EntityNotFoundException(("Professeur  n'existe pas")));
                    Classe classe = classeService.show(enseignement.getClasse().getId())
                            .orElseThrow(()-> new EntityNotFoundException("Classe n'existe pas"));
                    Enseignement enseignementCree = new Enseignement();
                    enseignementCree.setAnneeScolaire(anneeScolaire);
                    enseignementCree.setProfesseur(prof);
                    enseignementCree.setClasse(classe);
                    enseignementCree.setModules(new ArrayList<>());//initialiser la liste
                    for(ModuleResponseDto mod : enseignement.getModules()){
                        Module module = moduleService.show(mod.getId())
                                .orElseThrow(()->new EntityNotFoundException("Module n'existe pas"));
                        enseignementCree.getModules().add(module);
                    }
                    //Verifier si cet enseignement n'exite pas déjà pour kuste ajouter les modules non inclus
                    Enseignement enseignementExist = professeurService.getByClasseAndProfesseurAndAnneeScolaireActuelle(classe,prof);
                    if(enseignementExist == null){
                        //Si aucun trouver, on save
                        professeurService.saveEnseignement(enseignementCree);
                    }else {
                        //sinon on modifie les modules de l'ancien
                        for (Module mod : enseignementCree.getModules()){
                            if(!enseignementExist.getModules().contains(mod)){
                                enseignementExist.getModules().add(mod);
                            }
                        }
                        professeurService.saveEnseignement(enseignementExist);
                    }

                }
                response= RestResponse.response(enseignements,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(enseignements,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerProfesseurClasse(Long id) {
        Professeur professeur = professeurService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Professeur n'existe pas"));
        Page<Classe> classes = professeurService.getClasseByProfesseur(professeur,PageRequest.of(0,1000));
        Page<ClasseResponseDto> classesDto = classes.map(ClasseResponseDto::toDto);
        DetailClasseResponseDto detail = DetailClasseResponseDto.toDto(classesDto.getContent(),professeur.getNomComplet());
        Map<Object, Object> response = RestResponse.response(detail,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerProfesseurModule(Long id) {
        Professeur professeur = professeurService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Professeur n'existe pas"));
        Page<Module> modules = professeurService.getModuleByProfesseur(professeur,PageRequest.of(0,1000));
        Page<ModuleResponseDto> modulesDto = modules.map(ModuleResponseDto::toDto);
        DetailModuleResponseDto detail = DetailModuleResponseDto.toDto(modulesDto.getContent(),professeur.getNomComplet());
        Map<Object, Object> response = RestResponse.response(detail,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> PlanifierProfesseur(Long id) {
        Professeur professeur = professeurService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Professeur n'existe pas"));
        professeur.setIsPlanned(true);
        professeurService.save(professeur);
        Map<Object, Object> response = RestResponse.response("Planifié avec succes",HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
