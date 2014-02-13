package es.jccm.alf.fom.actions.test;

import static org.junit.Assert.*;

import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
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

import es.jccm.alf.fom.action.createPAU;

@RunWith(RemoteTestRunner.class)
@Remote(runnerClass=SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class createPAUActionTest {

	private static final String ADMIN_USER_NAME = "admin";
	
    static Logger log = Logger.getLogger(createPAUActionTest.class);

    @Autowired
    @Qualifier("NodeService")
    protected NodeService nodeService;

    @Autowired
    @Qualifier("ActionService")
    protected ActionService actionService;
    
    @Autowired
    @Qualifier("nodeLocatorService")
    protected NodeLocatorService nodeLocatorService;
    
    @Test
    public void testGetAction() {
    	log.log(Level.INFO, "Primer test");
    	AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
    	Action action = actionService.createAction(createPAU.NAME);
    	assertNotNull(action);
    }
    
    @Test
    public void testExecuteAction() {
    	log.log(Level.INFO, "Segundo test");
    	AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
        NodeRef companyHome = nodeLocatorService.getNode(CompanyHomeNodeLocator.NAME, null, null);
        
    	Action action = actionService.createAction("createPAU_Action");
    	actionService.executeAction(action, companyHome);
    }
}
