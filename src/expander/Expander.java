package expander;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.TreeMap;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.Expand;

public class Expander {
	static ProvFactory pFactory = InteropFramework.newXMLProvFactory();
	static Namespace ns = new Namespace();
	public static InteropFramework intFr = new InteropFramework();
	static TreeMap<String, Document> fullTemplateName_SchemaDocument = new TreeMap<String, Document>();
	static TreeMap<String, String> operator_fullTemplateName = new TreeMap<String, String>();
	static TreeMap<String, Document> bindings = new TreeMap<String, Document>();

	public static void main(String[] args) {

		String templatesDirectory = "templates";
		String bindingsDirectory = "bindings";
		loadNamespace();
		createOperator_fullTemplateNameMap();
		listf(templatesDirectory, ProvFormat.PROVN);
		try {

			File directory = new File("src/" + bindingsDirectory);// directoryURL.toString());
			if (directory.listFiles() != null) {
				File[] fList = directory.listFiles();
				// Each File corresponds to a binding file
				for (File file : fList) {
					Document dBin = intFr.readDocumentFromFile(file.getAbsolutePath(), ProvFormat.TURTLE);
					String operatorName = getBindingOperatorName(file.getName());
					String templateName = operator_fullTemplateName.get(operatorName);
					Bindings b = Bindings.fromDocument(dBin, pFactory);
					expand(templateName, bindingsDirectory, bindings, fullTemplateName_SchemaDocument, file.getName(),
							b);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void createOperator_fullTemplateNameMap() {
		operator_fullTemplateName.put("Insert", "DMOP1-InsertRowPROVTemplate");
		operator_fullTemplateName.put("InsertSelect", "DMOP1b-InsertRowWithSelectPROVTemplate");
		operator_fullTemplateName.put("Delete", "DMOP2-DeleteRowPROVTemplate");
		operator_fullTemplateName.put("AddPrimaryKey", "ICMOP1-ADDPrimaryKeyPROVTemplate");
		operator_fullTemplateName.put("AddPrimaryKeyWithDMO", "ICMOP1b-ADDPrimaryKeyWithDMOPROVTemplate");
		operator_fullTemplateName.put("CreateTable", "SMOP1-ADDTablePROVTemplate");
		operator_fullTemplateName.put("DropTable", "SMOP2-DropTablePROVTemplate");
		operator_fullTemplateName.put("AddColumn", "SMOP3-ADDColumnPROVTemplate");
		operator_fullTemplateName.put("CopyTable", "SMOP4-CopyTablePROVTemplate");
		operator_fullTemplateName.put("MergeTable", "SMOP5-MergeTablePROVTemplate");
		operator_fullTemplateName.put("DecomposeTable", "SMOP6-DecomposeTablePROVTemplate");
		operator_fullTemplateName.put("RenameTable", "SMOP7-RenameTablePROVTemplate");
	}

	private static void loadNamespace() {
		ns.register("exe", "http://example.org/");
		ns.register("tmpl", "http://openprovenance.org/tmpl#");
		ns.register("var", "http://openprovenance.org/var#");
		ns.register("prov", "http://www.w3.org/ns/prov#");
		ns.register("o2p", "http://uml2prov.unirioja.es/ns/o2p#");
		ns.register("d2p", "http://uml2prov.unirioja.es/ns/d2p#");
		ns.register("sch2p", "http://uml2prov.unirioja.es/ns/sch2p#");
		ns.register("bitemp", "http://uml2prov.unirioja.es/ns/bitemp#");
		ns.register("xsd", "http://www.w3.org/2001/XMLSchema#");

	}

	private synchronized static void expand(String nameTemplate, String nameBinding,
			TreeMap<String, Document> classBindings, TreeMap<String, Document> classTemplates,
			String expandedDocumentName, Bindings b) throws FileNotFoundException {

		Document docTemplate = classTemplates.get(nameTemplate);
		docTemplate.setNamespace(ns);
		URL directoryURL = Expander.class.getResource("..");
		new File(directoryURL + "expandedDocuments").mkdir();

		if (docTemplate != null) {
			Expand expand = new Expand(pFactory, false, true);
			Document expanded = expand.expander(docTemplate, b);
			expanded.setNamespace(ns);
			intFr.writeDocument("src/expandedDocuments/" + expandedDocumentName, ProvFormat.TURTLE, expanded);
		} else {
			System.out.println("docTemplate is null");
		}
	}

	private static void listf(String directoryName, ProvFormat format) {

		URL directoryURL = Expander.class.getResource("..");

		File directory = new File("src/" + directoryName);
		if (directory.listFiles() != null) {
			File[] fList = directory.listFiles();
			for (File file : fList) {
				Document d = intFr.readDocumentFromFile(file.getAbsolutePath(), format);
				fullTemplateName_SchemaDocument.put(file.getName().split("\\.")[0], d);

			}
		}
	}

	private static String getBindingOperatorName(String bindingName) {
		int i = bindingName.lastIndexOf("_");
		int j = bindingName.lastIndexOf(".");
		String aux = bindingName.substring(i + 1, j);
		return aux;
	}

}
