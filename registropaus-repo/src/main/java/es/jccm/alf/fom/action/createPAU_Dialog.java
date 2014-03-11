package es.jccm.alf.fom.action;

import es.jccm.alf.fom.model.pauModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
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

public class createPAU_Dialog extends ActionExecuterAbstractBase {

	private static Log logger = LogFactory.getLog(createPAU_Dialog.class);

	public final static String NAME = "createPAU";

	// Form parameters
	public static final String PARAM_CREATEPAU_NOMBRE = "nombrePAU";
	public static final String PARAM_CREATEPAU_DESCRIPCION = "descripcion";
	public static final String PARAM_CREATEPAU_NUMERO = "numeroPAU";
	public static final String PARAM_CREATEPAU_LOCALIDAD = "localidad";
	public static final String PARAM_CREATEPAU_SUPERFICIE = "superficie";
	public static final String PARAM_CREATEPAU_USO = "uso";
	public static final String PARAM_CREATEPAU_NUMEROVIVIENDAS = "numeroViviendas";

	// NodeService
	protected NodeService nodeService;

	@Override
	protected void executeImpl(Action accion, NodeRef nodoOrigen) {

		logger.info("Param nombre: "+(String) accion.getParameterValue(PARAM_CREATEPAU_NOMBRE));
		
		// Se crea un mapa que contiene las propiedades del nodo
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
		// cm:name	
		props.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,	"name"), 
				(String) accion.getParameterValue(PARAM_CREATEPAU_NOMBRE));
		// pau:numeroPAU
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_NUMERO), 
				(Integer) accion.getParameterValue(PARAM_CREATEPAU_NUMERO));
		// pau:localidad
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_LOCALIDAD), 
				(String) accion.getParameterValue(PARAM_CREATEPAU_LOCALIDAD));
		// pau:superficie
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_SUPERFICIE), 
				(Float) accion.getParameterValue(PARAM_CREATEPAU_SUPERFICIE));
		// pau:uso
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_USO), 
				(String) accion.getParameterValue(PARAM_CREATEPAU_USO));
		// pau:numeroViviendas
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_NUMEROVIVIENDAS), 
				(Integer) accion.getParameterValue(PARAM_CREATEPAU_NUMEROVIVIENDAS));
		//pau:estado
		props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_ESTADO), 
				(String)"Inicio");
		
		
		// Se crea el nodo
		NodeRef node = this.nodeService.createNode(
				nodoOrigen,
				ContentModel.ASSOC_CONTAINS,
				QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,(String) accion.getParameterValue(PARAM_CREATEPAU_NOMBRE)),
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.TYPE_PAU_PAU), 
				props).getChildRef();

		
		//Aspecto cm:title
		Map<QName, Serializable> props_titled = new HashMap<QName, Serializable>(1);

		props_titled.put(
				QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"title"), 
				(String) accion.getParameterValue(PARAM_CREATEPAU_DESCRIPCION));
		this.nodeService.addAspect(node, QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"titled"), props_titled);
		
		//Aspecto cm:geographic
		Map<QName, Serializable> props_geo = new HashMap<QName, Serializable>(1);
		props_geo.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"latitude"),new Double(39.866667));
		props_geo.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"longitude"),new Double(-4.033333));
		this.nodeService.addAspect(node, QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"geographic"), props_geo);
		
		//Se retorna el valor del nodeRef
		accion.setParameterValue(PARAM_RESULT, node);
		
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {

		logger.info("Llamada a addParameterDefinitions");
		
		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_NOMBRE,
				DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_CREATEPAU_NOMBRE)));
		
		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_DESCRIPCION,
				DataTypeDefinition.TEXT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_DESCRIPCION)));
/*
 * Sólo vamos a trabajar los parámetros Nombre y Descripción porque observ que si se le pasan todos los parámetros 
 * es obligatorio rellenarlos 
 * TODO: Mirar por qué ocurre esto
 * 
 */
		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_NUMERO,
				DataTypeDefinition.INT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_NUMERO)));

		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_LOCALIDAD,
				DataTypeDefinition.TEXT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_LOCALIDAD)));

		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_SUPERFICIE,
				DataTypeDefinition.FLOAT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_SUPERFICIE)));

		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_USO,
				DataTypeDefinition.TEXT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_USO)));

		paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_NUMEROVIVIENDAS, 
				DataTypeDefinition.INT, false,
				getParamDisplayLabel(PARAM_CREATEPAU_NUMEROVIVIENDAS)));

		logger.info("FIN Llamada a addParameterDefinitions");
		
	}
	
	/**
	 * Retorna el valor en caso en que sea distinto de nulo
	 */
	private Serializable getIfNotNull(Serializable value) {
		return null;
	}
	
	

	/**
	 * @param nodeService
	 *            The NodeService to set.
	 */
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

}
