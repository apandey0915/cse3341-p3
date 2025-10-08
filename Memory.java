import java.util.*;

final class Memory {
    enum Kind { INT, OBJ }
    static final class Cell {
        Kind kind;
        Integer ival; // for INT
        Map<String,Integer> obj; // for OBJ
        final String name; // identifier (used as default key for OBJ)
        Cell(Kind k, String name) { 
            this.kind = k;
            this.name = name;
            this.ival = 0;
            this.obj = null;
        }
    }

    // global + local scopes
    private static final Map<String,Cell> globals = new HashMap<>();
    private static final Deque<Map<String,Cell>> locals = new ArrayDeque<>();

    static void enterScope() {
        locals.push(new HashMap<>());
    }

    static void exitScope() {
        locals.pop();
    }

    static void declInt(String id, boolean global) {
        getScope(global).put(id, new Cell(Kind.INT, id));
    }

    static void declObj(String id, boolean global) {
        getScope(global).put(id, new Cell(Kind.OBJ, id));
    }

    private static Map<String,Cell> getScope(boolean global) {
        if (global || locals.isEmpty()) {
            return globals;
        }
        return locals.peek();
    }

    private static Cell lookup(String id) {
        for (Map<String,Cell> s : locals) {
            if (s.containsKey(id)) {
                return s.get(id);
            }
        }
        Cell c = globals.get(id);
        if (c == null) {
            throw new RuntimeException("Runtime error: undeclared identifier " + id);
        }
        return c;
    }

    // INT get/set
    static int getInt(String id) {
        Cell c = lookup(id);
        if (c.kind != Kind.INT) {
            throw new RuntimeException("Type error: " + id + " is not integer");
        }
        return c.ival;
    }

    static void setInt(String id, int v) {
        Cell c = lookup(id);
        if (c.kind != Kind.INT) {
            throw new RuntimeException("Type error: " + id + " is not integer");
        }
        c.ival = v;
    }

    // OBJ helpers
    static Map<String,Integer> getObjRef(String id) {
        Cell c = lookup(id);
        if (c.kind != Kind.OBJ) {
            throw new RuntimeException("Type error: " + id + " is not object");
        }
        return c.obj; // may be null
    }

    static void setObjRef(String id, Map<String,Integer> ref) {
        Cell c = lookup(id);
        if (c.kind != Kind.OBJ) {
            throw new RuntimeException("Type error: " + id + " is not object");
        }
        c.obj = ref;
    }

    static void newObject(String id, String defaultKey, int initVal) {
        Cell c = lookup(id);
        if (c.kind != Kind.OBJ) {
            throw new RuntimeException("Type error: " + id + " is not object");
        }
        Map<String,Integer> m = new HashMap<>();
        m.put(defaultKey, initVal);
        c.obj = m;
    }

    static int getObjKey(String id, String key) {
        Map<String,Integer> m = getObjRef(id);
        if (m == null) {
            throw new RuntimeException("Null object: " + id);
        }
        Integer v = m.get(key);
        if (v == null) {
            throw new RuntimeException("Missing key '" + key + "' in object " + id);
        }
        return v;
    }

    static void setObjKey(String id, String key, int v) {
        Map<String,Integer> m = getObjRef(id);
        if (m == null) {
            throw new RuntimeException("Null object: " + id);
        }
        m.put(key, v);
    }

    static int getObjDefault(String id) {
        return getObjKey(id, lookup(id).name);
    }

    static void setObjDefault(String id, int v) {
        setObjKey(id, lookup(id).name, v);
    }
}
