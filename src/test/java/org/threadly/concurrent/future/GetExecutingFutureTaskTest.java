package org.threadly.concurrent.future;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

import org.junit.Test;
import org.threadly.test.concurrent.TestRunnable;

@SuppressWarnings("javadoc")
public class GetExecutingFutureTaskTest extends ListenableFutureTaskTest {
  @Override
  protected FutureFactory makeFutureFactory() {
    return new Factory();
  }

  @Override
  protected <T> ListenableFutureTask<T> makeFutureTask(Runnable runnable, T result) {
    return new GetExecutingFutureTask<T>(runnable, result);
  }

  @Override
  protected <T> ListenableFutureTask<T> makeFutureTask(Callable<T> task) {
    return new GetExecutingFutureTask<T>(task);
  }
  
  @Test
  public void executeViaGetTest() throws InterruptedException, ExecutionException {
    TestRunnable tr = new TestRunnable();
    GetExecutingFutureTask<?> geft = new GetExecutingFutureTask<Object>(tr);
    
    geft.get();
    
    assertTrue(tr.ranOnce());
  }
  
  @Test
  public void executeOnceTest() throws InterruptedException, ExecutionException {
    TestRunnable tr = new TestRunnable();
    GetExecutingFutureTask<?> geft = new GetExecutingFutureTask<Object>(tr);
    
    geft.get();
    geft.run();
    geft.get(); // multiple get calls should not execute again either
    geft.run();
    
    assertTrue(tr.ranOnce());
  }
  
  private class Factory implements FutureFactory {
    @Override
    public RunnableFuture<?> make(Runnable run) {
      return new GetExecutingFutureTask<Object>(run);
    }

    @Override
    public <T> RunnableFuture<T> make(Runnable run, T result) {
      return new GetExecutingFutureTask<T>(run, result);
    }

    @Override
    public <T> RunnableFuture<T> make(Callable<T> callable) {
      return new GetExecutingFutureTask<T>(callable);
    }
  }
}
