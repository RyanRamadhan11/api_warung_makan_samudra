package com.enigma.livecode_wms.service.impl;

import com.enigma.livecode_wms.dto.request.BranchRequest;
import com.enigma.livecode_wms.dto.response.BranchResponse;
import com.enigma.livecode_wms.entity.Branch;
import com.enigma.livecode_wms.repository.BranchRepository;
import com.enigma.livecode_wms.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public List<BranchResponse> getAll() {
        List<Branch> branches = branchRepository.findAll();

        List<BranchResponse> branchResponses = branches.stream()
                .map(branch -> BranchResponse.builder()
                        .id(branch.getId())
                        .branchCode(branch.getBranchCode())
                        .branchName(branch.getBranchName())
                        .address(branch.getAddress())
                        .phoneNumber(branch.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        return branchResponses;
    }

    @Override
    public BranchResponse create(BranchRequest branchRequest) {
        Branch branch = Branch.builder()
                .branchCode(branchRequest.getBranchCode())
                .branchName(branchRequest.getBranchName())
                .address(branchRequest.getAddress())
                .phoneNumber(branchRequest.getPhoneNumber())
                .build();

        branchRepository.save(branch);
        return BranchResponse.builder()
                .id(branch.getId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }

    @Override
    public BranchResponse update(BranchRequest branchRequest) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchRequest.getId());

        if (optionalBranch.isPresent()){
            Branch branch = Branch.builder()
                    .id(branchRequest.getId())
                    .branchCode(branchRequest.getBranchCode())
                    .branchName(branchRequest.getBranchName())
                    .address(branchRequest.getAddress())
                    .phoneNumber(branchRequest.getPhoneNumber())
                    .build();

            branchRepository.save(branch);
            return BranchResponse.builder()
                    .id(branch.getId())
                    .branchCode(branch.getBranchCode())
                    .branchName(branch.getBranchName())
                    .address(branch.getAddress())
                    .phoneNumber(branch.getPhoneNumber())
                    .build();
        }else {
            return null;
        }
    }

    @Override
    public void delete(String id) {
       Branch branch = branchRepository.findById(id).orElse(null);
        if (branch != null) {
            branchRepository.deleteById(id);
            System.out.println("delete branch success");
        } else {
            System.out.println("id not found");
        }
    }

    @Override
    public BranchResponse getById(String id) {
        Branch branch = branchRepository.findById(id).orElse(null);
        if (branch != null) {
            return BranchResponse.builder()
                    .id(branch.getId())
                    .branchCode(branch.getBranchCode())
                    .branchName(branch.getBranchName())
                    .address(branch.getAddress())
                    .phoneNumber(branch.getPhoneNumber())
                    .build();
        }
        return null;
    }
}
