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
import projet.core.data.dto.SemestreResponseDto;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.NiveauService;
import projet.core.services.SemestreService;
import projet.restapi.web.controllers.SemestreRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.SemestreCreateRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class SemestreRestControllerImpl implements SemestreRestController {
    private final SemestreService semestreService;
    private final NiveauService niveauService;

    @Override
    public ResponseEntity<Map<Object, Object>> listerSemestre(int page, int size) {
        Page<Semestre> datas = semestreService.getAll(PageRequest.of(page,size));
        Page<SemestreResponseDto> dataDtos = datas.map(SemestreResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllSemestre() {
        Page<Semestre> datas = semestreService.getAll(PageRequest.of(0,1000));
        Page<SemestreResponseDto> dataDtos = datas.map(SemestreResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showSemestre(Long id) {
        Semestre semestre = semestreService.show(id).orElse(null);
        Map<Object, Object> response;
        if (semestre == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(SemestreResponseDto.toDto(semestre),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateSemestre(SemestreCreateRequestDto semestre, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Niveau niveau = niveauService.show(semestre.getNiveau()).orElseThrow(()-> new EntityNotFoundException("Niveau not found"));
            if (semestre.getIsActive()){
                this.changeSemestreActiveInBD(niveau);
            }
            Semestre semestreCree = semestre.toEntity(niveau);
            semestreCree.setId(id);
            semestreService.save(semestreCree);
            response= RestResponse.response(semestre,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveSemestre(SemestreCreateRequestDto semestre, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Niveau niveau = niveauService.show(semestre.getNiveau()).orElseThrow(()-> new EntityNotFoundException("Niveau not found"));
                if (semestre.getIsActive()){
                    this.changeSemestreActiveInBD(niveau);
                }
                Semestre semestreCree = semestre.toEntity(niveau);
                semestreService.save(semestreCree);
                response= RestResponse.response(semestre,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(semestre,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void changeSemestreActiveInBD(Niveau niveau){
        Semestre semestreEncours =semestreService.getSemestreActuelleByNiveau(niveau);
        semestreEncours.setIsActive(false);
        semestreService.save(semestreEncours);
    }
}
