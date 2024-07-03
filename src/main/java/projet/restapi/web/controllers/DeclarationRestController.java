package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.core.data.enums.EtatDeclaration;
import projet.restapi.web.dtos.request.ClasseCreateRequestDto;
import projet.restapi.web.dtos.request.DeclarationCreateRequestDto;
import projet.restapi.web.dtos.request.SalleCreateRequestDto;

import java.util.Map;


public interface DeclarationRestController {
    @GetMapping("/declarations")//End Point
    ResponseEntity<Map<Object, Object>>  listerDeclaration(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "Enattente") EtatDeclaration etat
                         );


    @GetMapping("/declarations/{id}/professeur/{userId}/seance/{seanceId}/{action}")
    ResponseEntity<Map<Object, Object>>  traiterDeclaration(
                                            @PathVariable Long id,
                                            @PathVariable Long userId,
                                            @PathVariable Long seanceId,
                                            @PathVariable String action);

    @PostMapping("/declarations")
    ResponseEntity<Map<Object, Object>> saveDeclaration(@Valid @RequestBody DeclarationCreateRequestDto declaration, BindingResult bindingResult);

    @GetMapping("/declarations/{seanceId}/{etuId}/check")
    ResponseEntity<Map<Object, Object>> IsEtudiantDeclaration(@PathVariable Long seanceId, @PathVariable Long etuId);
    }
