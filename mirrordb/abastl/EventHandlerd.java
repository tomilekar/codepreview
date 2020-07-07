package de.abas.cons.abastl;


import de.abas.cons.abastl.Selection.MasterFileSelections;
import de.abas.cons.abastl.Selection.MirrorDataSelections;
import de.abas.cons.abastl.Selection.MirrorTableSelections;
import de.abas.cons.abastl.Wrappers.MirrorOperations;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.annotation.ScreenEventHandler;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.Query;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.infosystem.custom.owst.TestsysClass;
import de.abas.erp.db.schema.custom.mirroring.MirrorData;
import de.abas.erp.db.schema.custom.mirroring.MirrorKonfig;
import de.abas.erp.db.schema.custom.mirroring.MirrorTable;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.enumeration.EnumerationEditor;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

import java.util.HashMap;
import java.util.List;

import static de.abas.cons.abastl.Wrappers.MirrorOperations.Data.createMirrorData;
import static de.abas.cons.abastl.Wrappers.MirrorOperations.Table.createMirrorTable;
import static de.abas.cons.abastl.Wrappers.MirrorOperations.Table.setSourceReferenceMirrorTable;


@EventHandler(head = TestsysClass.class, row = TestsysClass.Row.class)
@RunFopWith(EventHandlerRunner.class)
public class EventHandlerd {



    @ButtonEventHandler(field = "ycreatemissingobje", type = ButtonEventType.AFTER)
    public void createMissingSourceReferences(DbContext dbContext, TestsysClass head) throws  CommandException{
   //   MirrorDataSelections.createAllEmptySourceReferenceRows(MirrorDataSelections.getAllEmptySourceReferenceRows(),dbContext, head);

        DbContext targetC = Utils.getServerContext(head.getYmandkonfig());
        MirrorOperations.Table.createEmptyMirrorTableReferences(MirrorTableSelections.Query.getRowQuery(MirrorTableSelections.getAllEmptySourceReferenceRows(),dbContext),
                targetC,dbContext,head);
        MirrorOperations.Data.fillEmptyMirrorDataRows(dbContext);

        // UPDATE ALL ENUMS

       MirrorData mirrorData =  MirrorDataSelections.Querys
               .getMirrorData(MirrorDataSelections.getMirrorData(head.getYmandkonfig()), dbContext);
       if(mirrorData == null) return;

       for(MirrorData.Row row : mirrorData.table().getRows()){
           Utils.compareEnums(row,dbContext);
       }

    }
    @ButtonEventHandler(field = "ydeleteallenums", type = ButtonEventType.AFTER)
    public void deleteAllEnums(DbContext dbContext, TestsysClass head) {
        //   MirrorDataSelections.createAllEmptySourceReferenceRows(MirrorDataSelections.getAllEmptySourceReferenceRows(),dbContext, head);


        SelectionBuilder<Enumeration> customEnums = SelectionBuilder.create(Enumeration.class);
        customEnums.add(Conditions.gt(Enumeration.META.idno, String.valueOf(1000000)));
        Query<Enumeration> query = MasterFileSelections.Querys.getEnumerationsQuery(dbContext,customEnums);

        EnumerationEditor editor = null;
        for(Enumeration enumeration : query.execute()){
            try {


                editor = enumeration.createEditor().open(EditorAction.MODIFY);
                editor.delete();
                editor.commit();

            }catch (CommandException e ){

            }finally {
                if(editor != null && editor.active()){
                    editor.abort();
                    editor = null;
                }
            }
        }


    }




    @ScreenEventHandler(type =  ScreenEventType.ENTER)
    public void screenEnter(TestsysClass head){
     head.setYmandkonfig(Utils.getMirrorConfig(Utils.WLAN_MANDANT_1));
    }


    @ButtonEventHandler(field = "start", type = ButtonEventType.AFTER)
    public void start(DbContext dbContext, ScreenControl screenControl, TestsysClass head) throws CommandException{

        if(head.getYmandkonfig() == null){
            Utils.showTextBox(Messages.MIRROR_KONFIG_CANT_BE_NULL);
        }
        MirrorKonfig mirrorKonfig = head.getYmandkonfig();
        DbContext targetContext = Utils.getServerContext(head.getYmandkonfig());
        Query<Enumeration> sourceEnumerations = MasterFileSelections.Querys
                .getEnumerationsQuery(dbContext,MasterFileSelections.getAllEnumerations());
        MirrorData mirrorData = MirrorDataSelections.Querys
                .getMirrorData(MirrorDataSelections.getMirrorData(mirrorKonfig),dbContext);
        if(mirrorData == null) {
            Utils.showTextBox("No Mirrordata found for given Konfig!!!");
            return;
        }
        Utils.writeRows(head,mirrorData,sourceEnumerations);



    }
    @ButtonEventHandler(field = "ytshow", type = ButtonEventType.AFTER,table =  true)
    public void showRow(DbContext dbContext, ScreenControl screenControl, TestsysClass head, TestsysClass.Row row) throws CommandException{

        if(head.getYmandkonfig() == null || row.getYtmiirrordata() == null){
            Utils.showTextBox(Messages.MIRROR_KONFIG_CANT_BE_NULL);
        }
        MirrorKonfig mirrorKonfig = head.getYmandkonfig();
        MirrorData mirrorData = row.getYtmiirrordata();
        MirrorTable mirrorTable = MirrorTableSelections.getMirrorTableForData(mirrorData,dbContext);
        Enumeration enumeration = (Enumeration) row.getYtenumsource();

        if(mirrorTable == null)return;
        final String mirrorId = row.getYtmirrorid();
        //filteredContainer
        List<Enumeration.Row> rows = null;
        final HashMap<String, MirrorTable.Row> container = MirrorOperations.Table.getMirrorSwdRowHashMap(mirrorTable, mirrorId);
        if(enumeration != null) rows = enumeration.table().getRows();

        Utils.writeExpandingRows(row,container, rows);



    }




    @ButtonEventHandler(field =  "ydeletemirrordata", type = ButtonEventType.AFTER)
    public  void resetMirrorData( TestsysClass head, DbContext dbContext){
        MirrorOperations.resetData(head.getYmandkonfig(),Enumeration.class.getSimpleName().toUpperCase(), dbContext);
    }


    @ButtonEventHandler(field = "ycreatemirrordata", type = ButtonEventType.AFTER)
    public void startMirrorDataCreation(DbContext dbContext, TestsysClass head) throws CommandException{
        if(head.getYmandkonfig() == null){
            Utils.showTextBox(Messages.MIRROR_KONFIG_CANT_BE_NULL);
        }
        DbContext erp2Context = Utils.getServerContext(head.getYmandkonfig());

        Query<Enumeration> erp2ContextQuery = MasterFileSelections.Querys.getEnumerationsQuery
                (erp2Context,MasterFileSelections.getAllEnumerations());

        MirrorData mirrorData = null;
        MirrorTable mirrorTable = null;
        for(Enumeration enumeration : erp2ContextQuery.execute()){
            mirrorData = createMirrorData(enumeration, head);
        }
        if(mirrorData == null){
            return;
        }
        MirrorOperations.Data.setSourceReferenceMirrorData(mirrorData);
        mirrorTable = createMirrorTable(mirrorData,head);
        if(mirrorTable == null){
           return;
       }
       setSourceReferenceMirrorTable(mirrorTable);


    }











}


