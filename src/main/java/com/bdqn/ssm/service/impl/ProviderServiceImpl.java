package com.bdqn.ssm.service.impl;

import com.bdqn.ssm.dao.BillDao;
import com.bdqn.ssm.dao.ProviderDao;
import com.bdqn.ssm.pojo.Provider;
import com.bdqn.ssm.service.ProviderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service("providerService")
@Transactional
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderDao providerDao;
    @Resource
    private BillDao billDao;

    /**
     * @Description:通过条件查询-供应商表记录数
     * @param: [proName, proCode]
     * @return: int
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int getProviderCount(String proName, String proCode) {
        return providerDao.getProviderCounts(proName,proCode);
    }

    /**
     * @Description:通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param: [queryProName, queryProCode, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.Provider>
     * @Date: 2019-07-05
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Provider> getProviderList(String queryProName, String queryProCode, int currentPageNo, int pageSize) {
        return providerDao.getProviderLists(queryProName,queryProCode,currentPageNo,pageSize);
    }

    /**
     * @Description:增加供应商
     * @param: [provider]
     * @return: boolean
     * @Date: 2019-07-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean add(Provider provider) {
        boolean flag = false;
        if (providerDao.add(provider)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:通过proId获取Provider
     * @param: [id]
     * @return: com.bdqn.ssm.pojo.Provider
     * @Date: 2019-07-09
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Provider getProviderById(String id) {
        return providerDao.getProviderById(id);
    }

    /**
     * @Description:修改用户信息
     * @param: [provider]
     * @return: boolean
     * @Date: 2019-07-09
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean modify(Provider provider) {
        boolean flag = false;
        if (providerDao.modify(provider)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * @Description:根据pid删除供货商
     * @param: [proid]
     * @return: boolean
     * @Date: 2019-07-11
     */
    /*@Override
    public boolean deleteProviderById(String proid) {
        boolean flag = false;
        if (providerDao.deleteProviderById(proid)>0){
            flag = true;
        }
        return flag;
    }*/

    /**
     * @Description:业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则进行级联删除：先删订单，后删供应商
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     *
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     * @param: [delId]
     * @return: boolean
     * @Date: 2019-07-14
     */
    /*@Override
    public  boolean deleteProviderById(Integer delId) throws Exception {
        boolean flag = true;
        int billCount = billDao.getBillCountByProviderId(delId);
        if(billCount > 0 ){//先删除订单信息
            billDao.deleteBillByProviderId(delId);
        }
        //先删除该条记录的上传文件
        Provider provider = providerDao.getProviderById(String.valueOf(delId));
        if(provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")){
            //删除服务器上企业营业执照照片
            File file = new File(provider.getCompanyLicPicPath());
            if(file.exists()){
                flag = file.delete();
            }
        }
        if(flag && provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")){
            //删除服务器上组织机构代码证照片
            File file = new File(provider.getOrgCodePicPath());
            if(file.exists()){
                flag = file.delete();
            }
        }
        if(!flag){
            throw new Exception();
        }
        providerDao.deleteProviderById(String.valueOf(delId));
        return flag;
    }*/

    @Override
    public int deleteProviderById(String delId) {
        int billCount = -1;
        try {
            billCount = billDao.getBillCountByProviderId(Integer.valueOf(delId));
            if(billCount == 0){
                //先删除该条记录的上传文件
                Provider provider = providerDao.getProviderById(delId);
                boolean flag = true;
                if(provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")){
                    //删除服务器上企业营业执照照片
                    File file = new File(provider.getCompanyLicPicPath());
                    if(file.exists()){
                        flag = file.delete();
                    }
                }
                if(flag && provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")){
                    //删除服务器上组织机构代码证照片
                    File file = new File(provider.getOrgCodePicPath());
                    if(file.exists()){
                        flag = file.delete();
                    }
                }
                if(flag){
                    providerDao.deleteProviderById(delId);
                }else{
                    billCount = -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
        }
        return billCount;
    }
}
