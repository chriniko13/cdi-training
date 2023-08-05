package com.example.observer;

import com.example.observer.event.AsyncComputationEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AsyncComputationObserver implements Serializable {

    private static final Logger LOG = Logger.getLogger(AsyncComputationObserver.class);

    private transient ExecutorService workersPool;

    @PostConstruct
    void init() {
        workersPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
            private final AtomicInteger idx = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("worker-" + idx.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("closing workers...");
            workersPool.shutdown();
        }));
    }

    public void onAsyncComputation(@ObservesAsync AsyncComputationEvent event) {
        LOG.info("async computation received: " + event);

        final int workersToSpawn = event.workersToSpawn();

        final CountDownLatch latch = new CountDownLatch(workersToSpawn);

        final List<Runnable> tasksToExecute = IntStream.range(0, workersToSpawn)
              .boxed()
              .map(idx -> (Runnable) () -> {

                  // pseudo work to do
                  LOG.info("worker doing some work....");
                  try {
                      TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3) + 1);
                  } catch (InterruptedException ex) {
                      Thread.currentThread().interrupt();
                  }
                  // worker finished the pseudo work
                  latch.countDown();


                  // rendezvous point
                  try {
                      latch.await();
                  } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                  }

                  LOG.info("worker finished the work....");
              })
              .toList();

        final List<? extends Future<?>> futures = tasksToExecute.stream()
              .map(taskToExecute -> workersPool.submit(taskToExecute))
              .toList();

        for (final Future<?> future : futures) {
            try {
                future.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
