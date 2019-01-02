package ru.rofleksey.intflex.runtime;

import java.util.ArrayList;
import java.util.HashMap;

import ru.rofleksey.intflex.expr.Declaration;
import ru.rofleksey.intflex.runtime.error.IntFlexError;
import ru.rofleksey.intflex.runtime.type.IntFlexObject;

public class IntFlexContext {
    final ArrayList<Declaration> execOrder;
    HashMap<String, IntFlexObject> calcs = new HashMap<>(), system = new HashMap<>();
    Result result = new Result();
    int execIter = 0;

    volatile boolean interruptFlag = false;

    IntFlexContext(ArrayList<Declaration> execOrder) {
        this.execOrder = execOrder;
        //System.out.println(execOrder);
    }

    public IntFlexObject get(String name) throws IntFlexError {
        if (calcs.containsKey(name)) {
            return calcs.get(name);
        } else if (system.containsKey(name)) {
            return system.get(name);
        } else {
            throw new IntFlexError("IntFlexContext.get() failed");
        }
    }

    public void put(String name, IntFlexObject o) {
        calcs.put(name, o);
    }

    public void putSys(String name, IntFlexObject o) {
        system.put(name, o);
    }

    public void addToResult(IntFlexObject o) {
        result.add(this, o);
    }

    public Result getResult() {
        return result;
    }

    public String varsString() {
        return calcs.toString();
    }

    void clear() {
        calcs.clear();
    }

    void interrupt() {
        interruptFlag = true;
    }

    public void checkInterrupt() throws IntFlexError {
        if (interruptFlag) {
            throw new IntFlexError("Interrupted");
        }
    }

    public void enterNext() throws IntFlexError {
        if (execIter + 1 < execOrder.size()) {
            execIter++;
            execOrder.get(execIter).execute(this);
            execIter--;
        }
    }

    public void run() throws IntFlexError {
        if (!execOrder.isEmpty()) {
            execOrder.get(0).execute(this);
        }
    }
}
