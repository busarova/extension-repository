package com.telerik.extensionrepository.service;

import com.telerik.extensionrepository.data.base.ExtensionRepository;
import com.telerik.extensionrepository.data.base.GitExtensionInfoRepository;
import com.telerik.extensionrepository.model.Extension;
import com.telerik.extensionrepository.model.GitExtensionInfo;
import com.telerik.extensionrepository.dto.ExtensionForm;
import com.telerik.extensionrepository.model.UploadFile;
import com.telerik.extensionrepository.service.base.ExtensionService;
import com.telerik.extensionrepository.service.base.GitService;
import com.telerik.extensionrepository.service.base.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class ExtensionServiceImpl implements ExtensionService {

    private ExtensionRepository extensionRepository;
    private GitExtensionInfoRepository gitExtensionInfoRepository;
    private TagService tagService;
    private GitService gitService;

    @Autowired
    public ExtensionServiceImpl(ExtensionRepository extensionRepository, TagService tagService, GitService gitService, GitExtensionInfoRepository gitExtensionInfoRepository){
        this.extensionRepository = extensionRepository;
        this.tagService = tagService;
        this.gitService = gitService;
        this.gitExtensionInfoRepository = gitExtensionInfoRepository;
    }

    // Creates extension from the dto information given at input

    @Override
    public void createExtension(ExtensionForm extensionForm) {

        Extension newExtension = new Extension();

        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        GitExtensionInfo gitExtensionInfo = new GitExtensionInfo();
        gitExtensionInfo.setGitRepoLink(extensionForm.getGithubLink());

        UploadFile uploadFile = new UploadFile();

        // Checks if there is no file or if it is an empty string
        //If there is a file will set it properties
        //Otherwise will leave it null so that thymeleaf will show "No file" in page

        if(extensionForm.getCommonsMultipartFile() != null &&extensionForm.getCommonsMultipartFile().getSize() > 0) {

            uploadFile.setFileName(extensionForm.getCommonsMultipartFile().getOriginalFilename());
            uploadFile.setData(extensionForm.getCommonsMultipartFile().getBytes());

        }

        newExtension.setName(extensionForm.getName());
        newExtension.setDescription(extensionForm.getDescription());
        newExtension.setOwner(user.getUsername());
        newExtension.setTags(extensionForm.getTags());
        newExtension.setVersion(extensionForm.getVersion());
        newExtension.setApproved(1);
        newExtension.setFeatured(1);
        newExtension.setGitExtensionInfo(gitExtensionInfo);
        newExtension.setUploadFile(uploadFile);

        extensionRepository.createExtension(newExtension);

        gitExtensionInfoRepository.updateGitInfo(gitService.getGitDetails(newExtension.getGitExtensionInfo().getGitRepoLink()));


        tagService.loadNewTags(newExtension);

    }

    @Override
    public void updateExtension(Extension extension) {
        extensionRepository.updateExtension(extension);
    }

    @Override
    public void changeExtensionName(Extension extension, String newName) {

        extension.setName(newName);

        extensionRepository.updateExtension(extension);

    }


    @Override
    public void registerDownload(Extension extension) {

        extensionRepository.registerDownload(extension);
    }


}
