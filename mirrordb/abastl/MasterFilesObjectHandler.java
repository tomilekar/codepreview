package de.abas.cons.abastl;

import de.abas.cons.abastl.Selection.MasterFileSelections;
import de.abas.cons.abastl.Wrappers.FieldFiller;
import de.abas.cons.abastl.Wrappers.MirrorOperations;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.db.AbasObject;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.schema.company.Summary;
import de.abas.erp.db.schema.company.Vartab;
import de.abas.erp.db.schema.custom.mirroring.MirrorData;
import de.abas.erp.db.schema.custom.mirroring.MirrorDataEditor;
import de.abas.erp.db.schema.custom.mirroring.MirrorTable;
import de.abas.erp.db.schema.custom.mirroring.MirrorTableEditor;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.permission.LicenseType;
import de.abas.erp.db.schema.printparameter.DocumentTypes;
import de.abas.erp.db.schema.referencetypes.MasterFiles;
import de.abas.erp.db.schema.valueset.Identifier;
import de.abas.erp.db.schema.valueset.IdentifierEditor;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

import java.util.HashMap;

public class MasterFilesObjectHandler {


    private final static MasterFilesObjectHandler handlerInstance = new MasterFilesObjectHandler();




    public static MasterFilesObjectHandler getInstance(){
        return handlerInstance;

    }
    private MasterFilesObjectHandler () {
    }




   public void setMasterFileTypeInMirrorTableRow(MirrorTableEditor.Row editorRow, MasterFiles masterFile){
        Utils.writeMessage(" Set MasterFile");
        if(editorRow == null || !editorRow.header().active() || masterFile == null){
            System.out.println("ERROR");
            return;
        }
       if(masterFile instanceof Summary){
           Summary summary = (Summary) masterFile;
           editorRow.setYmirrortype(summary.getClass().getSimpleName());
       }
       if(masterFile instanceof Identifier){
           Identifier identifier = (Identifier) masterFile;
           editorRow.setYmirrortype(identifier.getClass().getSimpleName());
       }
       if(masterFile instanceof DocumentTypes){
           DocumentTypes documentTypes = (DocumentTypes) masterFile;
           editorRow.setYmirrortype(documentTypes.getClass().getSimpleName());
       }
       if(masterFile instanceof LicenseType){
           LicenseType documentTypes = (LicenseType) masterFile;
           editorRow.setYmirrortype(documentTypes.getClass().getSimpleName());
       }
       if(masterFile instanceof Vartab){
           Vartab documentTypes = (Vartab) masterFile;
           editorRow.setYmirrortype(documentTypes.getClass().getSimpleName());
       }
       if(masterFile instanceof Product){
           Product documentTypes = (Product) masterFile;
           editorRow.setYmirrortype(documentTypes.getClass().getSimpleName());
       }



   }


   public AbasObject getAbasObject(MasterFiles row, DbContext dbContext){
        if(row == null) return null;

       AbasObject object = null;
       if( row instanceof Summary){
           SelectionBuilder<Summary> sel = MasterFileSelections.getSummarySwdClassName(row.getSwd(),row.getClassName());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if( row instanceof Identifier){
           SelectionBuilder<Identifier> sel = MasterFileSelections.getIdentifierSwdClassName(row.getSwd(),row.getClassName());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if( row instanceof DocumentTypes){
           SelectionBuilder<DocumentTypes> sel = MasterFileSelections.getDocumentsSwdClassName(row.getSwd(),row.getClassName());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if( row instanceof LicenseType){
           SelectionBuilder<LicenseType> sel = MasterFileSelections.getLicenceTypesSwdClass(row.getSwd(),row.getClassName());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if( row instanceof Vartab){
           SelectionBuilder<Vartab> sel = MasterFileSelections.getVartabsDbnrGr(
                   row.getIdno(),row.getSwd());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if( row instanceof Product){
           SelectionBuilder<Product> sel = MasterFileSelections.getProductsSwd(
                   row.getSwd());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }


       Utils.writeMessage("TESTDDD");
       return object;
   }

   public AbasObject getAbasObjectMirrorTableRow(MirrorTable.Row row,  DbContext dbContext){
        if(row == null){
            return null;
        }
        AbasObject object = null;
       if(row.getYmirrortype().equals(Summary.class.getSimpleName())){
           SelectionBuilder<Summary> sel = MasterFileSelections.getSummarySwdClassName(row.getYtmirrorswd(),row.getYtmirrorclass());
           object = QueryUtil.getFirst(dbContext,sel.build());
       }
       if(row.getYmirrortype().equals(Identifier.class.getSimpleName())){
           SelectionBuilder<Identifier> sel = MasterFileSelections.getIdentifierSwdClassName(row.getYtmirrorswd(),row.getYtmirrorclass());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if(row.getYmirrortype().equals(DocumentTypes.class.getSimpleName())){
           SelectionBuilder<DocumentTypes> sel = MasterFileSelections.getDocumentsSwdClassName(row.getYtmirrorswd(),row.getYtmirrorclass());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if(row.getYmirrortype().equals(LicenseType.class.getSimpleName())){
           SelectionBuilder<LicenseType> sel = MasterFileSelections.getLicenceTypesSwdClass(row.getYtmirrorswd(),row.getYtmirrorclass());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if(row.getYmirrortype().equals(Vartab.class.getSimpleName())){
           SelectionBuilder<Vartab> sel = MasterFileSelections.getVartabsDbnrGr(
                   row.getYtmirroridno(),row.getYtmirrorswd());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }
       if(row.getYmirrortype().equals(Product.class.getSimpleName())){
           SelectionBuilder<Product> sel = MasterFileSelections.getProductsSwd(
                   row.getYtmirrorswd());
           object = QueryUtil.getFirst(dbContext,sel.build());

       }


       return object;
   }
   public AbasObject createAbasObjectMirrorTableRow(MirrorTable.Row row, DbContext sourceContext){
       DbContext targetContext = Utils.getServerContext(row.header().getYmirrorhead().getYmandantkonfig());

       if(row == null || sourceContext == null || targetContext == null){
            Utils.writeMessage("createAbasObjectMirrorTableRow null");
            return null;
        }

          MirrorTableEditor editor = null;
        MirrorTableEditor.Row rowEditor = null;
        AbasObject abasObject = null;
        abasObject = getAbasObjectMirrorTableRow(row,sourceContext);

        if(abasObject != null){
            setMasterFilesMirrorRow(row,abasObject);
        }

        if(row.getYmirrortype().equals(Identifier.class.getSimpleName())){
            IdentifierEditor identifierEditor = null;
            Identifier targetIdentifier = targetContext.load(Identifier.class,new IdImpl(row.getYtmirrorid()));
            if(targetIdentifier == null){
                return abasObject;
            }
              try {
                identifierEditor = sourceContext.newObject(IdentifierEditor.class);
                FieldFiller.fillAllIdentifierFields(identifierEditor,targetIdentifier,sourceContext);
                identifierEditor.commit();
                abasObject = identifierEditor.objectId();
            }catch (Exception e){
                e.printStackTrace();
            }finally {

            }
        }

        setMasterFilesMirrorRow(row,abasObject);
        return abasObject;
   }


   public void setEnumMirrorData(MirrorTable.Row row, Enumeration source){
       if(row == null || source == null){
           return;
       }
       MirrorData data = row.header().getYmirrorhead();
       MirrorDataEditor dataEditor = null;
       MirrorDataEditor.Row editorRow = null;
       final HashMap<String,MirrorData.Row> container = MirrorOperations.Data.getClassHashMapEmptyRows(data);
       if(container == null){
           return;
       }
       if(container.containsKey(source.getClassName())) {

           try {

               MirrorData.Row editedRow = container.get(source.getClassName());
               if(editedRow == null) return;
               dataEditor = data.createEditor().open(EditorAction.MODIFY);
               editorRow = dataEditor.table().getRow(editedRow.getRowNo());
               if(editorRow == null) return;
               editorRow.setYsourcereference(source);
               dataEditor.commit();

           }catch (CommandException e){
               e.printStackTrace();
           }finally {
               if(dataEditor != null && dataEditor.active()){
                   dataEditor.abort();
                   dataEditor = null;
               }
           }
       }
   }
   public void setEnumMirrorRow(MirrorTable.Row row, Enumeration source){
        if(row == null || source == null){
            return;
        }
        MirrorTableEditor editor = null;
        MirrorTableEditor.Row rowEditor = null;
        try {
            editor = row.header().createEditor().open(EditorAction.MODIFY);
            rowEditor = editor.table().getRow(row.getRowNo());
            if(rowEditor == null) {
                return;
            }
            rowEditor.setYsourceenumref(source);
            editor.commit();

        }catch (CommandException e){

            e.printStackTrace();
        }finally {
            if(editor != null && editor.active()){
                editor.abort();
                editor = null;
            }
        }

   }

   private void setMasterFilesMirrorRow(MirrorTable.Row row, AbasObject abasObject){
        if(row == null || abasObject == null){
            Utils.writeMessage("setMasterFile Failed for Row ");
            return;
        }
        MirrorTableEditor editor = null;
        MirrorTableEditor.Row rowEditor = null;
       try {
           editor  = row.header().createEditor().open(EditorAction.MODIFY);
           rowEditor = editor.table().getRow(row.getRowNo());
           if(rowEditor == null){
               return;
           }
           rowEditor.setYtsourcereference((MasterFiles) abasObject);
           editor.commit();
       }catch (CommandException e){
           e.printStackTrace();
       }finally{
           if(editor != null && editor.active()){
               editor.abort();
               editor = null;
           }
       }
   }
}
