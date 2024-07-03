package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.SalleCreateRequestDto;
import projet.restapi.web.dtos.request.SeanceCreateRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;


public interface SeanceRestController {
    @GetMapping("/seances")//End Point
    ResponseEntity<Map<Object, Object>>  listerSeance(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "0") Long classe,
                          @RequestParam(defaultValue = "0") Long professeur,
                          @RequestParam(defaultValue = "0") int couleur
                         );

    @GetMapping("/seances/professeur/disponibilite/{idCours}/{strDate}/{strHeureD}/{strHeureF}")
    ResponseEntity<Map<Object, Object>> checkProfDisponibilite(@PathVariable Long idCours,
                                                               @PathVariable String strDate,
                                                               @PathVariable String strHeureD,
                                                               @PathVariable String strHeureF
                                                               );

    @GetMapping("/seances/professeur/disponible/{idCours}/{strDate}/{strHeureD}/{strHeureF}")
    ResponseEntity<Map<Object, Object>> findProfesseurDisponible(@PathVariable Long idCours,
                                                               @PathVariable String strDate,
                                                               @PathVariable String strHeureD,
                                                               @PathVariable String strHeureF
                                                               );

    @GetMapping("/seances/salle/disponible/{idCours}/{strDate}/{strHeureD}/{strHeureF}")
    ResponseEntity<Map<Object, Object>> findSalleDisponible(@PathVariable Long idCours,
                                                               @PathVariable String strDate,
                                                               @PathVariable String strHeureD,
                                                               @PathVariable String strHeureF
                                                               );

    @GetMapping("/seances/classe/disponibilite/{idCours}/{strDate}/{strHeureD}/{strHeureF}")
    ResponseEntity<Map<Object, Object>> checkClasseDisponibilite(@PathVariable Long idCours,
                                                               @PathVariable String strDate,
                                                               @PathVariable String strHeureD,
                                                               @PathVariable String strHeureF
    );

    @PostMapping("/seances")
    ResponseEntity<Map<Object, Object>> saveSeance(@Valid @RequestBody SeanceCreateRequestDto seance, BindingResult bindingResult);

    //------------------------- FLUTTER ----------------------------------

    @GetMapping("seances/etudiant/{id}")
    ResponseEntity<Map<Object, Object>> listerSeanceOfEtudiant(@PathVariable Long id);

    @GetMapping("seances/classe/{id}")
    ResponseEntity<Map<Object, Object>> listerSeanceByClasse(@PathVariable Long id);

    @GetMapping("seances/{seanceId}/presence/{etuId}")
    ResponseEntity<Map<Object, Object>> SavePresenceOfEtudiant(@PathVariable Long seanceId, @PathVariable Long etuId);

    @GetMapping("seances/{seanceId}/presence/{etuId}/check")
    ResponseEntity<Map<Object, Object>> IsEtudiantPresence(@PathVariable Long seanceId, @PathVariable Long etuId);
}
