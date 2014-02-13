package es.jccm.alf.fom.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

import es.jccm.alf.fom.model.pauModel;

public class createPAU extends ActionExecuterAbstractBase {

	public final static String NAME = "createPAU";
	
	//NodeService
	protected NodeService nodeService;
	
	@Override
	protected void executeImpl(Action Accion, NodeRef nodoOrigen) {

		// Create a map to contain the values of the properties of the node
        String name = "assoc PAU";
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
        props.put(QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_NAME), "Nuevo PAU (prop especifica)"); 
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
	protected void addParameterDefinitions(List<ParameterDefinition> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	* @param nodeService The NodeService to set.
	*/
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
}
