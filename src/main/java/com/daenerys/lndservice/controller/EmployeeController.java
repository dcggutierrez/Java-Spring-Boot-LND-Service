package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.employee.EmployeeRequest;
import com.daenerys.lndservice.dto.employee.SpeakersResponse;
import com.daenerys.lndservice.model.employee.EmployeeEntity;
import com.daenerys.lndservice.service.TwcServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.daenerys.lndservice.config.SwaggerConfig.EMPLOYEE_TAG;

@RestController
@RequestMapping("/api/lnd/employees")
@Api(tags = EMPLOYEE_TAG)
public class EmployeeController {

    private final TwcServices twcServices;
    private final ModelMapper mapper;

    public EmployeeController(TwcServices twcServices, ModelMapper mapper) {
        this.twcServices = twcServices;
        this.mapper = mapper;
    }


    @ApiOperation(value = "View employees", notes =
            "Returns a list of employees. Search filters can be done using \"name\" and \"position\"as query parameters.\n" +
                    "Pagination using \"page\" and \"size\". Default pagination size is 10.", tags = {EMPLOYEE_TAG})
    @GetMapping
    public List<SpeakersResponse> getSpeakers(@RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size,
                                              @RequestParam(name = "position", required = false) String position) {
        Pageable pageable = PageRequest.of(page, size);
        return twcServices
                .getSpeakers(name, position, pageable)
                .stream()
                .map(req -> mapper.map(req, SpeakersResponse.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "View employee by id", notes = "Returns an employee by id", tags = {EMPLOYEE_TAG})
    @GetMapping(path = "/{id}")
    public EmployeeRequest getRequest(@PathVariable UUID id) {
        EmployeeRequest returnValue = new EmployeeRequest();
        Optional<EmployeeEntity> data = twcServices.getEmployee(id);
        EmployeeEntity employeeEntity = data.get();
        BeanUtils.copyProperties(employeeEntity, returnValue);
        return returnValue;
    }
}
