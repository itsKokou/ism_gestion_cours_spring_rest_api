package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.dto.AbsenceResponseDto;
import projet.core.data.entities.Absence;
import projet.core.data.entities.AnneeScolaire;
import projet.core.data.entities.Etudiant;
import projet.core.services.*;
import projet.restapi.web.controllers.AbsenceRestController;
import projet.restapi.web.dtos.RestResponse;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AbsenceRestControllerImpl implements AbsenceRestController {
    private final AbsenceService absenceService;
    private final EtudiantService etudiantService;
    private final AnneeScolaireService anneeScolaireService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerAbsence(int page, int size, String matricule) {
        Page<Absence> absences;
        Optional<Etudiant> etudiantActuelle = etudiantService.getEtudiantByMatricule(matricule);
        AnneeScolaire anneeScolaire = anneeScolaireService.getAnneeActuelle();
        if (etudiantActuelle.isPresent()){
            absences = absenceService.getAllByAnneeAndEtudiant(PageRequest.of(page,size),anneeScolaire,etudiantActuelle.get());
        }else {
            absences = absenceService.getAllByAnnee(PageRequest.of(page,size),anneeScolaire);
        }
        Page<AbsenceResponseDto> dataDtos = absences.map(AbsenceResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
