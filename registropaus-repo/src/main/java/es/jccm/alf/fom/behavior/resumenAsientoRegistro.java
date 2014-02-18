package es.jccm.alf.fom.behavior;

import java.io.Serializable;
import java.util.Map;

import es.jccm.alf.fom.model.pauModel;

import org.alfresco.repo.node.NodeServicePolicies.OnCreateNodePolicy;
import org.alfresco.repo.node.NodeServicePolicies.OnUpdateNodePolicy;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.Behaviour.NotificationFrequency;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

public class resumenAsientoRegistro implements OnUpdateNodePolicy {

	private Logger log = Logger.getLogger(resumenAsientoRegistro.class);
	
	//Dependencias
	private NodeService nodeService;
	private ContentService contentService;
	private PolicyComponent policyComponent;
	
	//Behaviours
	private Behaviour onUpdateNode;
	
	public void init() {
		
		log.info("********* Iniciando el componente");
		
		//Creación de behaviours
		this.onUpdateNode = new JavaBehaviour(this,"onUpdateNode", NotificationFrequency.TRANSACTION_COMMIT);
//		this.onUpdateNode = new JavaBehaviour(this,"onUpdateNode");

		
		this.policyComponent.bindClassBehaviour(
				QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateNode"),
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL, pauModel.TYPE_PAU_ASIENTOREGISTRAL),
				this.onUpdateNode);
	}
	
	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		log.info(">>>>>>>>>>> onUpdateNode");
		
		creaResumen(nodeRef);

	}
	
	private void creaResumen(NodeRef nodeRef) {
		
		ContentReader cr = contentService.getReader(nodeRef, QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"content"));
		
		if(cr!=null) {
			log.info(">>>>>>>>>>> Content Reader"+cr.toString());
			String content = cr.getContentString();
			
	//		String content = (String) props.get(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"content"));
			
			log.info(">>>>>>>>>>> Contenido de content: "+content.toString());
			
			nodeService.setProperty(
					nodeRef, 
					QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.PROP_RESUMENASIENTO),
					(content.length()>50?content.substring(0, 50):content)+"...");
		} else {
			log.info(">>>>>>>>>>> No hay contenido!!");
		}
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}


}
