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
import projet.core.data.dto.AbsenceResponseDto;
import projet.core.data.dto.DeclarationResponseDto;
import projet.core.data.entities.*;
import projet.core.data.enums.EtatDeclaration;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.security.services.SecurityService;
import projet.restapi.web.controllers.AbsenceRestController;
import projet.restapi.web.controllers.DeclarationRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.DeclarationCreateRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class DeclarationRestControllerImpl implements DeclarationRestController {
    private final DeclarationService declarationService;
    private final SeanceService seanceService;
    private final AnneeScolaireService anneeScolaireService;
    private final CoursService coursService;
    private final SecurityService securityService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerDeclaration(int page, int size, EtatDeclaration etat) {
        Page<Declaration> declarations = declarationService.getDeclarationsByEtatAndUserRole(etat,"ROLE_PROFESSEUR",PageRequest.of(page,size));
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        Page<DeclarationResponseDto> dataDtos = declarations.map(DeclarationResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> traiterDeclaration(Long id, Long userId, Long seanceId, String action) {
        Declaration declaration = declarationService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Declaration non trouvée !"));
        if (action.equals("refuser")){
            declaration.setEtat(EtatDeclaration.Refuse);
        }else {
            declaration.setEtat(EtatDeclaration.Accepte);
            Seance seance = seanceService.show(seanceId)
                    .orElseThrow(() -> new EntityNotFoundException("Seance non trouvée !"));
            //Annuler la seance
            if (!seance.getIsArchived()){
                Cours cours = seance.getCours();
                int nbreHeure = seance.getHeureF().getHour() - seance.getHeureD().getHour();
                cours.setNbreHeurePlanifie(cours.getNbreHeurePlanifie() - nbreHeure);
                cours.setNbreHeureRestantPlan(cours.getNbreHeureRestantPlan() + nbreHeure);
                seance.setIsArchived(true);
                seanceService.save(seance);
                coursService.save(cours);
            }
        }
        return new ResponseEntity<>(RestResponse.response("OKAY",HttpStatus.OK), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveDeclaration(DeclarationCreateRequestDto declaration, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                AppUser user = securityService.getUserById(declaration.getUserId())
                        .orElseThrow(()->new EntityNotFoundException("L'utilisateur n'existe pas"));
                Seance seance = seanceService.show(declaration.getSeanceId())
                        .orElseThrow(()->new EntityNotFoundException("La séance n'existe pas "));
                Declaration declarationCree = declaration.toEntity();
                declarationCree.setEtat(EtatDeclaration.Enattente);
                declarationCree.setUser(user);
                declarationCree.setSeance(seance);

                Declaration declarationExist = declarationService.getDeclarationsByUserAndSeance(user,seance);
                if (declarationExist != null){
                    response= RestResponse.response(declaration,HttpStatus.CONFLICT);
                }else {
                    declarationService.save(declarationCree);
                    response= RestResponse.response(declaration,HttpStatus.CREATED);
                }

            }catch (Exception e) {
                response= RestResponse.response(declaration,HttpStatus.NOT_ACCEPTABLE);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> IsEtudiantDeclaration(Long seanceId, Long etuId) {
        Seance seance = seanceService.show(seanceId)
                .orElseThrow(()->new EntityNotFoundException("La séance n'existe pas"));
        AppUser user = securityService.getUserById(etuId)
                .orElseThrow(()->new EntityNotFoundException("L'étudiant n'existe pas"));
        Declaration declarationExist = declarationService.getDeclarationsByUserAndSeance(user,seance);

        boolean isDeclaration = declarationExist!=null;
        Map<Object, Object> model = RestResponse.response(isDeclaration,HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
