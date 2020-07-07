package de.abas.cons.abastl.Selection;

import de.abas.cons.abastl.Utils;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.Query;
import de.abas.erp.db.schema.company.Summary;
import de.abas.erp.db.schema.company.Vartab;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.permission.LicenseType;
import de.abas.erp.db.schema.printparameter.DocumentTypes;
import de.abas.erp.db.schema.referencetypes.MasterFiles;
import de.abas.erp.db.schema.valueset.Identifier;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

import java.util.HashMap;

public class MasterFileSelections {

    public static SelectionBuilder<Product> getAllProducts(){
        return SelectionBuilder.create(Product.class);

    }
    public static SelectionBuilder<Product> getProductsSwd(String swd){
        return getAllProducts().add(Conditions.eq(Product.META.swd,swd));
    }
    public static SelectionBuilder<LicenseType> getAllLicenceTypes(){
        return SelectionBuilder.create(LicenseType.class);

    }
    public static SelectionBuilder<LicenseType> getLicenceTypesSwdClass(String swd, String className){
        return getAllLicenceTypes().add(Conditions.eq(LicenseType.META.swd,swd)).add(Conditions.eq(LicenseType.META.className, className));
    }
    public static SelectionBuilder<Vartab> getAllVartabs(){
        return SelectionBuilder.create(Vartab.class);
    }
    public static SelectionBuilder<Vartab> getVartabsDbnrGr(String idno, String swd){
//        String dbno = dbnr.replaceAll("\\W", "");
        //      String dbgrr = dbgr.replaceAll("\\W","");
        return getAllVartabs()
                //.add(Conditions.eq(Vartab.META.grpDBNo, EnumDatabase.valueOf(Integer.valueOf(dbno))))
                // .add(Conditions.eq(Vartab.META.grpGrpCmd, dbgr))
                .add(Conditions.eq(Vartab.META.idno,idno))
                .add(Conditions.eq(Vartab.META.swd,swd));
    }
    public static SelectionBuilder<DocumentTypes> getAllDocuments(){
        return SelectionBuilder.create(DocumentTypes.class);
    }
    public static SelectionBuilder<DocumentTypes> getDocumentsSwdClassName(String swd, String className){
        return getAllDocuments().add(Conditions.eq(DocumentTypes.META.swd,swd)).add(Conditions.eq(DocumentTypes.META.className, className));
    }
    public static SelectionBuilder<Enumeration> getAllEnumerations(){
        Utils.writeMessage("== GetAllEnumerations == ");
        return SelectionBuilder.create(Enumeration.class);
    }
    public static Query<MasterFiles> getMasterFilesQuery(DbContext dbContext, SelectionBuilder<MasterFiles> selection){
        return dbContext.createQuery(selection.build());
    }
    public static SelectionBuilder<Identifier> getAllIdentifiers(){
        return SelectionBuilder.create(Identifier.class);
    }
    public static SelectionBuilder<Identifier> getIdentifierSwdClassName(String swd, String className){
        return getAllIdentifiers().add(Conditions.eq(Identifier.META.swd,swd)).add(Conditions.eq(Identifier.META.className, className));
    }
    public static SelectionBuilder<Summary> getAllSummarys(){
        return SelectionBuilder.create(Summary.class);
    }
    public static SelectionBuilder<Summary> getSummarySwdClassName(String swd, String className){
        return getAllSummarys().add(Conditions.eq(Summary.META.swd,swd)).add(Conditions.eq(Summary.META.className, className));
    }

    public static SelectionBuilder<Enumeration> getEnumerationSwdClass(String swd , String className){
        return SelectionBuilder.create(Enumeration.class)
                .add(Conditions.eq(Enumeration.META.className,className))
                .add(Conditions.eq(Enumeration.META.swd,swd));
    }

    public static class Querys {
        public static HashMap<String, String> getEnumClassIdHashMap (Query<Enumeration> enumerations){
            final HashMap<String, String> container = new HashMap<>();
            for(Enumeration enumeration :enumerations.execute()){
                container.put(enumeration.getClassName(),enumeration.getId().toString());
            }
            return container;
        }
        public static HashMap<String, Enumeration> getClassEnumHashmap (Query<Enumeration> enumerations){
            final HashMap<String, Enumeration> container = new HashMap<>();
            for(Enumeration enumeration :enumerations.execute()){
                container.put(enumeration.getClassName(),enumeration);
            }
            return container;
        }
        private static HashMap<String, String> getMasterFilesHashMap (Query<MasterFiles> sourceQuery) {
            final HashMap<String,String > container = new HashMap<>();
            for(MasterFiles masterFiles : sourceQuery.execute()){
                container.put( masterFiles.getId().toString(),masterFiles.getSwd());
            }
            return container;
        }
        public static Query<Enumeration> getEnumerationsQuery(DbContext dbContext, SelectionBuilder<Enumeration> selection){
            Utils.writeMessage("=== Creating Enumeration Query === ");
            return dbContext.createQuery(selection.build());
        }
        public static Enumeration getFirstEnum(SelectionBuilder<Enumeration> sel, DbContext dbContext){
            if(sel == null){
                return null;
            }
            return QueryUtil.getFirst(dbContext,sel.build() );
        }


    }

}


