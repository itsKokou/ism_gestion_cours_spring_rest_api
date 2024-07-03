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
import projet.core.data.dto.SemestreResponseDto;
import projet.core.data.entities.Module;
import projet.core.data.entities.Niveau;
import projet.core.data.entities.Semestre;
import projet.core.exceptions.EntityNotFoundException;
import projet.core.services.ModuleService;
import projet.restapi.web.controllers.ModuleRestController;
import projet.restapi.web.dtos.RestResponse;
import projet.restapi.web.dtos.request.ModuleCreateRequestDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ModuleRestControllerImpl implements ModuleRestController {
    private final ModuleService moduleService;
    @Override
    public ResponseEntity<Map<Object, Object>> listerModule(int page, int size) {
        Page<Module> datas = moduleService.getAll(PageRequest.of(page,size));
        Page<ModuleResponseDto> dataDtos = datas.map(ModuleResponseDto::toDto);
        Map<Object, Object> model = RestResponse.paginateResponse(
                dataDtos.getContent(), new int[dataDtos.getTotalPages()], dataDtos.getNumber(),dataDtos.hasPrevious(),
                dataDtos.hasNext(), dataDtos.getTotalElements(), dataDtos.getTotalPages(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> getAllModule() {
        Page<Module> datas = moduleService.getAll(PageRequest.of(0,1000));
        Page<ModuleResponseDto> dataDtos = datas.map(ModuleResponseDto::toDto);
        Map<Object, Object> model = RestResponse.response(dataDtos.getContent(), HttpStatus.OK);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> showModule(Long id) {
        Module module = moduleService.show(id).orElse(null);
        Map<Object, Object> response;
        if (module == null){
            response= RestResponse.response(null,HttpStatus.NO_CONTENT);
        }else {
            response= RestResponse.response(ModuleResponseDto.toDto(module),HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> updateModule(ModuleCreateRequestDto module, Long id, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            Module moduleCree = module.toEntity();
            moduleCree.setId(id);
            moduleService.save(moduleCree);
            response= RestResponse.response(module,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<Object, Object>> saveModule(ModuleCreateRequestDto module, BindingResult bindingResult) {
        Map<Object, Object> response;
        if (bindingResult.hasErrors()){
            Map<String, String> errors =new HashMap<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.forEach(fieldError -> errors.put(fieldError.getField(),fieldError.getDefaultMessage()));
            response= RestResponse.response(errors, HttpStatus.NOT_FOUND);
        }else{
            try {
                Module moduleCree = module.toEntity();
                moduleService.save(moduleCree);
                response= RestResponse.response(module,HttpStatus.CREATED);
            }catch (Exception e) {
                response= RestResponse.response(module,HttpStatus.CONFLICT);
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
