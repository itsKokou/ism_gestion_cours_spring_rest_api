package projet.restapi.web.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.core.data.entities.AnneeScolaire;
import projet.core.services.AnneeScolaireService;
import projet.restapi.web.controllers.AnneeScolaireRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.AnneeScolaireCreateRequestDto;
import projet.restapi.web.dtos.response.AnneeScolaireResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AnneeScolaireRestControllerImpl implements AnneeScolaireRestController {
    private final AnneeScolaireService anneeScolaireService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerAnneeScolaire(int page, int size) {
        Page<AnneeScolaire> anneeScolaires = anneeScolaireService.getAll(PageRequest.of(page,size));
        Page<AnneeScolaireResponseDto> dataDtos = anneeScolaires.map(AnneeScolaireResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(),HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showAnneeScolaire(Long id) {
        AnneeScolaire anneeScolaire = anneeScolaireService.show(id).orElse(null);
        Map<Object, Object> response;
        if (anneeScolaire == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(AnneeScolaireResponseDto.toDto(anneeScolaire),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateAnneeScolaire(AnneeScolaireCreateRequestDto annee, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            if (annee.isActive()){
                this.changeAnneeActiveInBD();
            }
            AnneeScolaire anneeScolaire = annee.toEntity();
            anneeScolaire.setId(id);
            anneeScolaireService.save(anneeScolaire);
            response= RestResponse.response(annee,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveAnneeScolaire(AnneeScolaireCreateRequestDto annee, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            if (annee.isActive()){
                this.changeAnneeActiveInBD();
            }
            anneeScolaireService.save(annee.toEntity());
            response= RestResponse.response(annee,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void changeAnneeActiveInBD(){
        AnneeScolaire anneeEncours =anneeScolaireService.getAnneeActuelle();
        anneeEncours.setIsActive(false);
        anneeScolaireService.save(anneeEncours);
    }
}
