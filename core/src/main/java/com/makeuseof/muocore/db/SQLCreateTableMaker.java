package com.makeuseof.muocore.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alisher Alikulov on 3/8/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class SQLCreateTableMaker {
    private StringBuilder query = new StringBuilder();
    private List<SQLCol> columns = new ArrayList<>();

    public SQLCreateTableMaker(String tableName) {
        query.append("CREATE TABLE ").append(tableName).append(" (");
    }

    public SQLCreateTableMaker addColumn(String name, SQLCol.Type type, boolean isPrimaryKey){
        columns.add(new SQLCol(name, type, isPrimaryKey));
        return this;
    }

    public SQLCreateTableMaker addInt(String name){
        columns.add(new SQLCol(name, SQLCol.Type.Int, false));
        return this;
    }

    public SQLCreateTableMaker addText(String name){
        columns.add(new SQLCol(name, SQLCol.Type.Text, false));
        return this;
    }

    public SQLCreateTableMaker addPrimaryKey(String name, SQLCol.Type type){
        columns.add(new SQLCol(name, type, true));
        return this;
    }

    public String make(){
        for(int i=0; i<columns.size();i++){
            SQLCol col = columns.get(i);
            query.append(col.name).append(" ").append(col.type.name);
            if(col.isPrimaryKey){
                query.append(" PRIMARY KEY ");
            }
            if(i < columns.size()-1){
                query.append(", ");
            }
        }
        query.append(")");
        return query.toString();
    }
}
