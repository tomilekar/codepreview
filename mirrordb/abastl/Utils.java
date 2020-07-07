package de.abas.cons.abastl;

import de.abas.cons.abastl.Selection.MasterFileSelections;
import de.abas.cons.abastl.Wrappers.MirrorOperations;
import de.abas.eks.jfop.remote.FOe;
import de.abas.erp.api.gui.TextBox;
import de.abas.erp.common.ConnectionProperties;
import de.abas.erp.common.DefaultCredentialsProvider;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.db.*;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.infosystem.custom.owst.TestsysClass;
import de.abas.erp.db.schema.custom.mirroring.MirrorData;
import de.abas.erp.db.schema.custom.mirroring.MirrorKonfig;
import de.abas.erp.db.schema.custom.mirroring.MirrorTable;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.enumeration.EnumerationEditor;
import de.abas.erp.db.schema.referencetypes.MasterFiles;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.ContextHelper;
import de.abas.erp.db.util.QueryUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final String APPLICATION_PASSWORD = "sy";
    public static final String APPLICATION_NAME = "ISEXPORT";
    public static final String WLAN_MANDANT_1 = "WLAN_MANDANT_1";
    public static final String WLAN_MANDANT_2 = "WLAN_MANDANT_2";
    public static final String LAN_MANDANT_1 = "LAN_MANDANT_1";
    public static final String LAN_MANDANT_2 = "LAN_MANDANT_2";
    public static DbContext getDbContext(){
        return FOe.getFOPSessionContext().getDbContext();
    }
    public static DbContext getServerContext(MirrorKonfig mirrorKonfig){
        if(mirrorKonfig == null){
            showTextBox(Messages.INPUT_CANT_BE_EMPTY + " Will Use Fallback Context");
            return getDbContext();
        }
        ContextManager contextHelper = ContextHelper.buildContextManager();
        DefaultCredentialsProvider defaultCredentialsProvider = new DefaultCredentialsProvider(APPLICATION_PASSWORD);
        ConnectionProperties connectionProperties = new ConnectionProperties(mirrorKonfig.getYoncfighost(),
                Integer.valueOf(mirrorKonfig.getYoncfigport()),
                mirrorKonfig.getYconfigmandant(),APPLICATION_NAME);

        return contextHelper.createClientContext(connectionProperties,
                defaultCredentialsProvider);

    }

    public static void writeMessage(String test){
        DbContext dbContext = getDbContext();
        dbContext.out().println(test + "\n");
    }
    public static void showTextBox(String message){
        if(message.isEmpty()){
            writeMessage(Messages.INPUT_CANT_BE_EMPTY);
        }
        new TextBox(getDbContext(),
        "Message", message).show();
    }
    public static MirrorKonfig getMirrorConfig(String swd){
        SelectionBuilder<MirrorKonfig> selectionBuilder = SelectionBuilder.create(MirrorKonfig.class).add(Conditions.eq(MirrorKonfig.META.swd,swd));
        DbContext dbContext = getDbContext();
        return QueryUtil.getFirst(dbContext,selectionBuilder.build());

    }

    //
    public static HashMap<String, MasterFiles> getHashMapReferenceEnum(Enumeration enumeration){
        if(enumeration == null){
            return null;
        }
        final HashMap<String, MasterFiles> container = new HashMap<>();
        for(de.abas.erp.db.schema.enumeration.Enumeration.Row row : enumeration.table().getRows()){
            if(row.getRefToEnumElem() == null){
                continue;
            }
            container.put(row.getRefToEnumElem().getClassName(),row.getRefToEnumElem());
        }
        return  container;
    }

    public static void compareEnums(MirrorData.Row row , DbContext sourceContext){
        if(row == null ) return;
        if(row.getYsourcereference() == null || row.getYmirrorid().isEmpty()) {
            Utils.writeMessage("Data Row " + row.getRowNo() + " empty source reference canceled");
            return;
        }
        Enumeration source = (Enumeration)row.getYsourcereference();


        DbContext targetContext = Utils.getServerContext(row.header().getYmandantkonfig());

        if(targetContext == null) return;
        Enumeration target = targetContext.load(Enumeration.class, new IdImpl(row.getYmirrorid()));
        if(target == null) return;
        int targetRowCount = target.getRowCount();
        int sourceRowCount = source.getRowCount();
        if(targetRowCount < sourceRowCount){
            Utils.writeMessage(" Target should be updated");
            return;
        }
        //target
        final HashMap<String, MasterFiles> enumReferences = getHashMapReferenceEnum(target);
        final HashMap<String, MasterFiles> sourceReferences = getHashMapReferenceEnum(source);
        if(enumReferences == null) {
            Utils.writeMessage("Target Got no Rows " + target.getClassName());
            return;
        }
        final MasterFilesObjectHandler masterFilesObjectHandler = MasterFilesObjectHandler.getInstance();
        EnumerationEditor editor = null;
        final HashMap<String, String> enumDescr = new HashMap<>();
        try {
            Utils.writeMessage("Edit Enum : " + source.getClassName());
            editor = source.createEditor().open(EditorAction.UPDATE);
            int counter = 0 ;
            int doppeltValues = 0;
            for(Map.Entry<String, MasterFiles> entry:  enumReferences.entrySet()){
                counter++;
                AbasObject abasObject = masterFilesObjectHandler.getAbasObject(entry.getValue(),sourceContext);
                if(abasObject == null) {
                    Utils.writeMessage("Create Missing Objects");
                    return;
                }
                if(sourceReferences != null){
                    if(sourceReferences.containsKey(entry.getKey())) {
                        Utils.writeMessage("Enum Contains MasterFiles " + entry.getKey());
                        continue;
                    }
                }
                EnumerationEditor.Row rowE = editor.table().appendRow();
                rowE.setRefToEnumElem((MasterFiles) abasObject);
                enumDescr.put(rowE.getEnumDescr(),"CONTAINS");
                if(enumDescr.size() < counter){
                    doppeltValues ++;
                    rowE.setEnumDescr("changee" + doppeltValues);
                    Utils.writeMessage("Change Line " + counter );
                }
                if(rowE.getEnumDescr().isEmpty())
                    rowE.setEnumDescr("RESTMANMANAN");

            }
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


    public static void writeExpandingRows(TestsysClass.Row head, HashMap<String, MirrorTable.Row> mirror, List<Enumeration.Row> source){
        if(head == null || mirror == null ) return;
        Utils.writeMessage("Expanding Rows");
        if(source != null ) {
            Utils.writeMessage("Source Found");
            if (source.size() > mirror.size()) {
                Utils.writeMessage("source leading");
                writeSourceLeadingExpandRow(head,mirror,source);
                return;
            }
            Utils.writeMessage("Target Leading");
            writeTargetLeadingExpandRows(head,mirror,source);
        }

        Utils.writeMessage("Target Leading");
        writeTargetLeadingExpandRows(head,mirror,source);


    }
    public static void writeSourceLeadingExpandRow(TestsysClass.Row head, HashMap<String, MirrorTable.Row> mirror, List<Enumeration.Row> source){
        if(head == null || mirror == null || source == null) return;
        int startRow = head.getRowNo();
        for(Enumeration.Row row : source){
            startRow += 1;
            TestsysClass.Row appendRow = head.header().table().insertRow(startRow);
            writeEnumRow(row,appendRow);
            if(mirror != null){
                if(!mirror.containsKey(row.getRefToEnumElem().getSwd())); continue;
            }
            MirrorTable.Row mirrorRow = mirror.get(row.getRefToEnumElem().getSwd());
            writeMirrorRow(mirrorRow,appendRow);

        }
    }
    public static void writeEnumRow(Enumeration.Row masterFiles, TestsysClass.Row appendRow){
        if(masterFiles == null || appendRow == null) return;
        appendRow.setYtmirrorrefdescr(masterFiles.getEnumDescr());
        appendRow.setYtmirrorrefid(masterFiles.getRefToEnumElem().getId().toString());
        appendRow.setTytmirrorrefidno(masterFiles.getRefToEnumElem().getIdno());
        appendRow.setTytmirrorrefswd(masterFiles.getRefToEnumElem().getSwd());
        appendRow.setYtsourceref(masterFiles.getRefToEnumElem());
        appendRow.setYtshow(AbasIcons.ARROW_BLUE_DOWN.toString());

    }
    public static void writeMirrorRow(MirrorTable.Row row , TestsysClass.Row appendRow){
        if(row == null || appendRow == null) return;
        Utils.writeMessage("Write Mirror Row");
        appendRow.setYtmirrorrefid(row.getYtmirrorid());
        appendRow.setTytmirrorrefidno(row.getYtmirroridno());
        appendRow.setTytmirrorrefswd(row.getYtmirrorswd());
        appendRow.setYtshow(AbasIcons.ARROW_BLUE_DOWN.toString());

    }
    public static void writeTargetLeadingExpandRows(TestsysClass.Row head, HashMap<String, MirrorTable.Row> mirror, List<Enumeration.Row> source){
            if(head == null || mirror == null){

                Utils.writeMessage("CANT BE NULL");return;
            }
            int startRow = head.getRowNo();

            Utils.writeMessage("START  size mirror" + mirror.size() );
             HashMap<String, Enumeration.Row> masterFilesHashMap = null;
            if(source != null ) {
                Utils.writeMessage(" Source Not Null");
                masterFilesHashMap = Utils.getSwdMasterFilesHashMap(source);
            }
            for(Map.Entry<String, MirrorTable.Row> rowentry : mirror.entrySet()){
                MirrorTable.Row row = rowentry.getValue();
                Utils.writeMessage("Write " + rowentry.getValue().getYtmirrorswd());
                startRow += 1;
                TestsysClass.Row appendRow = head.header().table().insertRow(startRow);
                writeMirrorRow(row,appendRow);

                if(masterFilesHashMap == null){
                    continue;
                }Code
                if(!masterFilesHashMap.containsKey(row.getYtmirrorswd())) continue;

                Enumeration.Row masterFile = masterFilesHashMap.get(row.getYtmirrorswd());
                writeEnumRow(masterFile,appendRow);



            }
        }


    public static HashMap<String, Enumeration.Row> getSwdMasterFilesHashMap (List<Enumeration.Row> list) {
        if (list.size() == 0) return null;
        Utils.writeMessage("SWD MasterFiles HashMap");
        final HashMap<String, Enumeration.Row> container = new HashMap<>();
        for (Enumeration.Row row : list) {
            container.put(row.getRefToEnumElem().getSwd(), row);
        }
        return container;
    }
    public static void writeRows(TestsysClass head, MirrorData mirrorData, Query<Enumeration> sourceQu){
        if(head == null || mirrorData == null || sourceQu == null) return;
        head.table().clear();
        List<Enumeration> sourceList = sourceQu.execute();
        List<MirrorData.Row> targetRows = mirrorData.table().getRows();
        if(sourceList.size()> targetRows.size()){
            writeEnumLeadingRow(head,mirrorData,sourceQu);
            return;
        }
        writeTargetLeadingRow(head,mirrorData,sourceQu);

    }
    public static void writeEnumLeadingRow(TestsysClass head, MirrorData mirrorData, Query<Enumeration> sourceQ){
        if(head == null || mirrorData == null || sourceQ == null) return;
        final HashMap<String , MirrorData.Row>  classRowContainer = MirrorOperations.Data
                .getClassRowHashMap(mirrorData);
        for(Enumeration enumeration : sourceQ.execute()){
            final String sourceClass = enumeration.getClassName();
            final TestsysClass.Row appendRow = head.table().appendRow();
            appendRow.setYtshow(AbasIcons.ARROW_BLUE_RIGHT.toString());
            fillRowWithEnum(appendRow,enumeration);
            if(classRowContainer != null){
                if(!classRowContainer.containsKey(enumeration.getClassName()))continue;
            }
            MirrorData.Row curEntry = classRowContainer.get(sourceClass);
            fillRowWithMirrorDataRow(appendRow,curEntry);
        }
    }

    public static void fillRowWithEnum(TestsysClass.Row appendRow, Enumeration source){
        if(appendRow == null || source == null) return;
        appendRow.setYtenumsource(source);
        appendRow.setYtsourceclassname(source.getClassName());
        appendRow.setYtsourceidno(source.getIdno());
        appendRow.setYtsourceswd(source.getSwd());
    }
    public static void fillRowWithMirrorDataRow(TestsysClass.Row appendRow, MirrorData.Row dataRow ){
        if(appendRow == null || dataRow == null) return;
        appendRow.setYtmiirrordata(dataRow.header());
        appendRow.setYtmirrorclass(dataRow.getYmirrorclassname());
        appendRow.setYtmirrorswd(dataRow.getYmirrorswd());
        appendRow.setYtmirrorid(dataRow.getYmirrorid());
        appendRow.setYtmirroridno(dataRow.getYmirroridno());
    }
    public static void writeTargetLeadingRow(TestsysClass head, MirrorData mirrorData, Query<Enumeration> enumerations){
        if (head == null || mirrorData == null || enumerations == null) return;
        final HashMap<String, Enumeration> enumContainer = MasterFileSelections.Querys
                .getClassEnumHashmap(enumerations);
        for(MirrorData.Row row : mirrorData.table().getRows()){
            final String targetClass = row.getYmirrorclassname();
            final TestsysClass.Row appendRow = head.table().appendRow();
            appendRow.setYtshow(AbasIcons.ARROW_BLUE_RIGHT.toString());
            fillRowWithMirrorDataRow(appendRow,row);
            if(enumContainer != null){
                if(!enumContainer.containsKey(row.getYmirrorclassname()))continue;
            }
            final Enumeration source = enumContainer.get(row.getYmirrorclassname());
            fillRowWithEnum(appendRow,source);
        }

    }
    public static void showMirrorRows(TestsysClass.Row row){
        if(row == null) return;

    }

}
