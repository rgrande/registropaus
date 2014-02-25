package es.jccm.alf.fom.action;

import java.util.Date;
import java.util.List;

import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PermissionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.jccm.alf.fom.model.pauGroups;

public class PermisosGruposAction extends ActionExecuterAbstractBase {

	private static Log logger = LogFactory.getLog(PermisosGruposAction.class);
	
	public final static String NAME = "PermisosAction";
	
	//Parametros
	public static final String PARAM_PERM_AB = "permisoAB";
	public static final String PARAM_PERM_GU = "permisoGU";
	public static final String PARAM_PERM_CR = "permisoCR";
	public static final String PARAM_PERM_CU = "permisoCU";
	public static final String PARAM_PERM_TO = "permisoTO";
	
	//permissionService
	protected PermissionService permissionService;
	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		
		logger.info("Ejecutando acción de PermisosGrupos");
		
		permissionService.setPermission(
				actionedUponNodeRef, 
				PermissionService.GROUP_PREFIX+pauGroups.GROUP_AB, 
				PermissionService.READ, 
				(Boolean) action.getParameterValue(PARAM_PERM_AB));
		
		permissionService.setPermission(
				actionedUponNodeRef, 
				PermissionService.GROUP_PREFIX+pauGroups.GROUP_GU, 
				PermissionService.READ, 
				(Boolean) action.getParameterValue(PARAM_PERM_GU));

		permissionService.setPermission(
				actionedUponNodeRef, 
				PermissionService.GROUP_PREFIX+pauGroups.GROUP_CR, 
				PermissionService.READ, 
				(Boolean) action.getParameterValue(PARAM_PERM_CR));

		permissionService.setPermission(
				actionedUponNodeRef, 
				PermissionService.GROUP_PREFIX+pauGroups.GROUP_CU, 
				PermissionService.READ, 
				(Boolean) action.getParameterValue(PARAM_PERM_CU));

		permissionService.setPermission(
				actionedUponNodeRef, 
				PermissionService.GROUP_PREFIX+pauGroups.GROUP_TO, 
				PermissionService.READ, 
				(Boolean) action.getParameterValue(PARAM_PERM_TO));

		
		
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_PERM_AB,
				DataTypeDefinition.BOOLEAN, true,
				getParamDisplayLabel(PARAM_PERM_AB)));
		paramList.add(new ParameterDefinitionImpl(PARAM_PERM_GU,
				DataTypeDefinition.BOOLEAN, true,
				getParamDisplayLabel(PARAM_PERM_GU)));
		paramList.add(new ParameterDefinitionImpl(PARAM_PERM_CR,
				DataTypeDefinition.BOOLEAN, true,
				getParamDisplayLabel(PARAM_PERM_CR)));
		paramList.add(new ParameterDefinitionImpl(PARAM_PERM_CU,
				DataTypeDefinition.BOOLEAN, true,
				getParamDisplayLabel(PARAM_PERM_CU)));
		paramList.add(new ParameterDefinitionImpl(PARAM_PERM_TO,
				DataTypeDefinition.BOOLEAN, true,
				getParamDisplayLabel(PARAM_PERM_TO)));

	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
