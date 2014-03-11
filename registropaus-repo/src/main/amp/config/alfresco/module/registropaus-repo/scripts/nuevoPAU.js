/*
 * nuevoSitio.js
 */
var childAssoc = behaviour.args[0];
//En esta variable se encuentra el pau creado
var pau = childAssoc.getChild();

logger.log("### pau:" + pau);
logger.log("### childAssoc:" + childAssoc);
logger.log("### childAssoc.name:" + childAssoc.name);
logger.log("### childAssoc.qname:" + childAssoc.qnamePath);

logger.log("### Prepping to get Templates folder");

/*
 * Copia de la plantilla
 */
var templates = search.luceneSearch('PATH:"/app:company_home/app:dictionary/app:space_templates/cm:PAU_Plantilla"');
var pautemp = templates[0];

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

/*
 * Creación de asiento registral asociado
 * 
*/
var libror =  pau.parent.parent.childByNamePath("Libro de Registro");

if(libror) {
	var asiento = libror.createNode("Nuevo_PAU_"+pau.name,"pau:asientoRegistral");
	asiento.content = "Inscripción de PAU "+pau.name;
	asiento.properties["pau:tipoAsiento"] = "Inscripción";
	var fecha = new Date();
	asiento.properties["{http://www.pau.com/model/content/1.0}fechaAsiento"] = fecha;
	asiento.createAssociation(pau,"{http://www.pau.com/model/content/1.0}pauAsociadoAsiento");
	asiento.save();
}