package com.example.myscope;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Example usage:
 * <br/>
 *
 * <p>
 * <code>
 *     CustomAppScopeContext ctx = (CustomAppScopeContext) beanManager.getContext(CustomScoped.class);
 *     ctx.start(scopeId);
 *     try {
 *         // use scoped beans...
 *         ctx.suspend();
 *     } finally {
 *         ctx.destroy(scopeId);
 *     }
 * </code>
 * </p>
 *
 */
public class CustomAppScopeContext implements Context {

    private static final ThreadLocal<AtomicReference<String>> ACTIVE_SCOPE_THREAD_LOCAL =
          ThreadLocal.withInitial(AtomicReference::new);

    private final ConcurrentHashMap<String /*scopeId*/, Map<Contextual<?>, BeanInstance<?>>> instances =
          new ConcurrentHashMap<>();


    @Override
    public Class<? extends Annotation> getScope() {
        return CustomScoped.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        String scopeId = ACTIVE_SCOPE_THREAD_LOCAL.get().get();
        if (scopeId == null) {
            throw new ContextNotActiveException();
        }

        return (T) instances
              .computeIfAbsent(scopeId, sId -> new ConcurrentHashMap<>())
              .computeIfAbsent(contextual, c -> new BeanInstance<>(
                    contextual.create(creationalContext), contextual, creationalContext)
              )
              .get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Contextual<T> contextual) {
        String scopeId = ACTIVE_SCOPE_THREAD_LOCAL.get().get();
        if (scopeId == null) {
            throw new ContextNotActiveException();
        }

        Map<Contextual<?>, BeanInstance<?>> beansByScopeId = instances.getOrDefault(scopeId, Map.of());
        BeanInstance<?> beanInstance = beansByScopeId.get(contextual);
        if (beanInstance == null) {
            return null;
        }

        return (T) beanInstance.get();
    }

    @Override
    public boolean isActive() {
        return ACTIVE_SCOPE_THREAD_LOCAL.get().get() != null;
    }

    public void start(String scopeId) {
        AtomicReference<String> activeScope = ACTIVE_SCOPE_THREAD_LOCAL.get();
        if (activeScope.get() != null) {
            throw new IllegalStateException("An instance of the scope is already active");
        }
        activeScope.set(scopeId);
    }

    public void suspend(String scopeId) {
        AtomicReference<String> activeScope = ACTIVE_SCOPE_THREAD_LOCAL.get();
        if (activeScope.get() == null) {
            throw new IllegalStateException("Scope not currently active");
        }
        activeScope.set(null);
    }

    public void destroy(String scopeId) {
        instances.computeIfPresent(scopeId, (sId, beansByScopeId) -> {

            beansByScopeId.values().forEach(BeanInstance::destroy);
            return null;
        });
    }


}
