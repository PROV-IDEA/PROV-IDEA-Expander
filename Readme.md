# PROV-IDEA Expander

This tool allows the interested user to obtain PROV documents from bindings with schema evolution provenance data, as established by the PROV-IDEA approach [1]. 

PROV-IDEA is presented as a PROV-Interoperable Database Evolution Approach aimed at integrating provenance information into relational database schema evolution, using PROV as provenance framework. PROV-IDEA proposes the use of *PROV templates* [2], expressed in PROV [3], for Schema Modification Operators (SMOs) and Data Modification Operators (DMOs) to structure the provenance to be generated from database evolution. More specifically, the approach establishes a set of PROV Templates to represent SMOs and DMOs. These templates contain
variables that act as placeholders for values. On the other hand, following the approach, as SMOs and DMOs take place, *sets of bindings* are generated, which are mainly variable-value pairs that associate variables of the templates and values. Finally, the *expansion algorithm* [2] can be used to generate *PROV documents* from templates, by replacing all variables with values found in a set of bindings, obtaining the corresponding PROV document ready for provenance consumption.

*PROV-IDEA Expander* has been developed to perform such an expansion automatically. More specifically, it goes through the binding documents generated from the execution of
schema evolution operations (both SMOs and DMOs), finds the PROV template related to each binding (that is, to each operation), and, finally, uses the *expansion algorithm* 
to generate the corresponding PROV document ready for provenance consumption. 

## Project's main content

The repository mainly contains the following folders: 
- **src/bindings**: with the sets bindings in TURTLE format generated from the execution of different SMOs and DMOs of a concrete running example.
- **src/templates**: with the PROV templates established by the PROV-IDEA to structure the provenance to be generated from database evolution.
- **src/expander**: includes the *Expander.java* class with a *main* method which is in charge of executing the *expansion algorithm* [2] generating PROV documents from
  the PROV templates, in the **src/templates** folder, by replacing all variables by values found in a set of bindings located in the **src/bindings** folder.
- **src/expandedDocuments**: it is the place where the PROV documents, resulting from the expansion process, will be placed. The repository already includes the
                              documents generated from the expansion of the bindings, in the **src/bindings** folder, and the PROV templates, in the **src/templates** folder.

## Setup environment

For testing *PROV-IDEA Expander* you'll have to use at least JDK-17.

The code is configured to generate the PROV documents in TURTLE format but it can be modified to generate documents in other 
formats such as JSON, JPEG or SVG (options available as static attributes of the **org.openprovenance.prov.interop.InteropFramework.ProvFormat** class).

The format can be changed in the following line of the **expand** method:

&emsp;**intFr.writeDocument("src/expandedDocuments/" + expandedDocumentName, ProvFormat."staticAttribute", expanded);**
      
## Running

To test *PROV-IDEA Expander* you'll have to run the *main* method of the *Expander.java* class.

## References

[1] PROV-IDEA. Supplementary material. Available at: [https://zenodo.org/records/10701239](https://zenodo.org/records/10701239).

[2] Luc Moreau, Belfrit Victor Batlajery, Trung Dong Huynh, Danius Michaelides, and Heather Packer. 2018. A Templating System to Generate
Provenance. IEEE Transactions on Software Engineering 44, 2 (2018), 103–121.

[3] Paul Groth and Luc Moreau (eds.). 2013. PROV-Overview. An Overview of the PROV Family of Documents. W3C Working Group Note NOTE-prov-
overview-20130430. World Wide Web Consortium. Available at  [www.w3.org/TR/2013/NOTE-prov-overview-20130430/](www.w3.org/TR/2013/NOTE-prov-overview-20130430/).
