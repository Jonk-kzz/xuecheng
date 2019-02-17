package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CmsConfigController implements CmsConfigControllerApi {


    @Autowired
    CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/get/{id}")
    public CmsConfig getModel(@PathVariable("id")String id) {
        return cmsConfigService.getConfigById(id);
    }
}
