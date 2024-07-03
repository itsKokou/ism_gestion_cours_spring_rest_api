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
import projet.core.data.dto.ModuleResponseDto;
import projet.core.data.entities.AnneeScolaire;
import projet.core.data.entities.Module;
import projet.core.data.entities.Professeur;
import projet.core.data.entities.Salle;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.SalleService;
import projet.restapi.web.controllers.SalleRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.AnneeScolaireCreateRequestDto;
import projet.restapi.web.dtos.request.SalleCreateRequestDto;
import projet.restapi.web.dtos.response.AnneeScolaireResponseDto;
import projet.restapi.web.dtos.response.SalleResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SalleRestControllerImpl implements SalleRestController {
    private final SalleService salleService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerSalle(int page, int size, Boolean planifier) {
        Page<Salle> datas = salleService.getAllByPlanned(planifier,PageRequest.of(page,size));
        Page<SalleResponseDto> dataDtos = datas.map(SalleResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllSalle() {
        Page<Salle> datas = salleService.getAll(PageRequest.of(0,1000));
        Page<SalleResponseDto> dataDtos = datas.map(SalleResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showSalle(Long id) {
        Salle salle = salleService.show(id).orElse(null);
        Map<Object, Object> response;
        if (salle == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(SalleResponseDto.toDto(salle),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateSalle(SalleCreateRequestDto salle, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Salle salleCree = salle.toEntity();
            salleCree.setId(id);
            salleService.save(salleCree);
            response= RestResponse.response(salle,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveSalle(SalleCreateRequestDto salle, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Salle salleCree = salle.toEntity();
                salleService.save(salleCree);
                response= RestResponse.response(salle,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(salle,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> PlanifierSalle(Long id) {
        Salle salle = salleService.show(id)
                .orElseThrow(()->new EntityNotFoundException("Salle n'existe pas"));
        salle.setIsPlanned(true);
        salleService.save(salle);
        Map<Object, Object> response = RestResponse.response("Planifié avec succes",HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
