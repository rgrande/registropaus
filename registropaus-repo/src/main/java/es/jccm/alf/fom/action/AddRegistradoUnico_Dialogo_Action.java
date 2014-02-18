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

public class AddRegistradoUnico_Dialogo_Action extends
		ActionExecuterAbstractBase {
	
	private static Log logger = LogFactory.getLog(AddRegistradoUnico_Dialogo_Action.class);
	
	public final static String NAME = "addRegistradoUnico_Dialog";
	
	//Parametros
	public static final String PARAM_ADDREGU_NUMERO = "numRegistro";
	public static final String PARAM_ADDREGU_FECHA = "fechaRegistro";
	
	// NodeService
	protected NodeService nodeService;

	@Override
	protected void executeImpl(Action action, NodeRef nodo) {
		
		logger.info(">>>>>>>>>>> El tipo de objeto: "+action.getParameterValue(PARAM_ADDREGU_FECHA).getClass());
		logger.info(">>>>>>>>>>> El valor de fechaRegistro: "+action.getParameterValue(PARAM_ADDREGU_FECHA));
		
		//Se rellenan propiedades del aspecto
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
		//pau:numRegistro
		props.put(
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_NUMREGISTRO), 
				(String) action.getParameterValue(PARAM_ADDREGU_NUMERO));
		//pau:fechaRegistro
		props.put(
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_FECHAREGISTRO), 
				(Date) action.getParameterValue(PARAM_ADDREGU_FECHA));
		
		//Se añade el aspecto
		nodeService.addAspect(
				nodo, 
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.ASPECT_PAU_REGISTROUNICO),
				props);


	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_ADDREGU_NUMERO,
				DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_ADDREGU_NUMERO)));

		paramList.add(new ParameterDefinitionImpl(PARAM_ADDREGU_FECHA,
				DataTypeDefinition.DATE, false,
				getParamDisplayLabel(PARAM_ADDREGU_FECHA)));

	}
	
	/**
	 * @param nodeService
	 *            The NodeService to set.
	 */
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

}
