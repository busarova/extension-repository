package com.telerik.extensionrepository.service;

import com.telerik.extensionrepository.data.base.ExtensionRepository;
import com.telerik.extensionrepository.model.Extension;
import com.telerik.extensionrepository.model.GitExtensionInfo;
import com.telerik.extensionrepository.model.base.ExtensionForm;
import com.telerik.extensionrepository.service.base.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class ExtensionServiceImpl implements ExtensionService {

    private ExtensionRepository extensionRepository;

    @Autowired
    public ExtensionServiceImpl(ExtensionRepository extensionRepository){
        this.extensionRepository = extensionRepository;
    }

    @Override
    public void createExtension(ExtensionForm extensionForm) {

        int gitId = createGitExtentionInfo(extensionForm);

        Extension newExtension = new Extension();

        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        newExtension.setName(extensionForm.getName());
        newExtension.setDescription(extensionForm.getDescription());
        newExtension.setOwner(user.getUsername());
        newExtension.setTags(extensionForm.getTags());
        newExtension.setGitId(gitId);
        newExtension.setVersion(extensionForm.getVersion());
        newExtension.setApproved(1);
        newExtension.setUploadDate("15/08/2018");

        extensionRepository.createExtension(newExtension);

    }

    @Override
    public int createGitExtentionInfo(ExtensionForm extensionForm) {

        GitExtensionInfo gitExtensionInfo = new GitExtensionInfo();

        gitExtensionInfo.setGitRepoLink(extensionForm.getGithubLink());

        return extensionRepository.createGithub_info(gitExtensionInfo);

    }


}