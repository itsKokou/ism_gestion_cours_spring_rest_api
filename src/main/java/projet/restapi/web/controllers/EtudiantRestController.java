package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.ClasseCreateRequestDto;
import projet.restapi.web.dtos.request.EtudiantCreateRequestDto;

import java.util.Map;


public interface EtudiantRestController {
    @GetMapping("/etudiants")//End Point
    ResponseEntity<Map<Object, Object>>  listerEtudiant(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "0") Long annee,
                          @RequestParam(defaultValue = "0") Long classe
                         );
    @GetMapping("/etudiants/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showEtudiant(@PathVariable Long id);

    @GetMapping("/etudiants/matricule/{mat}")//End Point
    ResponseEntity<Map<Object, Object>>  showEtudiantByMat(@PathVariable String mat);

    @PutMapping("/etudiants/{id}")
    ResponseEntity<Map<Object, Object>> updateEtudiant(@Valid @RequestBody EtudiantCreateRequestDto etudiant,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @GetMapping("/etudiants/absence/{id}/annee/{annee}")
    ResponseEntity<Map<Object, Object>> listerEtudiantAbsence(@PathVariable Long id,@PathVariable Long annee);

    @GetMapping("/etudiants/dossier/{id}/annee/{annee}")
    ResponseEntity<Map<Object, Object>> listerEtudiantDossier( @PathVariable Long id,@PathVariable Long annee);

    @GetMapping("/etudiants/seance/{seanceId}")
    ResponseEntity<Map<Object, Object>> listerSeanceEtudiant(Model model, @PathVariable Long seanceId);

}
