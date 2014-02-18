package es.jccm.alf.fom.model;

public interface pauModel {
    
	// Namespace
	public static final String NAMESPACE_PAU_CONTENT_MODEL  = "http://www.pau.com/model/content/1.0";
	
	// Types
	public static final String TYPE_PAU_PAU = "PAU";
	public static final String TYPE_PAU__EVENTOPAU = "eventoPAU";
	public static final String TYPE_PAU_ASIENTOREGISTRAL = "asientoRegistral";
	
    // Aspects
    public static final String ASPECT_PAU_REGISTROUNICO = "registradoUnico";
    public static final String ASPECT_PAU_PUBLICADODOCM = "publicadoDOCM";
    public static final String ASPECT_PAU_TIPODOCUMENTOPAU = "tipoDocumentoPAU";
    public static final String ASPECT_PAU_ORIGENDOCUMENTO = "origenDocumento";    
    public static final String ASPECT_PAU_PUBLICADOWEB = "publicadoWeb";
    public static final String ASPECT_PAU_RELACIONES = "relaciones";
	
    
    // Properties
    //Tipo: PAU
    public static final String PROP_NAME = "name";
	public static final String PROP_NUMERO = "numeroPAU";
	public static final String PROP_LOCALIDAD = "localidadPAU";
	public static final String PROP_SUPERFICIE = "superficie";
	public static final String PROP_USO = "uso";
	public static final String PROP_NUMEROVIVIENDAS = "numeroViviendas";
	
	//Tipo: asientoRegistral
	public static final String PROP_RESUMENASIENTO = "resumenAsiento";
	
	//Aspecto: registradoUnico
	public static final String PROP_NUMREGISTRO = "numRegistro";
	public static final String PROP_FECHAREGISTRO = "fechaRegistro";
    
    // Associations
//    public static final String ASSN_ = "";
}
