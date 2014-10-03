package pivotal.au.fe.greenplumweb.dao.views;

public interface Constants
{
    public static final String USER_VIEWS =
            "SELECT n.nspname as \"Schema\", " +
                    "  c.relname as \"Name\", " +
                    "  CASE c.relkind WHEN 'r' THEN 'table' WHEN 'v' THEN 'view' WHEN 'i' THEN 'index' WHEN 'S' THEN 'sequence' WHEN 's' THEN 'special' END as \"Type\", " +
                    "  pg_catalog.pg_get_userbyid(c.relowner) as \"Owner\", CASE c.relstorage WHEN 'h' THEN 'heap' WHEN 'x' THEN 'external' WHEN 'a' THEN 'append only' WHEN 'v' THEN 'none' WHEN 'c' THEN 'append only columnar' WHEN 'f' THEN 'foreign' END as \"Storage\" " +
                    "FROM pg_catalog.pg_class c " +
                    "     LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                    "WHERE c.relkind IN ('v','') " +
                    "AND c.relstorage IN ('v','') " +
                    "  and n.nspname = ? " +
                    "  and c.relname like ? "+
                    "ORDER BY 1,2 ";

    public static final String USER_VIEWS_COUNT =
            "select object_type, count(*) " +
                    "from (select c.relname, 'View' as OBJECT_TYPE FROM pg_catalog.pg_class c " +
                    "     LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                    "WHERE c.relkind IN ('v','') " +
                    "AND c.relstorage IN ('v','') " +
                    "  and n.nspname = ? ) c group by object_type ";

    public static final String USER_VIEW_DEF =
            "select definition " +
            "from pg_views " +
            "where schemaname = ? " +
            "and   viewname = ? ";

    public static String DROP_VIEW = "drop view %s.%s";

    public static String DROP_VIEW_PUBLIC = "drop view %s";
}
