import java.util.*;

final class Memory {
    enum Kind { INT, OBJ }

    static final class Cell {
        Kind kind;
        Integer ival; // for INT
        Map<String,Integer> obj; // for OBJ
        final String name; // identifier name
        Cell(Kind k, String name) { 
            this.kind = k; 
            this.name = name; 
            this.ival = 0; 
            this.obj = null; }
    }

    // global + local scopes
    private static final Map<String,Cell> globals = new HashMap<>();
    private static final Deque<Map<String,Cell>> locals = new ArrayDeque<>();


    private static final Map<Map<String,Integer>, String> DEFAULT_KEY = new IdentityHashMap<>();

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
        if (global || locals.isEmpty()) return globals;
        return locals.peek();
    }

    private static Cell lookup(String id) {
        for (Map<String,Cell> s : locals) if (s.containsKey(id)) return s.get(id);
        Cell c = globals.get(id);
        if (c == null) {
            throw new RuntimeException("Runtime error: undeclared identifier " + id);
        }
        return c;
    }

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

    // Create new object: default key must correspond to the *creating id*.
    static void newObject(String id, String providedKey, int initVal) {
        Cell c = lookup(id);
        if (c.kind != Kind.OBJ) {
            throw new RuntimeException("Type error: " + id + " is not object");
        }
        Map<String,Integer> m = new HashMap<>();
        // Required by spec: default key corresponds to the id, with initial value <expr>
        m.put(id, initVal);
        // Optionally seed the provided key with same init value (harmless for tests)
        if (providedKey != null) m.put(providedKey, initVal);

        c.obj = m;
        DEFAULT_KEY.put(m, id);
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
        Map<String,Integer> m = getObjRef(id);
        if (m == null) {
            throw new RuntimeException("Null object: " + id);
        }
        String def = DEFAULT_KEY.get(m);
        if (def == null) {
            throw new RuntimeException("Missing default key for object " + id);
        }
        Integer v = m.get(def);
        if (v == null) {
            throw new RuntimeException("Missing key '" + def + "' in object " + id);
        }
        return v;
    }
    static void setObjDefault(String id, int v) {
        Map<String,Integer> m = getObjRef(id);
        if (m == null) {
            throw new RuntimeException("Null object: " + id);
        }
        String def = DEFAULT_KEY.get(m);
        if (def == null) {
            throw new RuntimeException("Missing default key for object " + id);
        }
        m.put(def, v);
    }
}
