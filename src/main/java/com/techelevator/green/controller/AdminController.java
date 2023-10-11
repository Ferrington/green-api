package com.techelevator.green.controller;

import com.techelevator.green.payload.response.TableResponse;
import com.techelevator.green.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/tables")
    public List<String> getTableNames() {
        return adminService.getTableNames();
    }

    @GetMapping("/tables/{name}/{page}")
    public TableResponse getTable(@PathVariable String name, @PathVariable int page) {
        return adminService.getTable(name, page);
    }
}
