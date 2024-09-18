# Coroutines

## Basic Concepts

- Coroutines in Kotlin are a powerful tool for writing asynchronous and non-blocking code in a straightforward and sequential style.
- Coroutine make it easy to manage concurrency, handle background task and simplifly asynchronous programming.
- Coroutine make it easy to perform other tasks, while doing long running task.

### CoroutineDispatcher
- It define on which thread the coroutine will be executed (e.g., Main, I/O thread etc).
- It execute coroutine on a specific thread or group of threads.
- Single coroutine can use multiple CoroutineDispatcher using `withContext()`
- Dispatchers.Main is a special CoroutineDispatcher that is used for UI thread in Android application. 

### Suspended Function
- Tasks related to long running operation are defined inside this funciton.
- This function which can be executed inside Coroutine directly or by another suspended function.
- This function can paused and resume later.
- Parent Coroutine will wait for suspended function to complete before proceeding to the next step. (e.g., The CoroutineScope.Launch will wait for suspended function to complete before running the code after that suspended funciton.)
- When more than one suspended functions are launch inside same Coroutine scope, then these functions are executed one by one. Suspended function need to wait for current Suspended function to complete their execution.
- Suspending function can suspend the execution, entering waiting state but withour blocking the calling thread.

### Job
- Each Coroutine is associated with job.
- Coroutine job is cancellable.
- Cancellation of Coroutine's job cancel the cancel the Coroutine itself.
- When coroutine is cancelled the code after the suspended function inside coroutineScope.launch{} will not execute.

### CoroutineScope
- It is used to launch the Coroutine.
- CoroutineScope controls the lifecycle of the Coroutine.
- More than one Coroutine can be associated with CoroutineScope at any time.
- CoroutineScope can cancel all its children Coroutines.


## Coroutine Scope Cancellation

### Two ways to cancel all CoroutineScope's children
1. coroutineScope.coroutineContext.cancelChildren
2. coroutineScope.cancel

### Scope Cancellation vs Scope's Children Cancellation
- When Coroutine scope is cancelled itself, It will not able to re launch again.
- When children Coroutines are cancelled, the coroutine are able to re launch again.

### ViewmodelScope Kotlin Extensions
- It is recommended to use viewmodelScope instead of coroutine scope inside Viewmodel class.
- viewmodelScope handles the cancellations of jobs based on the lifecycle of Voewmodel. With CoroutineScope we need to explicitly cancel the job.

### Cancellation Exception
- When a `coroutineScope.Launch()` is cancelled, the suspended functions with default dispatchers still continue its execution but once the execution is completed it will through an exception because where this suspended function was launched has been cancelled.
- When a `coroutineScope.Launch()` is cancelled, the suspended functions with default dispatchers also need to be stopped or cancelled. With the help of `ensureActive()` or `isActive` we can meke the suspended function stop its execution.
- When a suspended function is not dispatched into a separate dispatchers (e.g., Dispatchers.Default) then when the `coroutineScope.Launch()` is cancelled, it will not throw cancellable exception.
- In some cases, you will need to use `NonCancellable` to protect critical part of your code from being cancelled during execution.
- `NonCancellable` is detaching `withContext` from its parent job.
- `NonCancellable` is only designed for `withContext`.


## Structured Concurrency

- Ability to pause the code execution and wait for all concurrent flows which can be traced back to a specific ancestor to complete.

## Main Design Rule for Concurrent Code in Android

- Presentation layer or UI related logic should use UI thread.
- For long running task or multithreading create a separate class (e.g., UseCase, Repository etc.)


## Dispatchers
- It control on which threads the Coroutine code will be executed.

### Standard Dispatchers in Android
1. Dispatchers.Main
2. Dispatchers.Main.Immediate
3. Dispatchers.Default
4. Dispatchers.IO
5. Dispatchers.Unconfined

### Dispatchers.Main
- It executes the code on the UI (main) thread of Android application.
- When a coroutine is dispatched using Dispatchers.Main, it might not execute immediately if the main thread is busy. Instead, it is added to the main thread's event queue and will execute once the main thread is free.
- This is typically used when you want to ensure that a coroutine is executed on the main thread, but you do not necessarily require it to run immediately if the main thread is currently handling other tasks.

### Dispatchers.Main.Immediate
- Dispatchers.Main.Immediate also execute the code on the UI thread but the code is executed immediately instead of putting task into thread queue.
- When a coroutine is dispatched using Dispatchers.Main, and the current thread is the main thread then the coroutine will be executed right away without being enqueued.
- However, if the current thread is not the main thread, it behaves just like Dispatchers.Main and will be enqueued on the main thread.
- This is useful when you are already on the main thread and want to avoid the overhead of enqueuing the coroutine on the event queue, ensuring that it runs as soon as possible.

### Dispatchers.Default
- It is designed for CPU-intensive tasks, such as sorting large datasets, performing complex calculations, or running tasks that require significant processing power.
- It is used for a heavy task but don't involve blocking I/O operations. For example, image processing, data transformation, or running algorithms.

### Dispatchers.IO
- is optimized for I/O-bound operations, such as reading or writing files, making network requests, or interacting with databases.
- deal for tasks that involve blocking I/O operations, where the thread may be idle waiting for external resources (like disk, network, or database). For example, fetching data from an API, reading files, or performing database queries.

### Dispatchers.Unconfined
- tarts the coroutine in the current thread, but it does not confine it to a specific thread. This means that the coroutine can resume on a different thread after suspension.


## Coroutine Builders
- Following are the coroutine buolders:
  1. Coroutine.Launch
  2. Coroutine.async

- Both the builders are used to run coroutines with different purposes and behiviour

### Coroutine.Launch
- Used to launch a coroutine that does not return a result.
- It returns a `Job` object which used to manage or cancel the coroutine
- It is used when you want to perform a task that does not return a result.

### Coroutine.Async
- Used to launch a coroutine that does not return a result.
- It returns a `Deferred<T>` object, which is a future-like object the holds the result of the coroutine. You can call `.await()` on the deferred object to retrieve the results.
- It is used when you want to perform a task that returns a result.


## Parallel Decomposition

- It is a method used in parallel computing. It is used to break down large computational task into smaller task.

### Parallel Decomposition using Coroutines
  1. CoroutineScope.launch{}
  2. CoroutineScope.async{}

- Do not access shared mutable stage from concurrent Coroutines.


## Exception Handling in Coroutines

- When any of a child of **CoroutineScope** throws an uncaught exception, that child coroutine will be cancelled and the parent CoroutineScope will be cancelled. When the parent CoroutineScope is cancelled all its child will also be cancelled.
- In order to prevent CoroutineScope or its child from cancelling when one of its child fails or cancelled is to install SupervisorJob() into CoroutineScope.
- We can use **CoroutineExceptionHandler** to catch uncaught exceptions.
- When uncaught exception is thrown, the default handler handle it and the app crashes. When this uncaught exceptions is handled with **CoroutineExceptionHandler** the app does not crashes.
- The best way to handle exceptions in coroutine is to `try-catch` them locally and prevent their propagation to CoroutineScope.
- In Coroutine Async, exception is thrown in Deferred.await() function. 














