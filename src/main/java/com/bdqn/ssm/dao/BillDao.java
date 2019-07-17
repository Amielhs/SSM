package com.bdqn.ssm.dao;

import com.bdqn.ssm.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillDao {

    /**
     * @Description:根据供应商ID查询订单数量
     * @param: [providerId]
     * @return: int
     * @Date: 2019-07-14
     */
    public int getBillCountByProviderId(@Param("providerId")Integer providerId)throws Exception;

    /**
     * @Description:根据供应商ID删除订单信息
     * @param: [providerId]
     * @return: int
     * @Date: 2019-07-14
     */
    public int deleteBillByProviderId(@Param("providerId")Integer providerId)throws Exception;

    /**
     * @Description:增加订单
     * @param: [bill]
     * @return: int
     * @Date: 2019-07-14
     */
    public int add(Bill bill)throws Exception;


    /**
     * @Description:通过查询条件获取供应商列表-模糊查询-getBillList
     * @param: [bill]
     * @return: java.util.List<com.bdqn.ssm.pojo.Bill>
     * @Date: 2019-07-14
     */
    public List<Bill> getBillList(Bill bill)throws Exception;

    /**
     * @Description:通过delId删除Bill
     * @param: [delId]
     * @return: int
     * @Date: 2019-07-14
     */
    public int deleteBillById(String delId)throws Exception;


    /**
     * @Description:通过billId获取Bill
     * @param: [id]
     * @return: com.bdqn.ssm.pojo.Bill
     * @Date: 2019-07-14
     */
    public Bill getBillById(String id)throws Exception;

    /**
     * @Description:修改订单信息
     * @param: [bill]
     * @return: int
     * @Date: 2019-07-14
     */
    public int modify(Bill bill)throws Exception;
}
