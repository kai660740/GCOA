package com.qylm.dto.engineering;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.qylm.bean.returner.Returner;
import com.qylm.entity.Employee;
import com.qylm.entity.EngineeringProjectDetail;

/**
 * 保存工作登记管理画面需要的值
 * @author smj
 */
public class ProjectStateManageDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8465805699914549584L;
	
	/**
	 * 车辆编号
	 */
	private String number;

	/**
	 * 客户
	 */
	private String customerName;
	
	/**
	 * 项目开始日期（开始）
	 */
	private Date beginDate;
	
	/**
	 * 项目开始日期（结束）
	 */
	private Date endDate;
	
	/**
	 * 项目结束日期（开始）
	 */
	private Date startEndDate;
	
	/**
	 * 项目结束日期（结束）
	 */
	private Date endEndDate;
	
	/**
	 * 工地名称
	 */
	private String constructionName;
	
	/**
	 * 工作地址
	 */
	private String workAddress;
	
	/**
	 * 项目状态
	 * 1：未生效，（显示红色#FF0000）
	 * 2：工作安排中，（显示黄色#FFD700）
	 * 3：工作已安排，（显示绿色#00FF00）
	 * 4：工作暂停，（显示暗红色#FF4040）
	 * 5：工作完结，（显示蓝色#0000FF）
	 */
	private String type;
	
	/**
	 * 修改传值
	 */
	private EngineeringProjectDetail engineeringProjectDetail;
	
	/**
	 * 工作地址
	 */
	private String address;
	
	/**
	 * 泵送部位
	 */
	private String pumpParts;
	
	/**
	 * 工作方量
	 */
	private BigDecimal workVolume;
	
	/**
	 * 完成量，进度条
	 */
	private BigDecimal schedule;
	
	/**
	 * 新完成方量
	 */
	private BigDecimal newSchedule;
	
	/**
	 * 加油量
	 */
	private BigDecimal gasVolume;
	
	/**
	 * 新加油量
	 */
	private BigDecimal newGasVolume;
	
	/**
	 * 油费
	 */
	private BigDecimal gasPrice;
	
	/**
	 * 新油费
	 */
	private BigDecimal newGasPrice;
	
	/**
	 * 泵送开始时间
	 */
	private Date startPumpDate;
	
	/**
	 * 泵送结束时间
	 */
	private Date endPumpDate;
	
	/**
	 * 油价消费人
	 */
	private Employee employee;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 是否显示返回按钮
	 * @return true显示反之不显示
	 */
	public boolean isShowBackBtn() {
		return returner != null;
	}
	
	/**
	 * 共通方法返回值
	 */
	private Returner<?, ?, ?> returner;

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startEndDate
	 */
	public Date getStartEndDate() {
		return startEndDate;
	}

	/**
	 * @param startEndDate the startEndDate to set
	 */
	public void setStartEndDate(Date startEndDate) {
		this.startEndDate = startEndDate;
	}

	/**
	 * @return the endEndDate
	 */
	public Date getEndEndDate() {
		return endEndDate;
	}

	/**
	 * @param endEndDate the endEndDate to set
	 */
	public void setEndEndDate(Date endEndDate) {
		this.endEndDate = endEndDate;
	}

	/**
	 * @return the constructionName
	 */
	public String getConstructionName() {
		return constructionName;
	}

	/**
	 * @param constructionName the constructionName to set
	 */
	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}

	/**
	 * @return the workAddress
	 */
	public String getWorkAddress() {
		return workAddress;
	}

	/**
	 * @param workAddress the workAddress to set
	 */
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the engineeringProjectDetail
	 */
	public EngineeringProjectDetail getEngineeringProjectDetail() {
		return engineeringProjectDetail;
	}

	/**
	 * @param engineeringProjectDetail the engineeringProjectDetail to set
	 */
	public void setEngineeringProjectDetail(
			EngineeringProjectDetail engineeringProjectDetail) {
		this.engineeringProjectDetail = engineeringProjectDetail;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the pumpParts
	 */
	public String getPumpParts() {
		return pumpParts;
	}

	/**
	 * @param pumpParts the pumpParts to set
	 */
	public void setPumpParts(String pumpParts) {
		this.pumpParts = pumpParts;
	}

	/**
	 * @return the workVolume
	 */
	public BigDecimal getWorkVolume() {
		return workVolume;
	}

	/**
	 * @param workVolume the workVolume to set
	 */
	public void setWorkVolume(BigDecimal workVolume) {
		this.workVolume = workVolume;
	}

	/**
	 * @return the schedule
	 */
	public BigDecimal getSchedule() {
		return schedule;
	}

	/**
	 * @param schedule the schedule to set
	 */
	public void setSchedule(BigDecimal schedule) {
		this.schedule = schedule;
	}

	/**
	 * @return the newSchedule
	 */
	public BigDecimal getNewSchedule() {
		return newSchedule;
	}

	/**
	 * @param newSchedule the newSchedule to set
	 */
	public void setNewSchedule(BigDecimal newSchedule) {
		this.newSchedule = newSchedule;
	}

	/**
	 * @return the gasVolume
	 */
	public BigDecimal getGasVolume() {
		return gasVolume;
	}

	/**
	 * @param gasVolume the gasVolume to set
	 */
	public void setGasVolume(BigDecimal gasVolume) {
		this.gasVolume = gasVolume;
	}

	/**
	 * @return the newGasVolume
	 */
	public BigDecimal getNewGasVolume() {
		return newGasVolume;
	}

	/**
	 * @param newGasVolume the newGasVolume to set
	 */
	public void setNewGasVolume(BigDecimal newGasVolume) {
		this.newGasVolume = newGasVolume;
	}

	/**
	 * @return the gasPrice
	 */
	public BigDecimal getGasPrice() {
		return gasPrice;
	}

	/**
	 * @param gasPrice the gasPrice to set
	 */
	public void setGasPrice(BigDecimal gasPrice) {
		this.gasPrice = gasPrice;
	}

	/**
	 * @return the newGasPrice
	 */
	public BigDecimal getNewGasPrice() {
		return newGasPrice;
	}

	/**
	 * @param newGasPrice the newGasPrice to set
	 */
	public void setNewGasPrice(BigDecimal newGasPrice) {
		this.newGasPrice = newGasPrice;
	}

	/**
	 * @return the startPumpDate
	 */
	public Date getStartPumpDate() {
		return startPumpDate;
	}

	/**
	 * @param startPumpDate the startPumpDate to set
	 */
	public void setStartPumpDate(Date startPumpDate) {
		this.startPumpDate = startPumpDate;
	}

	/**
	 * @return the endPumpDate
	 */
	public Date getEndPumpDate() {
		return endPumpDate;
	}

	/**
	 * @param endPumpDate the endPumpDate to set
	 */
	public void setEndPumpDate(Date endPumpDate) {
		this.endPumpDate = endPumpDate;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the returner
	 */
	public Returner<?, ?, ?> getReturner() {
		return returner;
	}

	/**
	 * @param returner the returner to set
	 */
	public void setReturner(Returner<?, ?, ?> returner) {
		this.returner = returner;
	}
	
}
