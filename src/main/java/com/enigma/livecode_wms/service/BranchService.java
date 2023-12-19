package com.enigma.livecode_wms.service;

import com.enigma.livecode_wms.dto.request.BranchRequest;
import com.enigma.livecode_wms.dto.response.BranchResponse;

import java.util.List;

public interface BranchService {

    List<BranchResponse> getAll();

    BranchResponse create(BranchRequest branchRequest);

    BranchResponse update(BranchRequest branchRequest);

    void delete(String id);

    BranchResponse getById(String id);
}
