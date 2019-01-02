package ru.rofleksey.intflex.runtime.namespaces;

import ru.rofleksey.intflex.runtime.IntFlexContext;

public class Injectors implements NamespaceInjector {
    @Override
    public void inject(IntFlexContext context) {
        NamespaceInjector[] injs = new NamespaceInjector[]{
                new MathInjector()
        };
        for (NamespaceInjector ni : injs) {
            ni.inject(context);
        }
    }
}
