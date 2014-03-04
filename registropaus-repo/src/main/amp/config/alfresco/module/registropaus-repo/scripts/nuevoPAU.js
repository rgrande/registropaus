/*
 * nuevoSitio.js
 */
//ejemplo: http://forums.alfresco.com/forum/end-user-discussions/alfresco-share/creation-site-space-template-02232012-1648
// execute onCreate of a Site
//var site = document;
var childAssoc = behaviour.args[0];
var pau = childAssoc.getChild();

logger.log("### pau:" + pau);
logger.log("### childAssoc:" + childAssoc);
logger.log("### childAssoc.name:" + childAssoc.name);
logger.log("### childAssoc.qname:" + childAssoc.qnamePath);

logger.log("### Prepping to get Templates folder");

var templates = search.luceneSearch('PATH:"/app:company_home/app:dictionary/app:space_templates/cm:PAU_Plantilla"');

//var docLib = pau.childByNamePath("documentLibrary");

//if (!docLib) {

//	docLib = pau.createFolder("documentLibrary");

//}

//if (docLib) {

	var pautemp = templates[0];

	// var child = templates.children[0];

if(pautemp) {
	logger.log("Plantilla encontrada:"+pautemp.children[0].name);

	for (t in pautemp.children) {
		logger.log("-" + pautemp.children[t].name);
		var plant = pautemp.children[t];

		plant.copy(pau, true);

		for (s in plant.children) {
			logger.log("--" + plant.children[s].name);
		}

	}
}
//} // end if
