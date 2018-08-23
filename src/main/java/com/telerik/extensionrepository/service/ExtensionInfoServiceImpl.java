package com.telerik.extensionrepository.service;

import com.telerik.extensionrepository.data.base.ExtensionRepository;
import com.telerik.extensionrepository.model.Extension;
import com.telerik.extensionrepository.service.base.ExtensionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ExtensionInfoServiceImpl implements ExtensionInfoService {

    private ExtensionRepository extensionRepository;

    @Autowired
    public ExtensionInfoServiceImpl(ExtensionRepository extensionRepository){
        this.extensionRepository = extensionRepository;
    }

    @Override
    public List<Extension> getFeatured() {                                                  // gets all the extensions which are featured and approved

        return extensionRepository.getFeaturedExtensions();

    }

    @Override
    public List<Extension> getPopular() {                                               // gets all extensions and sorts them by number of downloads

        return extensionRepository.getPopularExtensions();
    }

    @Override
    public List<Extension> getNew() {
        return extensionRepository.getNewExtensions();
    }

    @Override
    public List<Extension> getByUserName(String userName) {

        return extensionRepository.getByUserName(userName);

    }

    @Override
    public Extension getExtByName(String name) {

        return extensionRepository.getExtByName(name);

    }

    @Override
    public List<Extension> returnOrderedBy(String parameter) {                       // sorts the list by the parameter

        String[] commands = parameter.split(" ");

        switch (commands[0]){

            case "popular":
                return sortListBy(getPopular(), commands[1]);

            case "featured":
                return sortListBy(getFeatured(), commands[1]);

            case "new":
                return sortListBy(getNew(), commands[1]);

        }

        return null;
    }

    @Override
    public List<Extension> sortListBy(List<Extension> list, String parameter) {

        switch (parameter){

            case "name":
                list.sort(Comparator.comparing(Extension::getName));
                return list;

            case "downloads":
                list.sort(Comparator.comparing(Extension::getNumberOfDownloads));
                 return list;

            case "uploadDate":

                list.sort(Comparator.comparing(Extension::getUploadDate));
                return list;

            case "lastCommitDate":

                list.sort(Comparator.comparing(Extension::getUploadDate));
                return list;

        }

        return null;
    }

}