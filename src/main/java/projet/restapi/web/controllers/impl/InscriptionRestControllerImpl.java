package projet.restapi.web.controllers.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.entities.*;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.AnneeScolaireService;
import projet.core.services.ClasseService;
import projet.core.services.EtudiantService;
import projet.core.services.InscriptionService;
import projet.restapi.security.services.SecurityService;
import projet.restapi.web.controllers.InscriptionRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.InscriptionCreateRequestDto;
import projet.restapi.web.dtos.request.ReinscriptionCreateRequestDto;
import projet.restapi.web.dtos.response.InscriptionResponseDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class InscriptionRestControllerImpl implements InscriptionRestController {
    private final InscriptionService inscriptionService;
    private final ClasseService classeService;
    private final AnneeScolaireService anneeScolaireService;
    private final EtudiantService etudiantService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerInscription(int page, int size,Long annee, Long classe, String date) {
        AnneeScolaire anneeScolaire = anneeScolaireService.show(annee)
                .orElseThrow(() -> new EntityNotFoundException("AnneeScolaire not found"));
        Classe classeFilter = classeService.show(classe).orElse(null);
        Date dateF = null;
        if (!date.equals("")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateF = dateFormat.parse(date);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        Page<Inscription> datas = inscriptionService.getAllByAnneeAndClasseAndDate(anneeScolaire,classeFilter,dateF,PageRequest.of(page,size));
        Page<InscriptionResponseDto> dataDtos = datas.map(InscriptionResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateInscription(String msg, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Inscription inscription = inscriptionService.show(id)
                    .orElseThrow(()->new EntityNotFoundException("Inscription non trouvé"));
            inscription.setIsArchived(true);
            inscriptionService.save(inscription);
            response= RestResponse.response(msg,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String generateMatricule(List<Etudiant> etudiants) {
        // Calculer la position de l'étudiant dans la liste
        int posEtudiant = etudiants.size() + 1;
        // Définir le nombre de caractères
        int nbreCaracteres = 3;

        // Convertir la position de l'étudiant en chaîne de caractères
        String posStr = String.valueOf(posEtudiant);

        // Calculer le nombre de zéros à ajouter
        int nbreZeros = Math.max(0, nbreCaracteres - posStr.length());

        return ("MAT" + "0".repeat(nbreZeros) + posStr);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveInscription(InscriptionCreateRequestDto inscription, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                String matricule =  this.generateMatricule(etudiantService.getAll(PageRequest.of(0,1000)).getContent());
                AppRole role = securityService.getRoleByName("ROLE_ETUDIANT");
                Classe classe = classeService.show(inscription.getClasse())
                        .orElseThrow(()->new EntityNotFoundException("Classe not found"));
                AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
                Etudiant etudiant = new Etudiant();
                etudiant.setMatricule(matricule);
                etudiant.setNomComplet(inscription.getNomComplet());
                etudiant.setLogin(inscription.getEmail());
                etudiant.setPassword(passwordEncoder.encode(inscription.getPassword()));
                etudiant.setTuteur(inscription.getTuteur());
                etudiant.setIsArchived(false);
                etudiant.setRoles(new ArrayList<>());
                etudiant.getRoles().add(role);
                etudiant.setPhoto(inscription.getPhoto());

                Inscription inscriptionCree = new Inscription();
                inscriptionCree.setCreatedAt(new Date());
                inscriptionCree.setIsArchived(false);
                inscriptionCree.setAnneeScolaire(anneeScolaire);
                inscriptionCree.setClasse(classe);
                inscriptionCree.setEtudiant(etudiant);
                inscriptionService.save(inscriptionCree);
                response= RestResponse.response(inscription,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(inscription,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> makeReinscription(ReinscriptionCreateRequestDto reinscription, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
                Classe classe = classeService.show(reinscription.getClasse())
                        .orElseThrow(()->new EntityNotFoundException("Classe not found"));
                Etudiant etudiant = etudiantService.show(reinscription.getId())
                        .orElseThrow(()->new EntityNotFoundException("Etudiant not found"));
                Inscription ins = inscriptionService.getByAndAnneeScolaireAndEtudiant(anneeScolaire,etudiant).orElse(null);
                if (ins==null){
                    etudiant.setLogin(reinscription.getEmail());
                    etudiant.setNomComplet(reinscription.getNomComplet());
                    etudiant.setPassword(passwordEncoder.encode(reinscription.getPassword()));
                    etudiant.setTuteur(reinscription.getTuteur());

                    Inscription reins = new Inscription();
                    reins.setCreatedAt(new Date());
                    reins.setIsArchived(false);
                    reins.setAnneeScolaire(anneeScolaire);
                    reins.setClasse(classe);
                    reins.setEtudiant(etudiant);
                    inscriptionService.save(reins);
                    response= RestResponse.response(reinscription,HttpStatus.CREATED);
                }else {
                    response= RestResponse.response(reinscription,HttpStatus.CONFLICT);
                }
            }catch (Exception e) {
                response= RestResponse.response(reinscription,HttpStatus.NOT_ACCEPTABLE);
                System.out.println(e.getMessage());
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
