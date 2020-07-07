package de.abas.cons.abastl.Wrappers;

import de.abas.cons.abastl.Utils;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.common.type.enums.EnumScreenWriteProtect;
import de.abas.erp.db.AbasObject;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.EditorObject;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.field.Field;
import de.abas.erp.db.infosystem.custom.owst.TestsysClass;
import de.abas.erp.db.schema.company.Summary;
import de.abas.erp.db.schema.company.SummaryEditor;
import de.abas.erp.db.schema.company.Vartab;
import de.abas.erp.db.schema.custom.mirroring.MirrorTable;
import de.abas.erp.db.schema.custom.mirroring.MirrorTableEditor;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.enumeration.EnumerationEditor;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.ProductEditor;
import de.abas.erp.db.schema.permission.LicenseType;
import de.abas.erp.db.schema.permission.LicenseTypeEditor;
import de.abas.erp.db.schema.printparameter.DocumentTypes;
import de.abas.erp.db.schema.printparameter.DocumentTypesEditor;
import de.abas.erp.db.schema.referencetypes.MasterFiles;
import de.abas.erp.db.schema.valueset.Identifier;
import de.abas.erp.db.schema.valueset.IdentifierEditor;
import de.abas.erp.db.util.QueryUtil;

import java.util.HashMap;

public class FieldFiller {

    public static AbasObject fillFields (AbasObject masterFiles, TestsysClass head, DbContext sourceDbContext) throws CommandException {

        DbContext targetC = Utils.getServerContext(head.getYmandkonfig());

        if(masterFiles instanceof Summary){
            Summary target = targetC.load(Summary.class,new IdImpl(((Summary) masterFiles).getId().toString()));
            if(target == null){
                return null;
            }
            SummaryEditor summaryEditor = sourceDbContext.newObject(SummaryEditor.class);
            FieldFiller.fillAllSummarys(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            return summaryEditor.objectId();


        }
        if(masterFiles instanceof Identifier){
            Identifier target = targetC.load(Identifier.class,new IdImpl(((Identifier) masterFiles).getId().toString()));
            if(target == null){
                return null;
            }
            IdentifierEditor summaryEditor = sourceDbContext.newObject(IdentifierEditor.class);
            FieldFiller.fillAllIdentifierFields(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            Utils.writeMessage("Identifiert Wrote");
            return summaryEditor.objectId();
        }


        if(masterFiles instanceof LicenseType){
            LicenseType target = targetC.load(LicenseType.class,new IdImpl(((LicenseType) masterFiles).getId().toString()));
            if(target == null){
                return null;
            }
            LicenseTypeEditor summaryEditor = sourceDbContext.newObject(LicenseTypeEditor.class);
            FieldFiller.fillAllLicenceType(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            return  summaryEditor.objectId();

        }
        if(masterFiles instanceof Enumeration){
            Enumeration target = targetC.load(Enumeration.class,new IdImpl(((Enumeration) masterFiles).getId().toString()));
            if(target == null){
                return null;
            }
            EnumerationEditor summaryEditor = sourceDbContext.newObject(EnumerationEditor.class);
            FieldFiller.fillAllEnumerationsField(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            return summaryEditor.objectId();

        }

        if(masterFiles instanceof DocumentTypes){
            DocumentTypes target = targetC.load(DocumentTypes.class,new IdImpl(((DocumentTypes) masterFiles).getId().toString()));
            if(target == null){
                return null;
            }
            DocumentTypesEditor summaryEditor = sourceDbContext.newObject(DocumentTypesEditor.class);
            FieldFiller.fillAllDocuments(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            return summaryEditor.objectId();

        }

        if(masterFiles instanceof Product){

            Product target = QueryUtil.getFirstByIdNo(targetC,((Product) masterFiles).getIdno(),Product.class);

            if(target == null){
                return null;
            }
            ProductEditor summaryEditor = sourceDbContext.newObject(ProductEditor.class);
            FieldFiller.fillAllProducts(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            return summaryEditor.objectId();
        }

        if(masterFiles instanceof Vartab){
            return null;
        }




       return null;


    }
    public static AbasObject fillAllFields (MirrorTable.Row row, DbContext sourceDbContext, TestsysClass head, HashMap<String, Integer> masterFilesContainer) throws CommandException {

        DbContext targetC = Utils.getServerContext(head.getYmandkonfig());

        EditorObject editorObject = null;
        if(row.getYmirrortype().equals(Summary.class.getSimpleName())){
            Summary target = targetC.load(Summary.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            SummaryEditor summaryEditor = sourceDbContext.newObject(SummaryEditor.class);
            FieldFiller.fillAllSummarys(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;

        }
        if(row.getYmirrortype().equals(Identifier.class.getSimpleName())){
            Identifier target = targetC.load(Identifier.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            IdentifierEditor summaryEditor = sourceDbContext.newObject(IdentifierEditor.class);
            FieldFiller.fillAllIdentifierFields(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
        }


        if(row.getYmirrortype().equals(LicenseType.class.getSimpleName())){
            LicenseType target = targetC.load(LicenseType.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            LicenseTypeEditor summaryEditor = sourceDbContext.newObject(LicenseTypeEditor.class);
            FieldFiller.fillAllLicenceType(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;

        }
        if(row.getYmirrortype().equals(Enumeration.class.getSimpleName())){
            Enumeration target = targetC.load(Enumeration.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            EnumerationEditor summaryEditor = sourceDbContext.newObject(EnumerationEditor.class);
            FieldFiller.fillAllEnumerationsField(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;

        }
        if(row.getYmirrortype().equals(DocumentTypes.class.getSimpleName())){
            DocumentTypes target = targetC.load(DocumentTypes.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            DocumentTypesEditor summaryEditor = sourceDbContext.newObject(DocumentTypesEditor.class);
            FieldFiller.fillAllDocuments(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;

        }
        if(row.getYmirrortype().equals(Product.class.getSimpleName())){
            Product target = targetC.load(Product.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            ProductEditor summaryEditor = sourceDbContext.newObject(ProductEditor.class);
            FieldFiller.fillAllProducts(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;
        }
        if(row.getYmirrortype().equals(Summary.class.getSimpleName())){
            Summary target = targetC.load(Summary.class,new IdImpl(row.getYtmirrorid()));
            if(target == null){
                return null;
            }
            SummaryEditor summaryEditor = sourceDbContext.newObject(SummaryEditor.class);
            FieldFiller.fillAllSummarys(summaryEditor,target,sourceDbContext);
            summaryEditor.commit();
            editorObject = summaryEditor;

        }
        if(row.getYmirrortype().equals(Vartab.class.getSimpleName())){
         return null;
        }




       if(editorObject == null){
           return null;
       }
        MirrorTableEditor.Row row1 = row.header().createEditor().open(EditorAction.MODIFY).table().getRow(row.getRowNo());
        row1.setYtsourcereference((MasterFiles)editorObject.objectId());


        masterFilesContainer.put(row.header().getId().toString(),row.getRowNo());
        row1.header().commit();
        return editorObject.objectId();

    }
    public static void fillAllLicenceType(LicenseTypeEditor sourceEditor, LicenseType target, DbContext sourceC){
        for(Field field : Identifier.META.fields()){

            if(field.getGermanName().equals("such")){
                sourceEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("classname")){
                sourceEditor.setClassName(target.getClassName());
            }
            if(field.getGermanName().equals("namebspr")){
                sourceEditor.setDescrOperLang(target.getDescrOperLang());
            }

            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                sourceEditor.setString(field,sourceEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
    public static void fillAllSummarys(SummaryEditor sourceEditor, Summary target, DbContext sourceC){
        for(Field field : Identifier.META.fields()){

            if(field.getGermanName().equals("such")){
                sourceEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("classname")){
                sourceEditor.setClassName(target.getClassName());
            }
            if(field.getGermanName().equals("namebspr")){
                sourceEditor.setDescrOperLang(target.getDescrOperLang());
            }

            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                sourceEditor.setString(field,sourceEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
    public static void fillAllProducts(ProductEditor sourceEditor, Product target, DbContext sourceC){
        for(Field field : Product.META.fields()){

            if(field.getGermanName().equals("such")){
                sourceEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("namebspr")){
                sourceEditor.setDescrOperLang(target.getDescrOperLang());
            }

            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                sourceEditor.setString(field,sourceEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
    public static void fillAllDocuments(DocumentTypesEditor sourceEditor, DocumentTypes target, DbContext sourceC){
        for(Field field : DocumentTypesEditor.META.fields()){

            if(field.getGermanName().equals("such")){
                sourceEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("classname")){
                sourceEditor.setClassName(target.getClassName());
            }
            if(field.getGermanName().equals("namebspr")){
                sourceEditor.setDescrOperLang(target.getDescrOperLang());
            }

            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                sourceEditor.setString(field,sourceEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
    public static void fillAllIdentifierFields(IdentifierEditor sourceEditor, Identifier target, DbContext sourceC){

        for(Field field : Identifier.META.fields()){

            if(field.getGermanName().equals("such")){
                sourceEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("classname")){
                sourceEditor.setClassName(target.getClassName());
            }

            if(field.getGermanName().equals("namebspr")){
                sourceEditor.setDescrOperLang(target.getDescrOperLang());
            }
            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                sourceEditor.setString(field,sourceEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
    public static void fillAllEnumerationsField(EnumerationEditor enumerationEditor,Enumeration target, DbContext sourceC){
        for(Field field : Enumeration.META.fields()){

            if(field.getGermanName().equals("such")){
                enumerationEditor.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("classname")){
                enumerationEditor.setClassName(target.getClassName());
            }
            if(field.getGermanName().equals("tabart")){
                enumerationEditor.setRefType(target.getRefType());
            }
            if(field.getGermanName().equals("namebspr")){
                enumerationEditor.setDescrOperLang(target.getDescrOperLang());
            }
            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                enumerationEditor.setString(field,enumerationEditor.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceC.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }

    }
}
