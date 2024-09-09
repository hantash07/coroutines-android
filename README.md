## Coroutines

### Basic Concepts
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


