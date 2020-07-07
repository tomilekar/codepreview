package de.abas.cons.abastl.Selection;

import de.abas.cons.abastl.Utils;
import de.abas.erp.common.type.IdImpl;
import de.abas.erp.common.type.enums.EnumScreenWriteProtect;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.RowQuery;
import de.abas.erp.db.exception.CommandException;
import de.abas.erp.db.field.Field;
import de.abas.erp.db.infosystem.custom.owst.TestsysClass;
import de.abas.erp.db.schema.custom.mirroring.MirrorData;
import de.abas.erp.db.schema.custom.mirroring.MirrorDataEditor;
import de.abas.erp.db.schema.custom.mirroring.MirrorKonfig;
import de.abas.erp.db.schema.enumeration.Enumeration;
import de.abas.erp.db.schema.enumeration.EnumerationEditor;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.RowSelectionBuilder;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

import java.util.List;

public class MirrorDataSelections {

    public static SelectionBuilder<MirrorData> getMirrorData(MirrorKonfig mirrorKonfig){
        return getAllMirrorData()
                .add(Conditions.eq(MirrorData.META.ymandantkonfig,mirrorKonfig));
    }
    public static void fillAllFields (MirrorData.Row row, DbContext sourceDbContext, TestsysClass head) throws CommandException {

        DbContext dbContext = Utils.getServerContext(head.getYmandkonfig());

        Enumeration target = dbContext.load(Enumeration.class,new IdImpl(row.getYmirrorid()));
        EnumerationEditor source = sourceDbContext.newObject(EnumerationEditor.class) ;
        for(Field field : Enumeration.META.fields()){

            if(field.getGermanName().equals("such")){
                source.setSwd(target.getSwd());
            }
            if(field.getGermanName().equals("such")){
                source.setClassName(target.getClassName());
            }
            if(field.getGermanName().equals("tabart")){
                source.setRefType(target.getRefType());
            }
            if(field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){

                sourceDbContext.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

                source.setString(field,target.getString(field));


            }
            if(!field.isModifiable() && field.getScrProtection().getDisplayString().equals(EnumScreenWriteProtect.Editable)){
                sourceDbContext.out().println("Primary " + field.getPrimaryName()
                        + " Qualified " + field.getQualifiedName() + " Name" + field.getName());

            }
        }
        source.commit();

        MirrorDataEditor.Row row1 = row.header().createEditor().open(EditorAction.MODIFY).table().getRow(row.getRowNo());

        row1.setYsourcereference(source.objectId());
        row1.header().commit();

    }
    public static void createAllEmptySourceReferenceRows(RowSelectionBuilder<MirrorData, MirrorData.Row> sel, DbContext dbContext, TestsysClass head) throws CommandException{

        RowQuery<MirrorData, MirrorData.Row> rowQuery = dbContext.createQuery(sel.build());
        for(MirrorData.Row row : rowQuery.execute()){

            fillAllFields(row,dbContext, head);
        }
    }
    public static RowSelectionBuilder<MirrorData, MirrorData.Row> getAllEmptySourceReferenceRows(){
        return RowSelectionBuilder.create(MirrorData.class,MirrorData.Row.class)
                .add(Conditions.empty(MirrorData.Row.META.ysourcereference ));
    }

    public static SelectionBuilder<MirrorData> getAllMirrorData(){
        return SelectionBuilder.create(MirrorData.class);
    }

    public static MirrorData getMirrorDataTypeMandant(String type, MirrorKonfig mirrorKonfig, DbContext dbContext){
       if(mirrorKonfig == null || dbContext == null || type.isEmpty()) return null;
        return
                QueryUtil.getFirst(dbContext,
                        SelectionBuilder.create(MirrorData.class)
                        .add(Conditions.eq(MirrorData.META.ymandantkonfig, mirrorKonfig))
                                .add(Conditions.eq(MirrorData.META.swd,type))
                        .build()
                );

    }


    public static class Querys {
        public static List<MirrorData.Row> getResults(RowSelectionBuilder<MirrorData, MirrorData.Row> sel,DbContext dbContext){
            if(dbContext == null || sel == null) return null;
            List<MirrorData.Row> rows = dbContext.createQuery(sel.build()).execute();
            return rows;
        }

        public static MirrorData getMirrorData(SelectionBuilder<MirrorData> sel, DbContext dbContext){
            if(dbContext == null || sel == null) return null;
            return QueryUtil.getFirst(dbContext, sel.build());
        }
    }
}
