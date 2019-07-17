package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.error.BusinessException;
import com.bdqn.ssm.error.CommonReturnType;
import com.bdqn.ssm.error.EmBusinessError;
import com.bdqn.ssm.pojo.Role;
import com.bdqn.ssm.pojo.User;
import com.bdqn.ssm.service.RoleService;
import com.bdqn.ssm.service.UserService;
import com.bdqn.ssm.utils.Constants;
import com.bdqn.ssm.utils.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * @ClassName: IndexController
 * @Description:用户页面控制器
 * @Author: amielhs
 * @Date 2019-07-02
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Resource
    private RoleService roleService;

    /**
     * @Description:处理网页请求
     * @param: [username, userPassword]
     * @return: java.lang.String
     * @Date: 2019-07-03
     */
    @RequestMapping(value = "/findUserById/{uId}", method = RequestMethod.GET)
    public String findUserById(@PathVariable(value = "uId", required = false) String uId, Model model) {
        User user = userService.findUserById(uId);
        model.addAttribute("user", user);
        return "user";
    }

    /**
     * @Description:实现跳转到登录页
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-04
     */
    @RequestMapping(value = "/login")
    public String login() {
        logger.info("UserController welcome ========>");
        return "login";
    }

    /**
     * @Description:实现登录
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-04
     */
    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(String userCode, String userPassword, HttpSession session, HttpServletRequest request) {
        logger.info("doLogin ========>");
        /*调用service方法，进行用户匹配*/
        User user = userService.login(userCode, userPassword);
        //匹配密码
        if (null != user) {//登录成功
            //放入session
            session.setAttribute(Constants.USER_SESSION, user);
            //页面跳转(frame.jsp)
            return "redirect:/user/main";
            //Response.sendRedirect("jsp/frame.jsp")
        } else {
            //页面跳转(login.jsp)
            request.setAttribute("error", "用户名或密码不正确");
            return "login";
        }
    }

    /**
     * @Description:进入系统管理
     * @param: [session]
     * @return: java.lang.String
     * @Date: 2019-07-04
     */
    @RequestMapping(value = "/main")
    public String main(HttpSession session) {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            return "redirect:/user/login";
        }
        return "frame";
    }

    /**
     * @Description:错误登录，异常处理
     * @param: [userCode, userPassword]
     * @return: java.lang.String
     * @Date: 2019-07-04
     */
    @RequestMapping(value = "/exlogin", method = RequestMethod.GET)
    public String exLogin(String userCode, String userPassword) {
        logger.debug("exLogin=============>");
        //调用service方法，进行用户匹配
        User user = userService.login(userCode, userPassword);
        if (null == user) {//登录失败
            throw new RuntimeException("用户名或者密码不正确！");
        }
        return "redirect:/user/main";
    }

    /**
     * @Description:退出系统
     * @param: [session]
     * @return: java.lang.String
     * @Date: 2019-07-05
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        //清除session
        session.removeAttribute(Constants.USER_SESSION);
        return "login";
    }

    @RequestMapping(value = "/userlist")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname", required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole", required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        logger.info("getUserList ---- > queryUserName: " + queryUserName);
        logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
        logger.info("getUserList ---- > pageIndex: " + pageIndex);
        int _queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (queryUserRole != null && !queryUserRole.equals("")) {
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/user/syserror";
                //response.sendRedirect("syserror.jsp");
            }
        }
        //总数量（表）
        int totalCount = userService.getUserCounts(queryUserName, _queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        int from = (currentPageNo - 1) * pageSize;
        userList = userService.getUserList(queryUserName, _queryUserRole, from, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "user/userList";
    }

    @RequestMapping(value = "/syserror")
    public String sysError() {
        return "syserror";
    }

    /**
     * @Description: 跳转到添加用户界面
     * @param: []
     * @return: java.lang.String
     * @Date: 2019/07/05 12:25
     */
    @RequestMapping(value = "/useradd")
    public String addUser(@ModelAttribute("user") User user) {
        return "user/userAdd";
    }

    /*添加用户另一种方法*/
    /*@RequestMapping(value="/useradd",method=RequestMethod.GET)
    public String addUser(User user,Model model){
        model.addAttribute("user", user);
        return "user/userAdd";
    }*/

    /*使用Spring表单标签，实现增加用户代码*/
    /*@RequestMapping(value="/add.html",method=RequestMethod.POST)
    public String addSave(User user,HttpSession session){
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if(userService.add(user)){
            return "redirect:/user/userlist";
        }
        return "user/userAdd";
    }*/

    /**
     * @Description:实现用户添加保存成功
     * @param: [user, session]
     * @return: java.lang.String
     * @Date: 2019-07-06
     */
    /*@RequestMapping(value="/useraddsave",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session){
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if(userService.add(user)){
            return "redirect:/user/userlist";
        }
        return "user/userAdd";
    }*/

    /**
     * @Description:单文件上传
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-13
     */
    @RequestMapping(value = "/uploadView")
    public String uploadView() {
        return "file/file";
    }

    /**
     * @Description:做单文件上传
     * @param: [file]
     * @return: java.lang.String
     * @Date: 2019-07-13
     */
    @RequestMapping(value = "/doupload", method = RequestMethod.POST)
    public String doupload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {//上传文件
            logger.info(file.getBytes());
            logger.info(file.getName());
            logger.info(file.getOriginalFilename());
            logger.info(file.getContentType());
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File("D:/temp/Admin/", System.currentTimeMillis() + file.getOriginalFilename()));
        }
        return "file/successFile";
    }

    /**
     * @Description:多文件上传
     * @param: []
     * @return: java.lang.String
     * @Date: 2019-07-13
     */
    @RequestMapping(value = "/multiUploadView")
    public String multiUploadView() {
        return "file/multifile";
    }

    /**
     * @Description:做多文件上传
     * @param: [file]
     * @return: java.lang.String
     * @Date: 2019-07-13
     */
    @RequestMapping(value = "/domultiupload", method = RequestMethod.POST)
    public String domultiupload(MultipartHttpServletRequest multipartRequest) throws IOException {
        Iterator<String> fileNames = multipartRequest.getFileNames();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            MultipartFile file = multipartRequest.getFile(fileName);
            if (!file.isEmpty()) {//上传文件
                logger.info(file.getBytes().length);
                logger.info(file.getName());
                logger.info(file.getOriginalFilename());
                logger.info(file.getContentType());
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File("D:/temp/Admin/", System.currentTimeMillis() + file.getOriginalFilename()));
            }
        }
        return "file/successFile";
    }


    /**
     * @Description:单文件上传
     * @param: [user, session, request, attach]
     * @return: java.lang.String
     * @Date: 2019-07-11
     */
    /*@RequestMapping(value="/useraddsave",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="a_idPicPath", required = false) MultipartFile attach){
        String idPicPath = null;
        //判断文件是否为空
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            logger.info("uploadFile path ============== > "+path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > "+oldFileName);
            String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 50000000;
            logger.debug("uploadFile size============> " + attach.getSize());
            if(attach.getSize() >  filesize){//上传大小不得超过 50000k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 50M");
                return "user/userAdd";
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
                    request.setAttribute("uploadFileError", " * 上传失败！");
                    return "user/userAdd";
                }
                idPicPath = path+File.separator+fileName;
            }else{
                request.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "user/userAdd";
            }
        }
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        user.setIdPicPath(idPicPath);
        if(userService.add(user)){
            return "redirect:/user/userlist";
        }
        return "user/userAdd";
    }*/

    /**
     * @Description:多文件上传
     * @param: [user, session, request, attachs]
     * @return: java.lang.String
     * @Date: 2019-07-11
     */
    /*@RequestMapping(value="/useraddsave",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value = "attachs",required = false) MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for (int i = 0; i < attachs.length ; i++) {
            MultipartFile attach = attachs[i];
            if (!attach.isEmpty()){
                if (i==0){
                    errorInfo = "uploadFileError";
                }else if (i==1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                logger.info("uploadFile oldFileName ============== > "+oldFileName);
                String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 50000000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if (attach.getSize()>filesize){//上传大小不得超过 50000k
                    request.setAttribute(errorInfo, " * 上传大小不得超过 50M");
                    flag = false;
                }else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path,fileName);
                    if (!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if (i==0){
                        idPicPath = path+File.separator+fileName;
                    }else if (i==1){
                        workPicPath = path+File.separator+fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);
                }else {
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if (flag){
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            if(userService.add(user)){
                return "redirect:/user/userlist";
            }
        }
        return "user/userAdd";
    }*/

    /**
     * @Description:多文件上传
     * @param: [user, session, request, idPicFile, workPicFile]
     * @return: java.lang.String
     * @Date: 2019-07-11
     */
    @RequestMapping(value = "/useraddsave", method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "a_idPicPath", required = false) MultipartFile idPicFile,
                              @RequestParam(value = "a_workPicPath", required = false) MultipartFile workPicFile) {
        String idPicPath = null;
        String workPicPath = null;
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ============== > " + path);

        //判断文件是否为空(证件照)
        if (!idPicFile.isEmpty()) {
            String oldFileName = idPicFile.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > " + oldFileName);
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 50000000;
            logger.debug("uploadFile size============> " + idPicFile.getSize());
            if (idPicFile.getSize() > filesize) {//上传大小不得超过 50000k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 50M");
                return "user/userAdd";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {//上传图片格式不正确
                String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                logger.debug("new fileName======== " + idPicFile.getName());
                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                //保存
                try {
                    idPicFile.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("uploadFileError", " * 上传失败！");
                    return "user/userAdd";
                }
                idPicPath = path + File.separator + fileName;
            } else {
                request.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "user/userAdd";
            }
        }

        //判断文件是否为空(工作证照)
        if (!workPicFile.isEmpty()) {
            String oldFileName = workPicFile.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > " + oldFileName);
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 50000000;
            logger.debug("uploadFile size============> " + workPicFile.getSize());
            if (workPicFile.getSize() > filesize) {//上传大小不得超过 50000k
                request.setAttribute("uploadWpError", " * 上传大小不得超过 50M");
                return "user/userAdd";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {//上传图片格式不正确
                String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                logger.debug("new fileName======== " + workPicFile.getName());
                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                //保存
                try {
                    workPicFile.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("uploadWpError", " * 上传失败！");
                    return "user/userAdd";
                }
                workPicPath = path + File.separator + fileName;
            } else {
                request.setAttribute("uploadWpError", " * 上传图片格式不正确");
                return "user/userAdd";
            }
        }

        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        user.setIdPicPath(idPicPath);
        user.setWorkPicPath(workPicPath);
        if (userService.add(user)) {
            return "redirect:/user/userlist";
        }
        return "user/userAdd";
    }

    /*使用JSR303框架完成新增用户的数据校验*/
    /*@RequestMapping(value="/add",method=RequestMethod.POST)
    public String addSave(@Valid User user,BindingResult bindingResult,HttpSession session){
        if(bindingResult.hasErrors()){
            logger.debug("add user validated has error=============================");
            return "user/userAdd";
        }
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        if(userService.add(user)){
            return "redirect:/user/userlist";
        }
        return "user/userAdd";
    }*/

    /*修改用户*/
    /*@RequestMapping(value="/usermodify/{uid}",method=RequestMethod.GET)
    public String getUserById(@PathVariable("uid") String uid,Model model){
        logger.debug("getUserById uid===================== "+uid);
        User user = userService.findUserById(uid);
        model.addAttribute("user",user);
        return "user/userModify";
    }*/

    @RequestMapping(value = "/usermodify/{uid}", method = RequestMethod.GET)
    public String usermodify(@PathVariable String uid, Model model, HttpServletRequest request) {
        logger.debug("getProviderById id===================== " + uid);
        User user = userService.findUserById(uid);
        model.addAttribute(user);
        return "user/userModify";
    }

    /*修改用户进行保存*/
    /*@RequestMapping(value="/usermodifysave",method=RequestMethod.POST)
    public String modifyUserSave(User user,HttpSession session){
        logger.debug("modifyUserSave userid===================== "+user.getId());
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if(userService.modify(user)){
            return "redirect:/user/userlist";
        }
        return "user/userModify";
    }*/

    @RequestMapping(value = "/usermodifysave", method = RequestMethod.POST)
    public String modifyUserSave(User user, HttpSession session, HttpServletRequest request,
                                 @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        logger.debug("modifyUserSave id===================== " + user.getId());
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ============== > " + path);
        if (attachs != null) {
            for (int i = 0; i < attachs.length; i++) {
                MultipartFile attach = attachs[i];
                if (!attach.isEmpty()) {
                    if (i == 0) {
                        errorInfo = "uploadFileError";
                    } else if (i == 1) {
                        errorInfo = "uploadWpError";
                    }
                    String oldFileName = attach.getOriginalFilename();//原文件名
                    logger.info("uploadFile oldFileName ============== > " + oldFileName);
                    String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
                    logger.debug("uploadFile prefix============> " + prefix);
                    int filesize = 50000000;
                    logger.debug("uploadFile size============> " + attach.getSize());
                    if (attach.getSize() > filesize) {//上传大小不得超过 50000k
                        request.setAttribute(errorInfo, " * 上传大小不得超过 50M");
                        flag = false;
                    } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                            || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {//上传图片格式不正确
                        String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                        logger.debug("new fileName======== " + attach.getName());
                        File targetFile = new File(path, fileName);
                        if (!targetFile.exists()) {
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
                        if (i == 0) {
                            idPicPath = path + File.separator + fileName;
                        } else if (i == 1) {
                            workPicPath = path + File.separator + fileName;
                        }
                        logger.debug("idPicPath: " + idPicPath);
                        logger.debug("workPicPath: " + workPicPath);

                    } else {
                        request.setAttribute(errorInfo, " * 上传图片格式不正确");
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            user.setModifyDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if (userService.modify(user)) {
                    return "redirect:/user/userlist";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "user/userModify";
    }

    /*查看用户信息*/
    /*@RequestMapping(value="/view/{id}",method=RequestMethod.GET)
    public String view(@PathVariable String id,Model model){
        logger.debug("view id===================== "+id);
        User user = userService.findUserById(id);
        model.addAttribute(user);
        return "user/userView";
    }*/

    /**
     * @Description:查看用户信息(带证件照的)
     * @param: [id, model, request]
     * @return: java.lang.String
     * @Date: 2019-07-14
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable String id, Model model, HttpServletRequest request) {
        logger.debug("view id===================== " + id);
        User user = new User();
        try {
            user = userService.findUserById(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user.getIdPicPath() != null && !"".equals(user.getIdPicPath())) {
            String[] paths = user.getIdPicPath().split("\\" + File.separator);
            logger.debug("view picPath paths[paths.length-1]============ " + paths[paths.length - 1]);
            user.setIdPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
        }
        if (user.getWorkPicPath() != null && !"".equals(user.getWorkPicPath())) {
            String[] paths = user.getWorkPicPath().split("\\" + File.separator);
            logger.debug("view workPicPath paths[paths.length-1]============ " + paths[paths.length - 1]);
            user.setWorkPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
        }

        model.addAttribute(user);
        return "user/userView";
    }

    /*查询并判断用户编码是否可以使用*/
    /*@RequestMapping(value = "/ucexist")
    @ResponseBody
    public Object userCodeIsExist(String userCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            resultMap.put("userCode", "exist");
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (user != null) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }*/

    /*查询并判断用户编码是否可以使用*/
    @RequestMapping(value = "/ucexist1")
    @ResponseBody
    public Object userCodeIsExist(@RequestParam String userCode) throws BusinessException {
        User user = userService.selectUserCodeExist(userCode);
        if (user==null){
            throw new BusinessException(EmBusinessError.UNFINDUSER_ERROR);
        }
        return CommonReturnType.create(JSONArray.toJSON(user));
    }

    /*@RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public Object view(@RequestParam String id){
        String cjson = "";
        if (null == id || "".equals(id)){
            return  "nodata";
        }else {
            try {
                User user = userService.findUserById(id);
                cjson = JSON.toJSONString(user);
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        return cjson;
    }*/

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Object view(@RequestParam String id) {
        User user = new User();
        try {
            user = userService.findUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /*删除用户*/
    @RequestMapping(value = "/deluser")
    @ResponseBody
    public Object deluser(@RequestParam String uid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(uid)) {
            resultMap.put("delResult", "notexist");
        } else {
            if (userService.deleteUserById(uid)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value = "/modifypwdview")
    public String modifypwdview() {
        logger.info("UserController welcome ========>");
        return "user/pwdModify";
    }

    /**
     * @Description:用户密码修改
     * @param: [oldpassword, session]
     * @return: java.lang.Object
     * @Date: 2019-07-12
     */
    @RequestMapping(value = "/pwdmodify", method = RequestMethod.POST)
    @ResponseBody
    public Object getPwdByUserId(@RequestParam String oldpassword, HttpSession session) {
        logger.debug("getPwdByUserId oldpassword ===================== " + oldpassword);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (null == session.getAttribute(Constants.USER_SESSION)) {//session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) session.getAttribute(Constants.USER_SESSION)).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * @Description:用户密码修改保存
     * @param: [newPassword, session, request]
     * @return: java.lang.String
     * @Date: 2019-07-12
     */
    @RequestMapping(value = "/pwdsave")
    public String pwdSave(@RequestParam(value = "newpassword") String newPassword,
                          HttpSession session,
                          HttpServletRequest request) {
        boolean flag = false;
        Object o = session.getAttribute(Constants.USER_SESSION);
        if (o != null && !StringUtils.isNullOrEmpty(newPassword)) {
            flag = userService.updatePwd(((User) o).getId(), newPassword);
            if (flag) {
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
                return "login";
            } else {
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "user/pwdModify";
    }

}
