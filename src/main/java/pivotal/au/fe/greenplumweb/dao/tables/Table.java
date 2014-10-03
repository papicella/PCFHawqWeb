package pivotal.au.fe.greenplumweb.dao.tables;

public class Table
{
    public String schemaName;
    public String tableName;
    public String type;
    public String owner;
    public String storage;

    public Table()
    {
    }

    public Table(String schemaName, String tableName, String type, String owner, String storage) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.type = type;
        this.owner = owner;
        this.storage = storage;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "Table{" +
                "schemaName='" + schemaName + '\'' +
                ", viewName='" + tableName + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", storage='" + storage + '\'' +
                '}';
    }
}
