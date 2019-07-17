package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.pojo.Provider;
import com.bdqn.ssm.pojo.User;
import com.bdqn.ssm.service.ProviderService;
import com.bdqn.ssm.utils.Constants;
import com.bdqn.ssm.utils.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: ProviderController
 * @Description:供货商页面控制器
 * @Author: amielhs
 * @Date 2019-07-05
 */
@Controller
@RequestMapping("/provider")
public class ProviderController {
    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private ProviderService providerService;

    @RequestMapping(value="/providerlist")
    public String getProviderList(Model model,
                                  @RequestParam(value="queryProCode",required=false) String queryProCode,
                                  @RequestParam(value="queryProName",required=false) String queryProName,
                                  @RequestParam(value="pageIndex",required=false) String pageIndex){
        logger.info("getProviderList ---- > queryProCode: " + queryProCode);
        logger.info("getProviderList ---- > queryProName: " + queryProName);
        logger.info("getProviderList ---- > pageIndex: " + pageIndex);
        List<Provider> providerList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        if(queryProCode == null){
            queryProCode = "";
        }
        if(queryProName == null){
            queryProName = "";
        }
        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/provider/syserror";
            }
        }
        //总数量（表）
        int totalCount	= providerService.getProviderCount(queryProName,queryProCode);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        int from = (currentPageNo-1)*pageSize;
        providerList = providerService.getProviderList(queryProName,queryProCode,from, pageSize);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProCode", queryProCode);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "provider/providerList";
    }

    @RequestMapping(value="/syserror")
    public String sysError(){
        return "syserror";
    }

    /**
     * @Description: 跳转到添加供应商界面
     * @param: []
     * @return: java.lang.String
     * @Date: 2019/07/05 12:25
     */
    @RequestMapping(value = "/provideradd",method= RequestMethod.GET)
    public String addProvider(@ModelAttribute("provider") Provider provider) {
        return "provider/providerAdd";
    }

    /*增加供应商-spring表单标签+JSR303校验*/
    /*@RequestMapping(value="/provideradd",method=RequestMethod.POST)
    public String addSave(@Valid Provider provider, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            logger.debug("add provider validated has error=============================");
            return "provider/providerAdd";
        }
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        if(providerService.add(provider)){
            return "redirect:/provider/providerlist";
        }
        return "provider/providerAdd";
    }*/

    /**
     * @Description:实现供应商添加保存成功
     * @param: [provider, session]
     * @return: java.lang.String
     * @Date: 2019-07-06
     */
    /*@RequestMapping(value="/provideraddsave",method=RequestMethod.POST)
    public String addProviderSave(Provider provider, HttpSession session){
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        if(providerService.add(provider)){
            return "redirect:/provider/providerlist";
        }
        return "provider/providerAdd";
    }*/

    @RequestMapping(value="/provideraddsave",method=RequestMethod.POST)
    public String addProviderSave(Provider provider,
                                  HttpSession session,
                                  HttpServletRequest request,
                                  @RequestParam("attachs") MultipartFile[] attachs){
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadOcError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                int filesize = 50000000;
                if(attach.getSize() >  filesize){//上传大小不得超过 50000k
                    request.setAttribute(errorInfo, " * 上传大小不得超过 50M");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        companyLicPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        orgCodePicPath = path+File.separator+fileName;
                    }
                    logger.debug("companyLicPicPath: " + companyLicPicPath);
                    logger.debug("orgCodePicPath: " + orgCodePicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            try {
                if(providerService.add(provider)){
                    return "redirect:/provider/providerlist";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "provider/providerAdd";
    }

    /*@RequestMapping(value="/view/{id}",method=RequestMethod.GET)
    public String view(@PathVariable String id, Model model){
        logger.debug("view id===================== "+id);
        Provider provider = providerService.getProviderById(id);
        model.addAttribute(provider);
        return "provider/providerView";
    }*/

    @RequestMapping(value="/view/{id}",method=RequestMethod.GET)
    public String view(@PathVariable String id,Model model,HttpServletRequest request){
        logger.debug("view id===================== "+id);
        Provider provider = new Provider();
        try {
            provider = providerService.getProviderById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(provider.getCompanyLicPicPath() != null && !"".equals(provider.getCompanyLicPicPath())){
            String[] paths = provider.getCompanyLicPicPath().split("\\"+File.separator);
            logger.debug("view companyLicPicPath paths[paths.length-1]============ " + paths[paths.length-1]);
            provider.setCompanyLicPicPath(request.getContextPath()+"/statics/uploadfiles/"+paths[paths.length-1]);
        }
        if(provider.getOrgCodePicPath() != null && !"".equals(provider.getOrgCodePicPath())){
            String[] paths = provider.getOrgCodePicPath().split("\\"+File.separator);
            logger.debug("view orgCodePicPath paths[paths.length-1]============ " + paths[paths.length-1]);
            provider.setOrgCodePicPath(request.getContextPath()+"/statics/uploadfiles/"+paths[paths.length-1]);
        }

        model.addAttribute(provider);
        return "provider/providerView";
    }

    @RequestMapping(value="/modify/{id}",method=RequestMethod.GET)
    public String modify(@PathVariable String id,Model model,HttpServletRequest request){
        logger.debug("getProviderById id===================== "+id);
        Provider provider = providerService.getProviderById(id);
        model.addAttribute(provider);
        return "provider/providerModify";
    }

    /*@RequestMapping(value="/modifysave",method=RequestMethod.POST)
    public String modifyProviderSave(Provider provider,HttpSession session){
        logger.debug("modifyProviderSave id===================== "+provider.getId());
        provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        if(providerService.modify(provider)){
            return "redirect:/provider/providerlist";
        }
        return "provider/providerModify";
    }*/

    @RequestMapping(value="/modifysave",method=RequestMethod.POST)
    public String modifyProviderSave(Provider provider,HttpSession session,HttpServletRequest request,
                                     @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        logger.debug("modifyProviderSave id===================== "+provider.getId());
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        if(attachs != null){
            for(int i = 0;i < attachs.length ;i++){
                MultipartFile attach = attachs[i];
                if(!attach.isEmpty()){
                    if(i == 0){
                        errorInfo = "uploadFileError";
                    }else if(i == 1){
                        errorInfo = "uploadOcError";
                    }
                    String oldFileName = attach.getOriginalFilename();//原文件名
                    String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                    int filesize = 50000000;
                    if(attach.getSize() >  filesize){//上传大小不得超过 50000k
                        request.setAttribute(errorInfo, " * 上传大小不得超过 50M");
                        flag = false;
                    }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                            || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                        String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                        logger.debug("new fileName======== " + attach.getName());
                        File targetFile = new File(path, fileName);
                        if(!targetFile.exists()){
                            targetFile.mkdirs();
                        }
                        //保存
                        try {
                            attach.transferTo(targetFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, " * 上传失败！");
                            flag = false;
                        }
                        if(i == 0){
                            companyLicPicPath = path+File.separator+fileName;
                        }else if(i == 1){
                            orgCodePicPath = path+File.separator+fileName;
                        }
                        logger.debug("companyLicPicPath: " + companyLicPicPath);
                        logger.debug("orgCodePicPath: " + orgCodePicPath);

                    }else{
                        request.setAttribute(errorInfo, " * 上传图片格式不正确");
                        flag = false;
                    }
                }
            }
        }
        if(flag){
            provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setModifyDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            try {
                if(providerService.modify(provider)){
                    return "redirect:/provider/providerlist";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "provider/providerModify";
    }

    /**
     * @Description:删除供货商
     * @param: [id]
     * @return: java.lang.Object
     * @Date: 2019-07-14
     */
    /*@RequestMapping(value="/delprovider",method=RequestMethod.POST)
    @ResponseBody
    public Object delProviderById(@RequestParam(value="proid") String id){
        logger.debug("delProviderById proid ===================== "+id);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            boolean flag = false;
            try {
                flag = providerService.deleteProviderById(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        return JSONArray.toJSONString(resultMap);
    }*/

    /**
     * @Description:删除供货商
     * @param: [id]
     * @return: java.lang.Object
     * @Date: 2019-07-14
     */
    @RequestMapping(value="/delprovider",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public Object delProviderById(@RequestParam(value="proid") String id) throws Exception {
        logger.debug("delProviderById proid ===================== "+id);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * @Description:供货商文件上传(上传企业营业执照照片)
     * @param: [provider, session, request, attach]
     * @return: java.lang.String
     * @Date: 2019-07-11
     */
    /*@RequestMapping(value="/provideraddsave",method=RequestMethod.POST)
    public String addProviderSave(Provider provider, HttpSession session, HttpServletRequest request,
                                  @RequestParam(value ="a_companyLicPicPath", required = false) MultipartFile attach){
        String companyLicPicPath = null;
        //判断文件是否为空
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix= FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 50000000;
            if(attach.getSize() >  filesize){//上传大小不得超过 50000k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 50M");
                return "provideradd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                File targetFile = new File(path, fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //保存
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("uploadFileError", " * 上传失败！");
                    return "provider/providerAdd";
                }
                companyLicPicPath = path+File.separator+fileName;
            }else{
                request.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "provider/providerAdd";
            }
        }
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        provider.setCompanyLicPicPath(companyLicPicPath);
        if(providerService.add(provider)){
            return "redirect:/provider/providerlist";
        }
        return "provider/providerAdd";
    }*/


    /**
     * @Description:供货商文件上传(上传企业营业执照和组织机构代码证照片)
     * @param: [provider, session, request, attachs]
     * @return: java.lang.String
     * @Date: 2019-07-11
     */
    /*@RequestMapping(value="/provideraddsave",method=RequestMethod.POST)
    public String addProviderSave(Provider provider,HttpSession session,HttpServletRequest request,
                                  @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadOcError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                int filesize = 50000000;
                if(attach.getSize() >  filesize){//上传大小不得超过 50000k
                    request.setAttribute(errorInfo, " * 上传大小不得超过 50M");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        companyLicPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        orgCodePicPath = path+File.separator+fileName;
                    }
                    logger.debug("companyLicPicPath: " + companyLicPicPath);
                    logger.debug("orgCodePicPath: " + orgCodePicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            if(providerService.add(provider)){
                return "redirect:/provider/providerlist";
            }
        }
        return "provider/providerAdd";
    }*/
}
