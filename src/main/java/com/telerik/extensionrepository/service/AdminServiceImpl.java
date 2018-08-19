package com.telerik.extensionrepository.service;

import com.telerik.extensionrepository.data.base.AdminRepository;
import com.telerik.extensionrepository.data.base.ExtensionRepository;
import com.telerik.extensionrepository.model.Extension;
import com.telerik.extensionrepository.service.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private ExtensionRepository extensionRepository;
    private AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(ExtensionRepository extensionRepository, AdminRepository adminRepository){
        this.extensionRepository = extensionRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Extension> getUnnaprovedExt() {
        return extensionRepository.getAllExtensions().stream()
                .filter( x-> x.getApproved() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public void approveExt(int id) {

        adminRepository.approveExtension(id);

    }
}