package com.qylm.bean.engineeringMaterials;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.qylm.bean.BasePagingBean;
import com.qylm.bean.UserBean;
import com.qylm.bean.engineering.EngineeringProjectManageBean;
import com.qylm.bean.engineering.ProjectStateCreateBean;
import com.qylm.bean.returner.Returner;
import com.qylm.bean.returner.engineering.ProjectStateManageReturner;
import com.qylm.bean.returner.engineeringMaterials.MaterialsGrantManageReturner;
import com.qylm.common.Message;
import com.qylm.common.MothedUtil;
import com.qylm.common.Navigation;
import com.qylm.common.Tool;
import com.qylm.common.utils.StringUtil;
import com.qylm.dto.engineering.EngineeringProjectManageDto;
import com.qylm.dto.engineeringMaterials.MaterialsGrantCreateDto;
import com.qylm.dto.engineeringMaterials.MaterialsGrantManageDto;
import com.qylm.entity.EngineeringProject;
import com.qylm.entity.EngineeringProjectDetail;
import com.qylm.entity.MaterialsGrant;
import com.qylm.service.MaterialsGrantService;

/**
 * 原料发放记录管理
 * @author qylm
 */
@ManagedBean
@RequestScoped
public class MaterialsGrantManageBean extends BasePagingBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2913350013622941075L;

	/**
	 * LOG
	 */
	private static final Log LOG = LogFactory.getLog(MaterialsGrantManageBean.class);
	
	/**
	 * 保存原料发放记录管理画面需要保存的值
	 */
	private MaterialsGrantManageDto materialsGrantManageDto = new MaterialsGrantManageDto();

	/**
	 * 原料发放记录列表（检索结果）
	 */
	private List<MaterialsGrant> materialsGrantList;
	
	/**
	 * 删除复选框选择的值
	 */
	private MaterialsGrant[] selectedModels;

	/**
	 * 原料发放记录管理bean
	 */
	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	/**
	 * 原料发放记录管理业务类
	 */
	@ManagedProperty(value="#{materialsGrantService}")
	private MaterialsGrantService materialsGrantService;

	/**
	 * 查询出所有原料发放记录列表
	 * 
	 * @return 原料发放记录管理画面
	 */
	public String getAll() {
		Tool.sendLog(LOG, userBean, "按下【功能菜单_原料发放记录管理按钮】");
		fetchData(0, true);
		return Navigation.MATERIALS_GRANT_MANAGE;
	}

	/**
	 * 此方法绑定于原料发放记录管理画面的新建按钮 
	 * 实现功能：跳转至原料发放记录登陆画面，新建一家原料发放记录
	 * @return 原料发放记录登陆画面
	 */
	public String newMaterialsGrant() {
		Tool.sendLog(LOG, userBean, "按下【原料发放记录管理画面_新建按钮】");
		return Tool.getBackBean(MaterialsGrantCreateBean.class).newCreate(new MaterialsGrantManageReturner(materialsGrantManageDto, currentPage));
	}

	/**
	 * 此方法绑定于原料发放记录管理画面的修改按钮 
	 * 实现功能：根据修改的对象，跳转至原料发放记录登陆画面
	 * @return 原料发放记录登陆画面
	 */
	public String updateMaterialsGrant(MaterialsGrant transferMaterialsGrant) {
		Tool.sendLog(LOG, userBean, "按下【原料发放记录管理画面_修改按钮】");
		return Tool.getBackBean(MaterialsGrantCreateBean.class).updateDetail(new MaterialsGrantManageReturner(materialsGrantManageDto, currentPage), transferMaterialsGrant);
	}
	
	/**
	 * 此方法绑定于原料发放记录管理画面的检索按钮 
	 * 实现功能：根据检索条件，检索出原料发放记录
	 * @return 画面不跳转
	 */
	public void selectMaterialsGrant() {
		Tool.sendLog(LOG, userBean, "按下【原料发放记录管理画面_检索按钮】");
		fetchData(0, true);
	}
	
	/**
	 * 绑定于原料发放记录管理画面的全选删除按钮
	 * @param event
	 */
	public void deleteMul(ActionEvent event) {
		Tool.sendLog(LOG, userBean, "按下【原料发放记录管理画面_批量删除按钮】");
		if (selectedModels != null) {
			List<MaterialsGrant> asList = Arrays.asList(selectedModels);
			materialsGrantList.removeAll(asList);
			materialsGrantService.deleteEntityAll(asList);
			removeData(1, materialsGrantList.isEmpty());
			Tool.sendErrorMessage(Message.GENERAL_DELETESUCCESS);
		}
	}

	/**
	 * 此方法绑定于原料发放记录管理画面的删除按钮 
	 * 实现功能：先移除集合内的数据，在移除数据库内的数据
	 * @return 画面不跳转
	 */
	public void deleteMaterialsGrant(MaterialsGrant transferMaterialsGrant) {
		Tool.sendLog(LOG, userBean, "按下【原料发放记录管理画面的_删除按钮】");
		materialsGrantList.remove(transferMaterialsGrant);
		materialsGrantService.deleteEntity(transferMaterialsGrant);
		removeData(1, materialsGrantList.isEmpty());
		Tool.sendErrorMessage(Message.GENERAL_DELETESUCCESS);
	}
	
	@Override
	protected void fetchData(int start, boolean needInit) {
		DetachedCriteria detachedCriteria = getDetached();
		materialsGrantList = materialsGrantService.getByDetachedCriteria(detachedCriteria, start, onePageCount);
		detachedCriteria.createAlias("engineeringProjectDetail.vehicleInfo", "engineeringProjectDetail.vehicleInfo", JoinType.LEFT_OUTER_JOIN);
		if (needInit) {
			init(materialsGrantService.getRowCount(detachedCriteria));
		}
	}

	/**
	 * 需要返回本画面时都调用此共通方法
	 * @param currentPage 当前页数
	 * @return 个人原料发放记录管理画面
	 */
	public String back(int currentPage) {
		reflushCurrentPage(currentPage);
		return Navigation.MATERIALS_GRANT_MANAGE;
	}
	
	/**
	 * 此方法绑定于项目管理画面的修改按钮 
	 * 实现功能：根据修改的对象，跳转至工作登记登陆画面
	 * @return 工作登记登陆画面
	 */
	public String updateEngineeringProject(EngineeringProject transferEngineeringProject) {
		Tool.sendLog(LOG, userBean, "按下【工作登记管理画面_详细按钮】");
		return "MATERIALS_GRANT_CREATE";
	}
	
	/**
	 * 共通检索条件
	 * @return
	 */
	private DetachedCriteria getDetached() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(MaterialsGrant.class);
		detachedCriteria.createAlias("employee", "employee", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("engineeringProjectDetail", "engineeringProjectDetail", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("engineeringProjectDetail.engineeringProject", "engineeringProjectDetail.engineeringProject", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.addOrder(Order.desc(MaterialsGrant.CREATE_DATE));
		MothedUtil.getBelongingUser(detachedCriteria, userBean.getUser());
//		String vehicleNumber = materialsGrantManageDto.getVehicleNumber();
//		String customerName = materialsGrantManageDto.getCustomerName();
//		Date beginDate = materialsGrantManageDto.getStartBeginDate();
//		Date endDate = materialsGrantManageDto.getEndBeginDate();
//		String type = materialsGrantManageDto.getType();
//		if (StringUtil.isNotBlank(vehicleNumber)) {
//			DetachedCriteria criteria = DetachedCriteria.forClass(EngineeringProjectDetail.class);
//			criteria.createAlias(EngineeringProjectDetail.ENGINEERING_PROJECT, EngineeringProjectDetail.ENGINEERING_PROJECT, JoinType.LEFT_OUTER_JOIN);
//			criteria.createAlias(EngineeringProjectDetail.VEHICLE_INFOL, EngineeringProjectDetail.VEHICLE_INFOL, JoinType.LEFT_OUTER_JOIN);
//			criteria.add(Restrictions.like(EngineeringProjectDetail.VEHICLE_INFOL_NUMBER, vehicleNumber, MatchMode.ANYWHERE));
//			criteria.setProjection(Property.forName(EngineeringProjectDetail.ENGINEERING_PROJECT_ID));
//			detachedCriteria.add(Property.forName(EngineeringProject.BASE_ID).in(criteria));
//		}
//		if (StringUtil.isNotBlank(customerName)) {
//			detachedCriteria.add(Restrictions.like(EngineeringProject.CUSTOMER_NAME, customerName, MatchMode.ANYWHERE));
//		}
//		if (beginDate != null) {
//			detachedCriteria.add(Restrictions.ge(EngineeringProject.BEGIN_DATE, beginDate));
//		}
//		if (endDate != null) {
//			detachedCriteria.add(Restrictions.le(EngineeringProject.BEGIN_DATE, endDate));
//		}
//		if (startEndDate != null) {
//			detachedCriteria.add(Restrictions.ge(EngineeringProject.END_DATE, startEndDate));
//		}
//		if (endEndDate != null) {
//			detachedCriteria.add(Restrictions.le(EngineeringProject.END_DATE, endEndDate));
//		}
//		if (StringUtil.isNotBlank(constructionName)) {
//			detachedCriteria.add(Restrictions.like(EngineeringProject.CONSTRUCTION_NAME, constructionName, MatchMode.ANYWHERE));
//		}
//		if (StringUtil.isNotBlank(workAddress)) {
//			detachedCriteria.add(Restrictions.like(EngineeringProject.WORK_ADDRESS, workAddress, MatchMode.ANYWHERE));
//		}
//		if (!StringUtil.isUnSelected(type)) {
//			detachedCriteria.add(Restrictions.eq(EngineeringProject.TYPE, type));
//		}
		return detachedCriteria;
	}
	
	/**
	 * 此方法绑定于工程项目管理画面的检索按钮 
	 * 实现功能：根据检索条件，检索出工程项目
	 * @return 画面不跳转
	 */
	public String selectMaterialsGrant(Returner<?, ?, ?> returner) {
		Tool.sendLog(LOG, userBean, "按下【工程项目管理画面_检索按钮】");
		fetchData(0, true);
		return Navigation.MATERIALS_GRANT_MANAGE;
	}
	
	/**
	 * set materialsGrantService
	 * @param materialsGrantService the materialsGrantService to set
	 */
	public void setMaterialsGrantService(MaterialsGrantService materialsGrantService) {
		this.materialsGrantService = materialsGrantService;
	}

	/**
	 * set userBean
	 * @param userBean the userBean to set
	 */
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	/**
	 * get materialsGrantManageDto
	 * @return the materialsGrantManageDto
	 */
	public MaterialsGrantManageDto getMaterialsGrantManageDto() {
		return materialsGrantManageDto;
	}

	/**
	 * set materialsGrantManageDto
	 * @param materialsGrantManageDto the materialsGrantManageDto to set
	 */
	public void setMaterialsGrantManageDto(MaterialsGrantManageDto materialsGrantManageDto) {
		this.materialsGrantManageDto = materialsGrantManageDto;
	}

	/**
	 * get materialsGrantList
	 * @return the materialsGrantList
	 */
	public List<MaterialsGrant> getMaterialsGrantList() {
		return materialsGrantList;
	}

	/**
	 * set materialsGrantList
	 * @param materialsGrantList the materialsGrantList to set
	 */
	public void setMaterialsGrantList(List<MaterialsGrant> materialsGrantList) {
		this.materialsGrantList = materialsGrantList;
	}

	/**
	 * get selectedModels
	 * @return the selectedModels
	 */
	public MaterialsGrant[] getSelectedModels() {
		return selectedModels;
	}

	/**
	 * set selectedModels
	 * @param selectedModels the selectedModels to set
	 */
	public void setSelectedModels(MaterialsGrant[] selectedModels) {
		this.selectedModels = selectedModels;
	}

}
