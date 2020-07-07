package de.abas.cons.abastl.Wrappers;

import de.abas.cons.abastl.MasterFilesObjectHandler;
import de.abas.cons.abastl.Selection.MasterFileSelections;
import de.abas.cons.abastl.Selection.MirrorDataSelections;
import de.abas.cons.abastl.Selection.MirrorTableSelections;
import de.abas.cons.abastl.Utils;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.common.type.enums.EnumFilingModeSelection;
import de.abas.erp.db.*;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.infosystem.custom.owst.TestsysClass;
import de.abas.erp.db.schema.custom.mirroring.*;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.enumeration.EnumerationEditor;
import de.abas.erp.db.schema.referencetypes.MasterFiles;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.abas.cons.abastl.Utils.getDbContext;

public class MirrorOperations {

    public static void resetData(MirrorKonfig mirrorKonfig, String type, DbContext dbContext){
        MirrorData mirrorData = MirrorDataSelections.getMirrorDataTypeMandant(type,mirrorKonfig,dbContext);
        if(mirrorData == null) return;
        MirrorTable mirrorTable = MirrorTableSelections.getMirrorTableForData(mirrorData,dbContext);
        mirrorData.delete();
        if(mirrorTable == null) return;
        mirrorTable.delete();
    }


    public static class Table {
        public static HashMap<String , String> getMirrorTableHashMapIdSwd(MirrorTable mirrorTable){
            Utils.writeMessage(" Write MirrorTable Id Swd Container ");
            final HashMap<String, String> container = new HashMap<>();
            for(MirrorTable.Row row : mirrorTable.table().getRows()){
                container.put(row.getYtmirrorid(),row.getYtmirrorswd());
            }
            return container;
        }
        public static void createMirrorTableRows(MirrorData mirrorData, MirrorTableEditor editor, TestsysClass head){
            if(mirrorData == null || editor == null || head == null){
                return;
            }
            Utils.writeMessage("-------- Create MirrorTable Rows -------");
            DbContext targetContext = Utils.getServerContext(head.getYmandkonfig());
            if(targetContext == null) return;

            final HashMap<String, String> container = getMirrorTableHashMapIdSwd(editor);
            final MasterFilesObjectHandler masterFilesObjectHandler = MasterFilesObjectHandler.getInstance();
            Utils.writeMessage("Start MirrorData loop!");
            for(MirrorData.Row dataRow : mirrorData.table().getRows()){

                Enumeration enumeration = targetContext.load(Enumeration.class,new IdImpl(dataRow.getYmirrorid()));
                if(enumeration == null)continue;

                for(Enumeration.Row row : enumeration.table().getRows()){
                    Utils.writeMessage(" --- Current Mirror Enum " + dataRow.getYmirrorclassname() + "---");
                    MasterFiles masterFile = row.getRefToEnumElem();
                    String id = masterFile.getId().toString();
                    if(container.containsKey(id) && container.get(id).equals(masterFile.getSwd())){
                              Utils.writeMessage("Already Exists");
                        continue;
                    }
                    Utils.writeMessage("Creating");
                    MirrorTableEditor.Row editorRow = editor.table().appendRow();
                    editorRow.setYtmirrorclass(masterFile.getClassName());
                    editorRow.setYtmirrordnr(masterFile.getDBNo().toString());
                    editorRow.setYtmirrorgr(String.valueOf(masterFile.getGrpNo()));
                    editorRow.setYtmirrorswd(masterFile.getSwd());
                    editorRow.setYtmirrorid(masterFile.getId().toString());
                    editorRow.setYtmirroridno(masterFile.getIdno());
                    editorRow.setYtmirrorreference(row.header().getId().toString());
                    if(dataRow.getYsourcereference() != null && dataRow.getYsourcereference() instanceof Enumeration){
                        editorRow.setYsourceenumref((Enumeration) dataRow.getYsourcereference());
                        Utils.writeMessage(" Reference set");
                    }
                    masterFilesObjectHandler.setMasterFileTypeInMirrorTableRow(editorRow, masterFile);



                }



            }

        }
        public static HashMap<Boolean, MirrorTable> checkIfMirrorTableExists(MirrorData mirrorData){
           Utils.writeMessage("Checking If Mirror Table Exists");
            HashMap<Boolean, MirrorTable > container = new HashMap<>();
            SelectionBuilder<MirrorTable> selectionBuilder = SelectionBuilder.create(MirrorTable.class);
            selectionBuilder.add(Conditions.eq(MirrorTable.META.ymirrorhead,mirrorData));
            selectionBuilder.setFilingMode(EnumFilingModeSelection.Active);
            MirrorTable mirrorTable = QueryUtil.getFirst(Utils.getDbContext(),selectionBuilder.build());
            if(mirrorTable == null){
                container.put(false,null);
                return container;
            }
            container.put(true,mirrorTable);
            return container;
        }
        public static MirrorTable createMirrorTable(MirrorData mirrorData, TestsysClass head) throws CommandException{
            if(mirrorData == null){
                return null;
            }
            Utils.writeMessage(" ######## Create MirrorTable For MirrorData " + mirrorData.getSwd() + "#########");
            DbContext dbContext = getDbContext();
            MirrorTable mirrorTable = null;
            MirrorTableEditor mirrorTableEditor = null;
            HashMap<Boolean, MirrorTable > container = checkIfMirrorTableExists(mirrorData);
            boolean doesTableExist = container.keySet().iterator().next();
            mirrorTable = container.get(doesTableExist);
            if(doesTableExist && mirrorTable != null){
                Utils.writeMessage("------- MirrorTable Exists start Editing -------");
                mirrorTableEditor = mirrorTable.createEditor().open(EditorAction.MODIFY);
            }
            if(!doesTableExist && mirrorTable == null){
                Utils.writeMessage("---- New MirrorTable Created -----");
                mirrorTableEditor = dbContext.newObject(MirrorTableEditor.class);
                mirrorTableEditor.setYmirrorhead(mirrorData);
                mirrorTableEditor.setSwd(mirrorData.getSwd());

            }
            if(head == null){
                Utils.writeMessage("!!!!!!!!!!!!!!!! CLOSED !!!!!!!!!!!!)");
                mirrorTableEditor.commit();
                return mirrorTableEditor.objectId();
            }

            MirrorOperations.Table.createMirrorTableRows(mirrorData,mirrorTableEditor,head);
            mirrorTableEditor.commit();
            Utils.writeMessage("Save Object");
            return mirrorTableEditor.objectId();
        }
        public static void setSourceReferenceMirrorTable(MirrorTable mirrorTable) throws CommandException{
            if(mirrorTable == null){
                return;
            }
            Utils.writeMessage("??????? Set Source References in Mirror Table ???????????");
            DbContext dbContext = getDbContext();

            MirrorTableEditor mirrorTableEditor = mirrorTable.createEditor().open(EditorAction.MODIFY);
            MasterFilesObjectHandler masterFilesObjectHandler = MasterFilesObjectHandler.getInstance();

            for(MirrorTableEditor.Row row : mirrorTableEditor.table().getEditableRows()){

                Utils.writeMessage("Current Row " + row.getYtmirrorswd());
                AbasObject object = masterFilesObjectHandler.getAbasObjectMirrorTableRow(row,dbContext);
                if(object == null){
                    continue;
                }
                    row.setYtsourcereference((MasterFiles)object);

            }
            mirrorTableEditor.commit();
        }
        // TargetMandant Enum Container EnumId - RefEnum, ClassNameRefEnu,
        public static HashMap<String, HashMap<String, MirrorTable.Row>> getTargetMandantEnumContainer(List<MirrorTable.Row> rows){
            if(rows == null ) return null;
            //TargetEnum-   TargetEnumRef , SourceMirrorRowID
            final HashMap<String, HashMap<String, MirrorTable.Row>> enumContainer = new HashMap<>();
            //LOOP 1 get all EnumsToCreate
            for(MirrorTable.Row row : rows){

                if(enumContainer.containsKey(row.getYtmirrorreference())){
                    continue;
                }
                final HashMap<String, MirrorTable.Row > containerrr = new HashMap<>();
                enumContainer.put(row.getYtmirrorreference(),containerrr);
            }

            //LOOP 2 add references to enum
            for(MirrorTable.Row row : rows){
                if(!enumContainer.containsKey(row.getYtmirrorreference())){
                    continue;
                }
                final HashMap<String, MirrorTable.Row > container = enumContainer.get(row.getYtmirrorreference());
                container.put(row.getYtmirrorid(),row);
            }
            return enumContainer;
        }
        public static void createEmptyMirrorTableReferences(RowQuery<MirrorTable , MirrorTable.Row> query, DbContext targetC, DbContext sourceC, TestsysClass head) throws CommandException {
            if (query == null) {
                return;
            }
            List<MirrorTable.Row> rows = query.execute();


            final MasterFilesObjectHandler masterFilesObjectHandler = MasterFilesObjectHandler.getInstance();
            final HashMap<String, HashMap<String, MirrorTable.Row>> enumContainer = getTargetMandantEnumContainer(rows);

            for (Map.Entry<String, HashMap<String, MirrorTable.Row>> entry : enumContainer.entrySet()) {
                Enumeration target = targetC.load(Enumeration.class, new IdImpl(entry.getKey()));
                if (target == null) continue;
                sourceC.out().println("Target : " + target.getSwd() + "   " + target.getClassName());

                Enumeration source = null;
                String targetSwd = target.getSwd();
                String targetClass = target.getClassName();
                final HashMap<String, MirrorTable.Row> rowContainer = entry.getValue();


                EnumerationEditor editor = null;
                EnumerationEditor.Row rowEditor = null;


                //setMasterFilesInRow
                for (Map.Entry<String, MirrorTable.Row> masterFilesEntry : rowContainer.entrySet()) {
                    masterFilesObjectHandler.createAbasObjectMirrorTableRow(masterFilesEntry.getValue(), sourceC);
                }
                //checkIfEnumReferenceExist
                source = MasterFileSelections.Querys.getFirstEnum(MasterFileSelections
                        .getEnumerationSwdClass(targetSwd, targetClass), sourceC);


                // create EnumReference
                if (source == null) {
                    editor = sourceC.newObject(EnumerationEditor.class);
                    FieldFiller.fillAllEnumerationsField(editor, target, sourceC);
                    editor.commitAndReopen();
                }

                if (source != null) {
                    editor = source.createEditor().open(EditorAction.UPDATE);
                }
                if (editor == null) continue;

                //container with source enums refs
                final HashMap<String, MasterFiles> referenceContainer = Utils.getHashMapReferenceEnum(editor);

                int counter = 0;
                for (Map.Entry<String, MirrorTable.Row> mirrorRow : rowContainer.entrySet()) {
                    counter++;
                    MirrorTable.Row currVal = mirrorRow.getValue();
                    //continue if reference exists in row
                    if (referenceContainer != null) {
                        if (referenceContainer.containsKey(currVal.getYtmirrorclass())) continue;
                    }
                    if (currVal.getYtsourcereference() == null) continue;

                    rowEditor = editor.table().appendRow();
                    rowEditor.setRefToEnumElem(currVal.getYtsourcereference());
                    Enumeration.Row targetRow = target.table().getRow(counter);
                    if (targetRow != null) rowEditor.setEnumDescr(targetRow.getEnumDescr());

                }
                editor.commit();
                source = editor.objectId();
                //setEnumToAllRows

                for (Map.Entry<String, MirrorTable.Row> mirrorRow : rowContainer.entrySet()) {
                    masterFilesObjectHandler.setEnumMirrorData(mirrorRow.getValue(), source);
                }
                for (Map.Entry<String, MirrorTable.Row> mirrorRow : rowContainer.entrySet()) {
                    masterFilesObjectHandler.setEnumMirrorRow(mirrorRow.getValue(), source);
                }



            }


        }
        public static HashMap<String, MirrorTable.Row> getMirrorSwdRowHashMap(MirrorTable mirrorTable, String filter){
            if(mirrorTable == null) {
                Utils.writeMessage("ERROR");
                return null;
            }
            Utils.writeMessage("Creating MirrorTable SwdRow HashMap");
            final HashMap<String, MirrorTable.Row> container = new HashMap<>();
            for(MirrorTable.Row row : mirrorTable.table().getRows()){
                if(!filter.isEmpty() && !filter.equals(row.getYtmirrorreference())){

                        continue;
                }
                Utils.writeMessage("MirrorTable Contains " + filter);
                container.put(row.getYtmirrorswd(),row);
            }
            return container;
        }

    }

    public static class Data {

        public static void fillEmptyMirrorDataRows(DbContext dbContext){
            if(dbContext == null) return;
            final List<MirrorData.Row> emptyRows = MirrorDataSelections.Querys.getResults(MirrorDataSelections.getAllEmptySourceReferenceRows(),dbContext);
            for(MirrorData.Row row : emptyRows){
                Enumeration enumx = MasterFileSelections.Querys.getFirstEnum(
                        MasterFileSelections
                                .getEnumerationSwdClass(row.getYmirrorswd(), row.getYmirrorclassname()),
                dbContext);
                if(enumx == null) continue;
                setEnumToRow(row,enumx);


            }
        }
        public static void setEnumToRow(MirrorData.Row row , Enumeration enumeration){
            if(row == null|| enumeration == null) return;
            if(!row.getYmirrorclassname().equals(enumeration.getClassName())) return;
            MirrorDataEditor editor = null;
            MirrorDataEditor.Row rowE = null;
            try {
                editor = row.header().createEditor().open(EditorAction.MODIFY);
                rowE = editor.table().getRow(row.getRowNo());
                if(rowE == null) return;

                rowE.setYsourcereference(enumeration);
                editor.commit();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(editor != null && editor.active()){
                    editor.abort();
                    editor = null;
                }
            }
        }
        public static HashMap<String, String> getMirrorDataHashMapClassId(MirrorData mirrorData){
            if(mirrorData == null ) return null;
            Utils.writeMessage(" === Write MirrorDataHashMap Container ### Class!Id ");
            final HashMap<String, String> container = new HashMap<>();
            for(MirrorData.Row row : mirrorData.table().getRows()){
                container.put(row.getYmirrorclassname(),row.getYmirrorid());
            }
            return container;
        }
        public static MirrorData createMirrorData(Enumeration enumeration, TestsysClass head) throws CommandException {
           Utils.writeMessage(" ==== Create MirrorData ====");
            DbContext dbContext = getDbContext();
            String className = enumeration.getClass().getSimpleName();
            String enumClassName = enumeration.getClassName();
            String enumId = enumeration.getId().toString();
            HashMap<Boolean, MirrorData> container = checkifMirrorDataExists(className, head);

            MirrorDataEditor mirrorDataEditor = null;
            boolean doesMirrorExist = container.keySet().iterator().next();
            MirrorData mirrorData = container.get(doesMirrorExist);

            if(doesMirrorExist && mirrorData != null){

                final HashMap<String,String > containerEnums = getMirrorDataHashMapClassId(mirrorData);
                if(containerEnums.containsKey(enumClassName) && containerEnums.get(enumClassName).equals(enumId)){
                    Utils.writeMessage(" ##MirrorData " + mirrorData.getSwd() +
                            "  contains already " + className + " " +enumId);
                       return mirrorData;
                }

                mirrorDataEditor = mirrorData.createEditor().open(EditorAction.MODIFY);

            }

            if(!doesMirrorExist && mirrorData == null){
                Utils.writeMessage("===== New MirrorData Created =====" + "# " + className);
                mirrorDataEditor = dbContext.newObject(MirrorDataEditor.class);
                mirrorDataEditor.setSwd(className);
            }

            MirrorDataEditor.Row row = mirrorDataEditor.table().appendRow();
            row.setYmirrorclassname(enumeration.getClassName());
            row.setYmirrordbnr(enumeration.getDBNo().getDisplayString());
            row.setYmirrorgr(String.valueOf(enumeration.getGrpNo()));
            row.setYmirrorswd(enumeration.getSwd());
            row.setYmirrorid(enumeration.getId().toString());
            row.setYmirroridno(enumeration.getIdno());
            mirrorDataEditor.setYmandantkonfig(head.getYmandkonfig());

            mirrorDataEditor.commit();
            return mirrorDataEditor.objectId();

        }
        public static HashMap<Boolean, MirrorData> checkifMirrorDataExists(String className, TestsysClass head){
           Utils.writeMessage("Check if MirrorData for Object and MandantConfiguration exists!");
            HashMap<Boolean, MirrorData> container = new HashMap<>();
            SelectionBuilder<MirrorData> selectionBuilder = SelectionBuilder.create(MirrorData.class);
            selectionBuilder.add(Conditions.eq(MirrorData.META.swd, className.toUpperCase()));
            selectionBuilder.add(Conditions.eq(MirrorData.META.ymandantkonfig,head.getYmandkonfig()));
            selectionBuilder.setFilingMode(EnumFilingModeSelection.Active);
            MirrorData mirrorData = QueryUtil.getFirst(Utils.getDbContext(), selectionBuilder.build());
            if(mirrorData == null){
                container.put(false, null);
                return container;
            }
            container.put(true,mirrorData);
            return container;

        }
        public static HashMap<String, MirrorData.Row> getClassHashMapEmptyRows (MirrorData mirrorData){
            if(mirrorData == null){
                return null;
            }
            final HashMap<String, MirrorData.Row > container = new HashMap<>();
            for(MirrorData.Row row : mirrorData.table().getRows()){
                if(row.getYsourcereference() != null) continue;
                container.put(row.getYmirrorclassname(),row );
            }
            return container;
        }
        public static void setSourceReferenceMirrorData(MirrorData mirrorData) throws CommandException{
            if(mirrorData == null){return;}
            DbContext dbContext = getDbContext();

            Utils.writeMessage(" ==== Set Source References in MirrorData === ");
            Utils.writeMessage(" ");
            Query<Enumeration> sourceQuery = MasterFileSelections.Querys.getEnumerationsQuery(dbContext,MasterFileSelections.getAllEnumerations());
            final HashMap<String, String> sourceContainer = MasterFileSelections.Querys.getEnumClassIdHashMap(sourceQuery);

            MirrorDataEditor editor = mirrorData.createEditor().open(EditorAction.MODIFY);
            for(MirrorDataEditor.Row row : editor.table().getEditableRows()){
                final String actualClassName = row.getYmirrorclassname();
                if(sourceContainer.containsKey(actualClassName)){
                    Utils.writeMessage(" Checking " + " ##" + actualClassName );
                    final String id = sourceContainer.get(actualClassName);
                    Enumeration enumeration = dbContext.load(Enumeration.class,new IdImpl(id));
                    if(enumeration == null){
                        Utils.writeMessage("  ===== Found new Enumeration ====");
                        Utils.writeMessage("=== Source Context dosnt contain " + actualClassName );
                        continue;
                    }
                    Utils.writeMessage(" Found Enumeration, setting to MirrorData Row");
                    row.setYsourcereference(enumeration);
                }
            }

            editor.commit();


        }
        public static HashMap<String, MirrorData.Row> getClassRowHashMap(MirrorData mirrorData){
            if(mirrorData == null) return null;
            final HashMap<String, MirrorData.Row> container = new HashMap<>();
            for(MirrorData.Row row : mirrorData.table().getRows()){
                container.put(row.getYmirrorclassname(),row);
            }
            return container;

        }

    }
}
