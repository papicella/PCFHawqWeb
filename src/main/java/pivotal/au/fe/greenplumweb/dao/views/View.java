package pivotal.au.fe.greenplumweb.dao.views;

public class View
{
    public String schemaName;
    public String viewName;
    public String type;
    public String owner;
    public String storage;

    public View()
    {
    }

    public View(String schemaName, String viewName, String type, String owner, String storage) {
        this.schemaName = schemaName;
        this.viewName = viewName;
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

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
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
        return "View{" +
                "schemaName='" + schemaName + '\'' +
                ", viewName='" + viewName + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", storage='" + storage + '\'' +
                '}';
    }
}
