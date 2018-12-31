package ru.rofleksey.intflex.expr;

import java.util.Set;

public abstract class Dependable {
    private Set<String> dependencies;
    private boolean isConstant;


    public abstract Set<String> calcDependencies();

    public Set<String> getDependencies() {
        return dependencies == null ? dependencies = calcDependencies() : dependencies;
    }

    public void calcIsConstant() {
        for (String s : dependencies) {

        }
    }

    @Override
    public String toString() {
        return dependencies.toString();
    }
}
