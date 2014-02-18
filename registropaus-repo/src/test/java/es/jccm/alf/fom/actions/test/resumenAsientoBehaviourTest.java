package es.jccm.alf.fom.actions.test;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

import es.jccm.alf.fom.model.pauModel;

@RunWith(RemoteTestRunner.class)
@Remote(runnerClass=SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class resumenAsientoBehaviourTest {

	private static final String ADMIN_USER_NAME = "admin";
	
    static Logger log = Logger.getLogger(resumenAsientoBehaviourTest.class);

    @Autowired
    @Qualifier("NodeService")
    protected NodeService nodeService;
    
    @Autowired
    @Qualifier("ContentService")
    protected ContentService contentService;
    
    @Autowired
    @Qualifier("nodeLocatorService")
    protected NodeLocatorService nodeLocatorService;

	
	@Test
	public void test() {
		log.log(Level.INFO, ">>>>>>>>>>> Test de Behaviour");
		
		AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
		
		NodeRef companyHome = nodeLocatorService.getNode(CompanyHomeNodeLocator.NAME, null, null);
		
		// Se crea un mapa que contiene las propiedades del nodo
		Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
		//cm:name
		props.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,
				"name"), "AsientoTest");
		ContentData ct = ContentData.createContentProperty("Contenido Test");
		props.put(QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,
				"content"), ct);

		
		NodeRef node = nodeService.createNode(
				companyHome, 
				ContentModel.ASSOC_CONTAINS, 
				QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"Asiento Test"), 
				QName.createQName(pauModel.NAMESPACE_PAU_CONTENT_MODEL,pauModel.TYPE_PAU_ASIENTOREGISTRAL),
				props).getChildRef();

	
		log.info(">>>>>>>>>>> Node creado: "+node.toString());
		
		//Se modifica el contenido
		ContentWriter writer = contentService.getWriter(node, ContentModel.PROP_CONTENT, true);
		writer.putContent("Esto es un contenido de prueba");
	
		nodeService.deleteNode(node);
		log.info(">>>>>>>>>>> Nodo eliminado");
	}
	
	

}
