package es.jccm.alf.fom.action;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.jccm.alf.fom.model.pauModel;


/**
 * @author roman
 * Esta acción añade o quita el aspecto publicadoWeb a un nodo.
 * Recibe como parámetro la fecha de fin de publicación.
 */
public class PublicadoWebAction extends ActionExecuterAbstractBase {

	private static Log logger = LogFactory.getLog(PublicadoWebAction.class);
	
	public final static String NAME = "ControlAspectosAction";
	
	//Parametros
	public static final String PARAM_ASP_FECHAFIN = "fechaFin";
	
	// NodeService
	protected NodeService nodeService;
	
	// operación a realizar
	private String operation; //Los valores que tomará son: add o remove. Por defecto siempre es remove
	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		
		//Añadir aspecto
		if (operation.equals("add")) {
			
			//Propiedades pau:publicadoWeb
			Map<QName, Serializable> props_publicadoWeb = new HashMap<QName, Serializable>(1);
			//pau:fechaFin
			props_publicadoWeb.put(
					QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_FECHAFIN), 
					(Date) action.getParameterValue(PARAM_ASP_FECHAFIN));
			
			nodeService.addAspect(
					actionedUponNodeRef, 
					QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL, pauModel.ASPECT_PAU_PUBLICADOWEB), 
					props_publicadoWeb);
		//Eliminar aspecto
		} else {
			if(nodeService.hasAspect(
					actionedUponNodeRef, 
					QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL, pauModel.ASPECT_PAU_PUBLICADOWEB))) {
				
				nodeService.removeAspect(
						actionedUponNodeRef, 
						QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL, pauModel.ASPECT_PAU_PUBLICADOWEB));
			}
		}
		

	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_ASP_FECHAFIN,
				DataTypeDefinition.DATETIME, false,
				getParamDisplayLabel(PARAM_ASP_FECHAFIN)));

	}

	public static void setLogger(Log logger) {
		PublicadoWebAction.logger = logger;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
