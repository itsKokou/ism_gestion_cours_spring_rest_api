package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.ClasseCreateRequestDto;
import projet.restapi.web.dtos.request.ModuleCreateRequestDto;

import java.util.Map;


public interface ClasseRestController {
    @GetMapping("/classes")//End Point
    ResponseEntity<Map<Object, Object>>  listerClasse(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "0") Long niveau,
                          @RequestParam(defaultValue = "0") Long filiere,
                          @RequestParam(defaultValue = "true") Boolean planifier
                         );
    @GetMapping("/classes/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllClasse();

    @GetMapping("/classes/filter/module/{idModule}/professeur/{idProfesseur}/semestre/{idSemestre}")//End Point
    ResponseEntity<Map<Object, Object>>  getClasseByProfesseurAndModule(@PathVariable Long idModule,
                                                                        @PathVariable Long idProfesseur,
                                                                        @PathVariable Long idSemestre);

    @GetMapping("/classes/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showClasse(@PathVariable Long id);

    @PutMapping("/classes/{id}")
    ResponseEntity<Map<Object, Object>> updateClasse(@Valid @RequestBody ClasseCreateRequestDto classe,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/classes")
    ResponseEntity<Map<Object, Object>> saveClasse(@Valid @RequestBody ClasseCreateRequestDto classe, BindingResult bindingResult);

    @GetMapping("/classes/planifier/{id}")
    ResponseEntity<Map<Object, Object>>  PlanifierClasse(@PathVariable Long id);
}
