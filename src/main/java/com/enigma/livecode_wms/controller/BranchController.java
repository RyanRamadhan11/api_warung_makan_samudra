package com.enigma.livecode_wms.controller;

import com.enigma.livecode_wms.constant.AppPath;
import com.enigma.livecode_wms.dto.request.BranchRequest;
import com.enigma.livecode_wms.dto.response.BranchResponse;
import com.enigma.livecode_wms.dto.response.CommonResponse;
import com.enigma.livecode_wms.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.BRANCH)
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public List<BranchResponse> getAllBranch() {
        return branchService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBranch(@RequestBody BranchRequest branchRequest) {
        BranchResponse branchResponse = branchService.create(branchRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<BranchResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new branch")
                        .data(branchResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchById(@PathVariable String id) {
        BranchResponse branchResponse = branchService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<BranchResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get branch by id")
                        .data(branchResponse)
                        .build());

    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<?> updateBranch(@RequestBody BranchRequest branchRequest) {
        BranchResponse branchResponse = branchService.update(branchRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<BranchResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully update new branch")
                        .data(branchResponse)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable String id) {
        branchService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Delete Branch")
                        .data(HttpStatus.OK)
                        .build());
    }
}
