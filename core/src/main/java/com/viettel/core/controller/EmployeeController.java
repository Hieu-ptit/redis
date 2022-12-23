package com.viettel.core.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.request.EmployeeRequest;
import com.viettel.core.model.response.EmployeeResponse;
import com.viettel.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

//    @PreAuthorize("hasPermission('*', 'account:create')")
    @PostMapping
    public Response<Void> create(@Valid @RequestBody EmployeeRequest request) throws IOException {
        employeeService.create(request);
        return Response.ofSucceeded();
    }

    @GetMapping("/{id}")
    public Response<EmployeeResponse> getAccount(@PathVariable("id") Long id) {
        return Response.ofSucceeded(employeeService.getEmployee(id));
    }
}
