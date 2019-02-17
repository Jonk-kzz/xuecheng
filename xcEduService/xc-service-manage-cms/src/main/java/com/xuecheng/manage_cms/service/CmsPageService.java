package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    /**
     * 页面列表分页查询
     * @param page 当前页码
     * @param size  页面显示个数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){

        //增强代码的健壮性
        if(queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }
        if(page<=0){
            page=1;
        }
        if(size<=0){
            size=10;
        }


        //条件匹配器
        //页面名称模糊查询,需要自定义字符串的匹配器实现模糊查询
        final ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("pageType",ExampleMatcher.GenericPropertyMatchers.exact());



        //条件值
        final CmsPage cmsPage = new CmsPage();

        //页面别名
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //页面名称
        if(StringUtils.isNotEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        //页面类型
        if(StringUtils.isNotEmpty(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        //站点ID
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板ID
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }


        //创建条件实例
        final Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);


        //分页对象
         Pageable pageable = PageRequest.of(--page,size);

        //分页查询
         Page<CmsPage> cmsPages = cmsPageRepository.findAll(example,pageable);

        final QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();

        //封装结果集 进行返回
        cmsPageQueryResult.setList(cmsPages.getContent());
        //封装总记录数 进行返回
        cmsPageQueryResult.setTotal(cmsPages.getTotalElements());

        return new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);
    }

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult<CmsPage> add(CmsPage cmsPage){
        //校验页面是否存在，根据页面名称，站台Id，页面webPath查询
        final CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if(cmsPage1!=null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        if(cmsPage1==null){
            cmsPage.setPageId(null); //添加页面主键由spring data自动生成
             cmsPage = cmsPageRepository.save(cmsPage);
            //返回结果
            final CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
                return cmsPageResult;
        }
        return new CmsPageResult(CommonCode.FAIL,null);

    }

    /**
     * 更新页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult<CmsPage> update(String id, CmsPage cmsPage) {
        final CmsPageResult<CmsPage> cmsPageResult = this.getById(id);

        final CmsPage one = cmsPageResult.getCmsPage();

        if(one!=null){
            //更新模板
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //页面类型
            one.setPageType(cmsPage.getPageType());
            //数据路径
            one.setDataUrl(cmsPage.getDataUrl());

            //执行更新
            final CmsPage save = cmsPageRepository.save(one);
            if(save != null){
                //返回成功
                return new CmsPageResult(CommonCode.SUCCESS, save);

            }

        }

        return new CmsPageResult<CmsPage>(CommonCode.FAIL,null);


    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据Id查询页面
     * @param id 主键Id
     * @return
     */
    public CmsPageResult<CmsPage> getById(String id){
        final Optional<CmsPage> optional = cmsPageRepository.findById(id);

        if(optional.isPresent()){
          return new CmsPageResult<CmsPage>(CommonCode.SUCCESS,optional.get());
        }

        //返回空
        return new CmsPageResult<CmsPage>(CommonCode.FAIL,null);
    }

    /**
     * 站台列表
     * @return
     */
    public QueryResponseResult findBySite() {
        final List<CmsSite> cmsSiteList = cmsSiteRepository.findAll();

        return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult(cmsSiteList,cmsSiteList.size()));
    }

    /**
     * 模板列表
     * @return
     */
    public QueryResponseResult findByTemplate() {
        final List<CmsTemplate> cmsTemplateList = cmsTemplateRepository.findAll();

        return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult(cmsTemplateList,cmsTemplateList.size()));
    }


}
