package es.jccm.alf.fom.action;

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

import es.jccm.alf.fom.model.pauModel;

public class createPAU_Dialog extends ActionExecuterAbstractBase {

//	private static Log logger = LogFactory.getLog(createPAU_Dialog.class);
	
	public final static String NAME = "createPAU";
	
	// Form parameters
    public static final String PARAM_CREATEPAU_NOMBRE = "nombrePAU";
    public static final String PARAM_CREATEPAU_DESCRIPCION = "descripcion";
    public static final String PARAM_CREATEPAU_NUMERO = "numeroPAU";
    public static final String PARAM_CREATEPAU_LOCALIDAD = "localidad";
    public static final String PARAM_CREATEPAU_SUPERFICIE = "superficie";
    public static final String PARAM_CREATEPAU_USO = "uso";
    public static final String PARAM_CREATEPAU_NUMEROVIVIENDAS = "numeroViviendas";
	
	//NodeService
	protected NodeService nodeService;
	
	@Override
	protected void executeImpl(Action accion, NodeRef nodoOrigen) {

		// Create a map to contain the values of the properties of the node
        String name = "assoc PAU";
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
        props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_NAME), (String) accion.getParameterValue(PARAM_CREATEPAU_NOMBRE)); 
//        props.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"name"), "Nuevo PAU (cm)");
		
		//Creamos el nodo
		NodeRef node = this.nodeService.createNode(
				nodoOrigen, 
                ContentModel.ASSOC_CONTAINS, 
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, name),
                QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.TYPE_PAU_PAU), 
                props).getChildRef();  

	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		// TODO Auto-generated method stub
		
		 paramList.add(new ParameterDefinitionImpl(PARAM_CREATEPAU_NOMBRE,
	                DataTypeDefinition.TEXT, true,
	                getParamDisplayLabel(PARAM_CREATEPAU_NOMBRE)));

	}

	/**
	* @param nodeService The NodeService to set.
	*/
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
}
