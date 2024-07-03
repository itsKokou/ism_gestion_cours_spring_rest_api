package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.ModuleCreateRequestDto;
import projet.restapi.web.dtos.request.SalleCreateRequestDto;

import java.util.Map;


public interface SalleRestController {
    @GetMapping("/salles")//End Point
    ResponseEntity<Map<Object, Object>>  listerSalle(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "true") Boolean planifier
                         );

    @GetMapping("/salles/all")//End Point
    ResponseEntity<Map<Object, Object>>  getAllSalle();

    @GetMapping("/salles/{id}")//End Point
    ResponseEntity<Map<Object, Object>>  showSalle(@PathVariable Long id);

    @PutMapping("/salles/{id}")
    ResponseEntity<Map<Object, Object>> updateSalle(@Valid @RequestBody SalleCreateRequestDto salle,
                                                     @PathVariable Long id, BindingResult bindingResult);

    @PostMapping("/salles")
    ResponseEntity<Map<Object, Object>> saveSalle(@Valid @RequestBody SalleCreateRequestDto salle, BindingResult bindingResult);

    @GetMapping("/salles/planifier/{id}")
    ResponseEntity<Map<Object, Object>>  PlanifierSalle(@PathVariable Long id);
}
