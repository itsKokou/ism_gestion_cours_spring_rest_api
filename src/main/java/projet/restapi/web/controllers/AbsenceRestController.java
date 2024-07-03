package projet.restapi.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projet.restapi.web.dtos.request.ClasseCreateRequestDto;

import java.util.Map;


public interface AbsenceRestController {
    @GetMapping("/absences")//End Point
    ResponseEntity<Map<Object, Object>>  listerAbsence(
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "") String matricule
                         );
    }
