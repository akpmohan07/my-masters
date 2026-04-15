# OpenMP

Open Multi-Processing

{% hint style="info" %}
Globally Sequential Locally Parallel (GSLP)
{% endhint %}

**implicit barrier** --> Parent thread waiting for the child thread to complete.

```
main thread
    |
    |--- #pragma omp parallel ---+
    |                            |
    |    thread 0   thread 1   thread 2
    |       |          |          |
    |      work       work       work
    |       |          |          |
    +------------ implicit barrier  --------+
    |
    | (parent continues here)
```





parallel regions  --> Actual thread execution

&#x20;constructs --> Pragma + Body



### Variable Scopes:

1. Shared&#x20;
   1. Outside parallel region
2. Private
   1. Inside a parallel region
3. Reduction
   1. Common between parallel and private scope



<pre class="language-java"><code class="lang-java"><a data-footnote-ref href="#user-content-fn-1">chunk = max(min_chunk, remaining / num_threads)</a>
</code></pre>





[^1]: 
