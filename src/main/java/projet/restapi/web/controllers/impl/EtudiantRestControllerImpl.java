package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.AbsenceResponseDto;
import projet.core.data.dto.InscriptionEtudiantResponseDto;
import projet.core.data.entities.*;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.*;
import projet.restapi.web.controllers.EtudiantRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.EtudiantCreateRequestDto;
import projet.restapi.web.dtos.response.DetailAbsenceResponseDto;
import projet.restapi.web.dtos.response.DetailDossierResponseDto;
import projet.restapi.web.dtos.response.EtudiantResponseDto;
import projet.restapi.web.dtos.response.InscriptionResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class EtudiantRestControllerImpl implements EtudiantRestController {
    private final EtudiantService etudiantService;
    private  final InscriptionService inscriptionService;
    private final AnneeScolaireService anneeScolaireService;
    private final ClasseService classeService;
    private final PasswordEncoder passwordEncoder;
    private final AbsenceService absenceService;
    private final SeanceService seanceService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerEtudiant(int page, int size, Long annee, Long classe) {
        AnneeScolaire anneeScolaire = anneeScolaireService.show(annee)
                .orElseThrow(()->new EntityNotFoundException("Annee des inscriptions n'existe pas"));
        Classe classeEt = classeService.show(classe).orElse(null);
        Page<Inscription> datas = inscriptionService.getAllByAnneAndClasse(anneeScolaire,classeEt,PageRequest.of(page,size));
        Page<InscriptionEtudiantResponseDto> dataDtos = datas.map(InscriptionEtudiantResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showEtudiant(Long id) {
        Etudiant etudiant = etudiantService.show(id).orElse(null);
        Map<Object, Object> response;
        if (etudiant == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(EtudiantResponseDto.toDto(etudiant),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showEtudiantByMat(String mat) {
        Etudiant etudiant = etudiantService.getEtudiantByMatricule(mat).orElse(null);
        Map<Object, Object> response;
        if (etudiant == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(EtudiantResponseDto.toDto(etudiant),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateEtudiant(EtudiantCreateRequestDto etudiant, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Etudiant etu = etudiantService.show(id)
                    .orElseThrow(()->new EntityNotFoundException("Etudiant non trouvé !"));
            Etudiant etudiantCree = etudiant.toEntity();
            etudiantCree.setId(id);
            etudiantCree.setPhoto(etu.getPhoto());
            etudiantCree.setPassword(passwordEncoder.encode(etudiant.getPassword()));
            etudiantService.save(etudiantCree);
            response= RestResponse.response(etudiant,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerEtudiantAbsence(Long id,Long annee) {
        Etudiant etudiant = etudiantService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Etudiant n'existe pas"));
        AnneeScolaire anneeScolaire = anneeScolaireService.show(annee)
                .orElseThrow(()->new EntityNotFoundException("Année scolaire n'existe pas"));;
        Page<Absence> absences = absenceService.getAllByAnneeAndEtudiant(PageRequest.of(0,1000),anneeScolaire,etudiant);
        Page<AbsenceResponseDto> absencesDtos = absences.map(AbsenceResponseDto::toDto);
        DetailAbsenceResponseDto detail = DetailAbsenceResponseDto.toDto(absencesDtos.getContent(),etudiant.getNomComplet());
        Map<Object, Object> response = RestResponse.response(detail,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerEtudiantDossier(Long id,Long annee) {
        Etudiant etudiant = etudiantService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Etudiant n'existe pas"));
        AnneeScolaire anneeScolaire = anneeScolaireService.show(annee)
                .orElseThrow(()->new EntityNotFoundException("Année scolaire n'existe pas"));
        Inscription inscription = inscriptionService.getByAndAnneeScolaireAndEtudiant(anneeScolaire,etudiant)
                .orElseThrow(()->new EntityNotFoundException("Inscrption n'existe pas"));;
        List<InscriptionResponseDto> inscriptions = etudiant.getInscriptions().stream().map(InscriptionResponseDto::toDto).toList();
        DetailDossierResponseDto detail = DetailDossierResponseDto.toDto(inscriptions,EtudiantResponseDto.toDto(etudiant),inscription.getClasse().getLibelle());
        Map<Object, Object> response = RestResponse.response(detail,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> listerSeanceEtudiant(Model model, Long seanceId) {
        Seance seance = seanceService.show(seanceId)
                .orElseThrow(()->new EntityNotFoundException("Seance n'existe pas"));
        Page<Inscription> lesEtudiantsBySeance = seanceService.getLesEtudiantsBySeance(seance, PageRequest.of(0, 1000));
        List<InscriptionEtudiantResponseDto> inscriptions = lesEtudiantsBySeance.map(InscriptionEtudiantResponseDto::toDto).toList();
        Map<Object, Object> response = RestResponse.response(inscriptions,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
