package com.bdqn.ssm.service;

import com.bdqn.ssm.pojo.Provider;

import java.util.List;

public interface ProviderService {

    /**
     * @Description:通过条件查询-供应商表记录数
     * @param: [proName, proCode]
     * @return: int
     * @Date: 2019-07-05
     */
    public int getProviderCount(String proName,String proCode);

    /**
     * @Description:通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param: [queryProName, queryProCode, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.Provider>
     * @Date: 2019-07-05
     */
    List<Provider> getProviderList(String queryProName, String queryProCode, int currentPageNo, int pageSize);

    /**
     * @Description:增加供应商
     * @param: [provider]
     * @return: boolean
     * @Date: 2019-07-06
     */
    public boolean add(Provider provider);

    /**
     * @Description:通过proId获取Provider
     * @param: [id]
     * @return: com.bdqn.ssm.pojo.Provider
     * @Date: 2019-07-09
     */
    public Provider getProviderById(String id);

    /**
     * @Description:修改用户信息
     * @param: [provider]
     * @return: boolean
     * @Date: 2019-07-09
     */
    public boolean modify(Provider provider);

    /**
     * @Description:根据pid删除供货商
     * @param: [delId]
     * @return: boolean
     * @Date: 2019-07-11
     */
    /*public boolean deleteProviderById(Integer delId) throws Exception;*/
    public int deleteProviderById(String delId) throws Exception;
}
