package de.abas.cons.abastl.Selection;

import de.abas.cons.abastl.Utils;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.RowQuery;
import de.abas.erp.db.schema.custom.mirroring.MirrorData;
import de.abas.erp.db.schema.custom.mirroring.MirrorTable;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.RowSelectionBuilder;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.QueryUtil;

public class MirrorTableSelections {

    private MirrorTableSelections(){

    }

    public static SelectionBuilder<MirrorTable> getAllMirrorTables(){
        return SelectionBuilder.create(MirrorTable.class);

    }

    public static RowSelectionBuilder<MirrorTable, MirrorTable.Row> getAllEmptySourceReferenceRows(){
        RowSelectionBuilder<MirrorTable, MirrorTable.Row> sel = RowSelectionBuilder.create(MirrorTable.class, MirrorTable.Row.class);
                sel.setTermConjunction(SelectionBuilder.Conjunction.OR)
                .add(Conditions.empty(MirrorTable.Row.META.ytsourcereference)).add(Conditions.empty(MirrorTable.Row.META.ysourceenumref));

                return sel;
    }



    public static MirrorTable getMirrorTableForData(MirrorData mirrorData, DbContext dbContext){
        if(mirrorData == null || dbContext == null) return null;
        Utils.writeMessage("Getting MirrorTable Data");
        return QueryUtil.getFirst(dbContext,SelectionBuilder.create(MirrorTable.class)
                .add(Conditions.eq(MirrorTable.META.ymirrorhead,mirrorData))
                .add(Conditions.eq(MirrorTable.META.swd,mirrorData.getSwd()))
                .build());

    }


    public static class Query {
        public static RowQuery<MirrorTable, MirrorTable.Row> getRowQuery(RowSelectionBuilder<MirrorTable, MirrorTable.Row> sel, DbContext sourceC)
        {
            return sourceC.createQuery(sel.build());
        }
    }
}
