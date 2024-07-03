package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.EnseignementCreateRequestDto;
import projet.restapi.web.dtos.request.EtudiantCreateRequestDto;
import projet.restapi.web.dtos.request.ProfesseurCreateRequestDto;

import java.util.List;
import java.util.Map;


public interface ProfesseurRestController {
    @GetMapping("/professeurs")//End Point
    ResponseEntity<Map<Object, Object>>  listerProfesseur(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "0") Long module,
                          @RequestParam(required = false) String grade,
                          @RequestParam(required = false) String portable,
                          @RequestParam(defaultValue ="true") Boolean planifier
                         );

    @GetMapping("/professeurs/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllProfesseur();

    @GetMapping("/professeurs/filter/module/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  getProfesseurByModule(@PathVariable Long id);

    @GetMapping("/professeurs/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showProfesseur(@PathVariable Long id);

    @GetMapping("/professeurs/grades")//End Point
    ResponseEntity<Map<Object, Object>>  getAllGrades();

    @PutMapping("/professeurs/{id}")
    ResponseEntity<Map<Object, Object>> updateProfesseur(@Valid @RequestBody ProfesseurCreateRequestDto prof,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/professeurs")
    ResponseEntity<Map<Object, Object>> saveProfesseur(@Valid @RequestBody ProfesseurCreateRequestDto prof, BindingResult bindingResult);

    @PostMapping("/professeurs/affectation")
    ResponseEntity<Map<Object, Object>> makeAffectation(@Valid @RequestBody List<EnseignementCreateRequestDto> enseignements, BindingResult bindingResult);

    @GetMapping("/professeurs/classe/{id}")
    ResponseEntity<Map<Object, Object>> listerProfesseurClasse(@PathVariable Long id);

    @GetMapping("/professeurs/module/{id}")
    ResponseEntity<Map<Object, Object>> listerProfesseurModule( @PathVariable Long id);

    @GetMapping("/professeurs/planifier/{id}")
    ResponseEntity<Map<Object, Object>>  PlanifierProfesseur(@PathVariable Long id);

}
