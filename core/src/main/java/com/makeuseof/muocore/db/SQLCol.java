package com.makeuseof.muocore.db;

/**
 * Created by Alisher Alikulov on 3/8/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class SQLCol {
    public final String name;
    public final Type type;
    public final boolean isPrimaryKey;

    public SQLCol(String name, Type type, boolean isPrimaryKey) {
        this.name = name;
        this.type = type;
        this.isPrimaryKey = isPrimaryKey;
    }

    enum Type {
        Int("INTEGER"),
        Text("TEXT");

        public final String name;

        Type(String name){
            this.name = name;
        }
    }
}
