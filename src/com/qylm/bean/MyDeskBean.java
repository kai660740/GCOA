package com.qylm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qylm.bean.dailyPlan.PlanArrangementManageBean;
import com.qylm.bean.engineering.EngineeringProjectManageBean;
import com.qylm.bean.engineering.ProjectStateManageBean;
import com.qylm.bean.engineeringMaterials.MaterialsGrantManageBean;
import com.qylm.bean.engineeringMaterials.MaterialsGrantManageProjectBean;
import com.qylm.bean.engineeringMaterials.ProblemProjectManageBean;
import com.qylm.bean.fitting.FittingPurchaseManageBean;
import com.qylm.bean.fitting.FittingStorageManageBean;
import com.qylm.bean.procedure.FittingPurchaseExamineManageBean;
import com.qylm.bean.returner.MyDeskReturner;
import com.qylm.common.Navigation;
import com.qylm.common.Tool;
import com.qylm.common.utils.BigDecimalUtil;
import com.qylm.common.utils.DateUtil;
import com.qylm.common.utils.GenericCodeUtil;
import com.qylm.constants.Constants;
import com.qylm.dto.MyDeskDto;
import com.qylm.entity.EngineeringProject;
import com.qylm.entity.EngineeringProjectDetail;
import com.qylm.entity.FittingPurchase;
import com.qylm.entity.FittingPurchaseDetail;
import com.qylm.entity.FittingPurchaseExamine;
import com.qylm.entity.FittingStorage;
import com.qylm.entity.FittingStorageDetail;
import com.qylm.entity.ProjectEmployeeDetail;
import com.qylm.service.EngineeringProjectService;

/**
 * 桌面显示上下班按钮，内部消息，BUG管理，工作安排
 * @author 
 */
@ManagedBean
@RequestScoped
public class MyDeskBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5623059981716430758L;

	/**
	 * LOG 日志
	 */
	private static final Log LOG = LogFactory.getLog(MyDeskBean.class);
	
	/**
	 * 存放我的桌面需要保存的值
	 */
	private MyDeskDto myDeskDto = new MyDeskDto();
	
	/**
	 * 工程项目业务类
	 */
	@ManagedProperty(value="#{engineeringProjectService}")
	private EngineeringProjectService engineeringProjectService;
	
	/**
	 * 登陆用户信息
	 */
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	/**
	 * 绑定与我的桌面按钮
	 * @return
	 */
	public String getAllMyDesk() {
		Tool.sendLog(LOG, userBean, "按下【功能菜单_我的桌面】");
		if (userBean.isCheckPermission("engineering_projectState", "登记工作")) {
			myDeskDto.setEngineeringProjectList(Tool.getBackBean(ProjectStateManageBean.class).getEngineeringProjectList(EngineeringProject.TYPE_2, EngineeringProject.TYPE_7));
			myDeskDto.setEngineeringWorkInList(Tool.getBackBean(ProjectStateManageBean.class).getEngineeringProjectList(EngineeringProject.TYPE_3));
		}
		myDeskDto.setFittingPurchaseList(Tool.getBackBean(FittingPurchaseManageBean.class).getFittingPurchase("1", userBean.getUser()));
		myDeskDto.setStorageFittingPutchaseList(Tool.getBackBean(FittingPurchaseManageBean.class).getFittingPurchase("2", userBean.getUser()));
		myDeskDto.setFittingPurchaseExamineList(Tool.getBackBean(FittingPurchaseExamineManageBean.class).getFittingPurchaseExamine());
		myDeskDto.setPlanArrangementList(Tool.getBackBean(PlanArrangementManageBean.class).getRemindPlanArrangement());
		return Navigation.MY_DESK;
	}
	
	/**
	 * 根据传入的集合，验证是否需要显示
	 * @param objectList
	 * @return true 显示反之不显示
	 */
	public boolean isShowTable(List<Object> objectList) {
		boolean state = false;
		if (objectList != null && !objectList.isEmpty()) {
			state = true;
		}
		return state;
	}
	
	/**
	 * 车辆工程项目搜索更多
	 * @param type 车辆工程状态
	 * @return 工程项目管理画面
	 */
	public String selectEngineering(String type) {
		if (EngineeringProject.TYPE_2.equals(type)) {
			Tool.sendLog(LOG, userBean, "按下【我的桌面_车辆安排中的更多链接】");
		} else {
			Tool.sendLog(LOG, userBean, "按下【我的桌面_车辆工作中的更多链接】");
		}
		return Tool.getBackBean(ProjectStateManageBean.class).selectEngineeringProject(type, new MyDeskReturner(myDeskDto));
	}
	
	/**
	 * 获取车辆安排情况，lable
	 * @param type
	 * @return
	 */
	public String selectCodeLabel(SelectItem[] items,String type) {
		return GenericCodeUtil.findLabel(items, type);
	}
	
	/**
	 * 此方法绑定与查看车辆安排情况按钮
	 * @param engineeringProject
	 * @return 跳转至车辆安排详细画面
	 */
	public void selectEngineeringSituation(EngineeringProject engineeringProject) {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_车辆安排情况按钮】");
		myDeskDto.setEngineeringProject(engineeringProject);
	}
	
	/**
	 * 配件采购
	 * @param fittingPurchase
	 */
	public void purchaseFitting(FittingPurchase fittingPurchase) {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_采购按钮】");
		if (FittingPurchase.STATE_4.equals(fittingPurchase.getState())) {
			fittingPurchase.setState(FittingPurchase.STATE_5);
			Tool.getBackBean(FittingPurchaseManageBean.class).updateFittingPurchaseInfo(fittingPurchase);
		}
		myDeskDto.setFittingPurchase(fittingPurchase);
	}
	
	/**
	 * 配件采购入库
	 */
	public void purchaseStorage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_配件采购入库按钮】");
		FittingPurchase fittingPurchase = myDeskDto.getFittingPurchase();
		if (BigDecimalUtil.isNullOrZero(fittingPurchase.getPurchasePrice())) {
			fittingPurchase.setPurchasePrice(Constants.BIGDECIMAL_1_ZERO);
		}
		fittingPurchase.setState(FittingPurchase.STATE_6);
		Tool.getBackBean(FittingPurchaseManageBean.class).updateFittingPurchaseInfo(fittingPurchase);
	}
	
	/**
	 * 配件无法采购
	 */
	public void purchaseFailure() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_配件无法采购按钮】");
		FittingPurchase fittingPurchase = myDeskDto.getFittingPurchase();
		if (BigDecimalUtil.isNullOrZero(fittingPurchase.getPurchasePrice())) {
			fittingPurchase.setPurchasePrice(Constants.BIGDECIMAL_1_ZERO);
		}
		fittingPurchase.setState(FittingPurchase.STATE_9);
		Tool.getBackBean(FittingPurchaseManageBean.class).updateFittingPurchaseInfo(fittingPurchase);
	}
	
	/**
	 * 入库申请按钮
	 * @param fittingPurchase
	 */
	public void storageApply(FittingPurchase fittingPurchase) {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_入库申请按钮】");
		myDeskDto.setFittingPurchase(fittingPurchase);
		FittingStorage fittingStorage = new FittingStorage();
		fittingStorage.setCreater(userBean.getUser());
		fittingStorage.setStorageDate(DateUtil.getNowyyyymmdd());
		fittingStorage.setFittingPurchase(fittingPurchase);
		List<FittingStorageDetail> fittingStorageDetailList = new ArrayList<FittingStorageDetail>();
		List<FittingPurchaseDetail> fittingPurchaseDetailList = fittingPurchase.getFittingPurchaseDetailList();
		if (fittingPurchaseDetailList != null && !fittingPurchaseDetailList.isEmpty()) {
			for (FittingPurchaseDetail fittingPurchaseDetail : fittingPurchaseDetailList) {
				FittingStorageDetail fittingStorageDetail = new FittingStorageDetail();
				fittingStorageDetail.setFittingStorage(fittingStorage);
				fittingStorageDetail.setCarName(fittingPurchaseDetail.getCarName());
				fittingStorageDetail.setFittingBrand(fittingPurchaseDetail.getFittingBrand());
				fittingStorageDetail.setFittingName(fittingPurchaseDetail.getFittingName());
				fittingStorageDetail.setModel(fittingPurchaseDetail.getModel());
				fittingStorageDetail.setMaintenanceCubic(fittingPurchaseDetail.getMaintenanceCubic());
				fittingStorageDetail.setMaintenanceDay(fittingPurchaseDetail.getMaintenanceDay());
				fittingStorageDetail.setPrice(fittingPurchaseDetail.getPrice());
				fittingStorageDetail.setQuantity(fittingPurchaseDetail.getQuantity());
				fittingStorageDetailList.add(fittingStorageDetail);
			}
		}
		fittingStorage.setFittingStorageDetailList(fittingStorageDetailList);
		myDeskDto.setFittingStorage(fittingStorage);
	}
	
	/**
	 * 此方法绑定于配件审核管理画面的审核按钮 
	 */
	public void examine(FittingPurchaseExamine transferFittingPurchaseExamine) {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_审核按钮】");
		myDeskDto.setFittingPurchaseExamine(transferFittingPurchaseExamine);
	}
	
	/**
	 * 此方法绑定于采购单审核管理画面的审核通过按钮
	 * 实现功能：将审核提交给下一位，如果已经没有下一位就设置为审核通过
	 */
	public void examineThrough() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_审核通过按钮】");
		FittingPurchaseExamine fittingPurchaseExamine = myDeskDto.getFittingPurchaseExamine();
		fittingPurchaseExamine.setState(FittingPurchaseExamine.STATE_2);
		FittingPurchase fittingPurchase = fittingPurchaseExamine.getFittingPurchase();
		fittingPurchase.setBudgetPrice(fittingPurchase.getBudgetPrice());
		Tool.getBackBean(FittingPurchaseExamineManageBean.class).examine(fittingPurchaseExamine, fittingPurchase, true);
	}
	
	/**
	 * 此方法绑定于采购单审核管理画面审核不通过按钮
	 * 实现功能：设置审核未通过
	 */
	public void examineNotThrough() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_审核不通过按钮】");
		FittingPurchaseExamine fittingPurchaseExamine = myDeskDto.getFittingPurchaseExamine();
		fittingPurchaseExamine.setState(FittingPurchaseExamine.STATE_3);
		FittingPurchase fittingPurchase = fittingPurchaseExamine.getFittingPurchase();
		fittingPurchase.setState(FittingPurchase.STATE_3);
		Tool.getBackBean(FittingPurchaseExamineManageBean.class).examine(fittingPurchaseExamine, fittingPurchase, false);
	}
	
	/**
	 * 此方法绑定与配件采购单入库的入库按钮
	 */
	public void storage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_入库按钮】");
		Tool.getBackBean(FittingStorageManageBean.class).saveFittingStorage(myDeskDto.getFittingStorage());
	}
	
	/**
	 * 刷新桌面内容信息
	 */
	public void refreshDesk() {
		// 刷新安排中的工程信息
		if (userBean.isCheckPermission("engineering_projectState", "登记工作")) {
			myDeskDto.setEngineeringProjectList(Tool.getBackBean(ProjectStateManageBean.class).getEngineeringProjectList(EngineeringProject.TYPE_2, EngineeringProject.TYPE_7));
			myDeskDto.setEngineeringWorkInList(Tool.getBackBean(ProjectStateManageBean.class).getEngineeringProjectList(EngineeringProject.TYPE_3));
		}
	}
	
	/**
	 * 此方法绑定于我的桌面的工程项目管理
	 * @return
	 */
	public String engineeringProjectManage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_工程项目管理】");
		return Tool.getBackBean(EngineeringProjectManageBean.class).selectEngineeringProject(new MyDeskReturner(myDeskDto));
	}
	
	/**
	 * 此方法绑定于我的桌面的工程项目管理
	 * @return
	 */
	public String materialsGrantManage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_原料发放记录管理】");
		return Tool.getBackBean(MaterialsGrantManageBean.class).selectMaterialsGrant(new MyDeskReturner(myDeskDto));
	}
	
	/**
	 * 此方法绑定于我的桌面的工程项目管理
	 * @return
	 */
	public String materialsGrantProjectManage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_原料发放管理】");
		return Tool.getBackBean(MaterialsGrantManageProjectBean.class).selectMaterialsGrant(new MyDeskReturner(myDeskDto));
	}
	
	/**
	 * 此方法绑定于我的桌面的工程项目管理
	 * @return
	 */
	public String problemProjectManage() {
		Tool.sendLog(LOG, userBean, "按下【我的桌面_问题项目管理】");
		return Tool.getBackBean(ProblemProjectManageBean.class).selectMaterialsGrant(new MyDeskReturner(myDeskDto));
	}

	/**
	 * @param engineeringProjectService the engineeringProjectService to set
	 */
	public void setEngineeringProjectService(
			EngineeringProjectService engineeringProjectService) {
		this.engineeringProjectService = engineeringProjectService;
	}

	/**
	 * @param userBean the userBean to set
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	/**
	 * @return the myDeskDto
	 */
	public MyDeskDto getMyDeskDto() {
		return myDeskDto;
	}

	/**
	 * @param myDeskDto the myDeskDto to set
	 */
	public void setMyDeskDto(MyDeskDto myDeskDto) {
		this.myDeskDto = myDeskDto;
	}

}
