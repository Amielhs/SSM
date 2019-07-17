package com.bdqn.ssm.dao;

import com.bdqn.ssm.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderDao {

    /**
     * @Description:通过条件查询-供应商表记录数
     * @param: [proName, proCode]
     * @return: int
     * @Date: 2019-07-05
     */
    public int getProviderCounts(@Param("pName")String proName,@Param("pCode")String proCode);

    /**
     * @Description:通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param: [queryProName, queryProCode, currentPageNo, pageSize]
     * @return: java.util.List<com.bdqn.ssm.pojo.Provider>
     * @Date: 2019-07-05
     */
    List<Provider> getProviderLists(@Param("qProName")String queryProName,@Param("qProCode") String queryProCode,@Param("from") int currentPageNo,@Param("pSize") int pageSize);

    /**
     * @Description:增加供应商
     * @param: [provider]
     * @return: int
     * @Date: 2019-07-06
     */
    public int add(Provider provider);

    /**
     * @Description:通过proId获取Provider
     * @param: [id]
     * @return: com.bdqn.ssm.pojo.Provider
     * @Date: 2019-07-09
     */
    public Provider getProviderById(@Param("pId") String id);

    /**
     * @Description:修改用户信息
     * @param: [provider]
     * @return: int
     * @Date: 2019-07-09
     */
    public int modify(Provider provider);

    /**
     * @Description:根据pid删除供货商
     * @param: [proid]
     * @return: int
     * @Date: 2019-07-11
     */
    /*int deleteProviderById(@Param("proid") String proid);*/

    /**
     * @Description:通过供应商id删除供应商信息
     * @param: [proid]
     * @return: int
     * @Date: 2019-07-14
     */
     int deleteProviderById(@Param("proid")String proid)throws Exception;
}
