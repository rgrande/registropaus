package es.jccm.alf.fom.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class sendDocumentByMail extends ActionExecuterAbstractBase {

private static Log logger = LogFactory.getLog(sendDocumentByMail.class);
	
	public final static String NAME = "sendDocumentByMail";
	
	//Parametros
	public static final String PARAM_MAIL_TO = "to";
	public static final String PARAM_MAIL_SUBJECT = "subject";
	public static final String PARAM_MAIL_BODY = "body";
	
	// NodeService
	protected NodeService nodeService;
	
	// ContentService
	protected ContentService contentService;
	
	// Global properties
	protected Properties globalproperties;
	
	@Override
	protected void executeImpl(Action action, NodeRef nodo) {
		logger.info(">>>>>>>>>>>>>>>>>>< EJECUTANDO LA ACCIÓN DE ENVIO DE MAIL");
		
		//Se comprueba que exista el nodo
		if (nodeService.exists(nodo) == true) {
			// Se rescatan los valores de las variables del formulario de Share
	        String to = (String) action.getParameterValue(PARAM_MAIL_TO);
	        String subject = (String) action.getParameterValue(PARAM_MAIL_SUBJECT);
	        String body = (String) action.getParameterValue(PARAM_MAIL_BODY);
logger.info(">>>>>>>>>>>>>>>>>> 1");
			// Se obtiene el nombre del documento
			Serializable filename = nodeService.getProperty(nodo, ContentModel.PROP_NAME);
			if (filename == null) {
			    throw new AlfrescoRuntimeException("Document filename is null");
			}
			String documentName = (String) filename;
logger.info(">>>>>>>>>>>>>>>>>> 2");			
			try {
				// Se crea la sesión de mail
                Properties props = new Properties();
                props.put("mail.smtp.host", globalproperties.get("mail.host"));
                props.put("mail.smtp.port", globalproperties.get("mail.port"));
                props.put("mail.smtp.user", globalproperties.get("mail.username"));
                props.put("mail.smtp.password", globalproperties.get("mail.password"));
                props.put("mail.smtp.starttls.enable", globalproperties.get("mail.smtp.starttls.enable"));
                props.put("mail.smtp.starttls.required", "true");
                props.put("mail.smtp.auth", globalproperties.get("mail.smtp.auth"));
                
                props.put("mail.smtp.socketFactory.fallback", "false");
//                props.put("mail.smtp.socketFactory.class","utils.DummySSLSocketFactory");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.quitwait", "false");
/*
 * Hay que definir una conexión correcta
 */
//                Session session = Session.getDefaultInstance(props, null);
                Session session = Session.getDefaultInstance(props);
                session.setDebug(true); //<-- Esto hay que cambiarlo a false cuando todo funcione
logger.info(">>>>>>>>>>>>>>>>>> 3");
                // Se define el mensaje
                Message message = new MimeMessage(session);
                String fromAddress = "paus@netrga.es";
                message.setFrom(new InternetAddress(fromAddress));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject(subject);
logger.info(">>>>>>>>>>>>>>>>>> 4");
                // Se crea la parte del mensaje con el body
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(body);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
logger.info(">>>>>>>>>>>>>>>>>> 5");
                // Creación de la parte del adjunto

                // Se consiguen los bytes del contenido
                byte[] documentData = getDocumentContentBytes(nodo, documentName);
                if (documentData == null) {
                    throw new AlfrescoRuntimeException("Document content is null");
                }
logger.info(">>>>>>>>>>>>>>>>>> 6");
                // Se adjunta el documento
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(
                        documentData, new MimetypesFileTypeMap().getContentType(documentName))));
                messageBodyPart.setFileName(documentName);
                multipart.addBodyPart(messageBodyPart);
logger.info(">>>>>>>>>>>>>>>>>> 7");
                // Se colocan las partes en el mensaje
                message.setContent(multipart);

                // Se envia el mensaje
                Transport.send(message);
logger.info(">>>>>>>>>>>>>>>>>> 8");
                // Set status on node as "sent via email"
                Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
                properties.put(ContentModel.PROP_ORIGINATOR, fromAddress);
                properties.put(ContentModel.PROP_ADDRESSEE, to);
                properties.put(ContentModel.PROP_SUBJECT, subject);
                properties.put(ContentModel.PROP_SENTDATE, new Date());
                nodeService.addAspect(nodo, ContentModel.ASPECT_EMAILED, properties);
logger.info(">>>>>>>>>>>>>>>>>> 9");
                
			} catch (MessagingException me) {
                throw new AlfrescoRuntimeException("Could not send email: " + me.getMessage());
            }
		}
		
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_MAIL_TO,
				DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_MAIL_TO)));
		paramList.add(new ParameterDefinitionImpl(PARAM_MAIL_SUBJECT,
				DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_MAIL_SUBJECT)));
		paramList.add(new ParameterDefinitionImpl(PARAM_MAIL_BODY,
				DataTypeDefinition.TEXT, true,
				getParamDisplayLabel(PARAM_MAIL_BODY)));
		
	}
	
	/**
     * Get the content bytes for the document with passed in node reference.
     *
     * @param documentRef      the node reference for the document we want the content bytes for
     * @param documentFilename document filename for logging
     * @return a byte array containing the document content or null if not found
     */
    public byte[] getDocumentContentBytes(NodeRef documentRef, String documentFilename) {
        // Get a content reader
        ContentReader contentReader = contentService.getReader(documentRef, ContentModel.PROP_CONTENT);
        if (contentReader == null) {
            logger.error("Could not send document with email, content reader was null [filename=" +
                    documentFilename + "][docNodeRef=" + documentRef + "]");

            return null;
        }

        // Get the document content bytes
        InputStream is = contentReader.getContentInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] documentData = null;

        try {
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            documentData = bos.toByteArray();
        } catch (IOException ioe) {
            logger.error("Content could not be read: " + ioe.getMessage() +
                    " [filename=" + documentFilename + "][docNodeRef=" + documentRef + "]");
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable e) {
                    logger.error("Could not close doc content input stream: " + e.getMessage() +
                            " [filename=" + documentFilename + "][docNodeRef=" + documentRef + "]");
                }
            }
        }

        return documentData;
    }

	public static void setLogger(Log logger) {
		sendDocumentByMail.logger = logger;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setGlobalproperties(Properties globalproperties) {
		this.globalproperties = globalproperties;
	}

}
