<img width="771" height="232" alt="image" src="https://github.com/user-attachments/assets/9290822a-9712-4108-bcd2-0f4543b3d49b" />

---

⭐ COMPLETE MODES OF OPERATION TABLE (Final Revision)
| **Property ↓**        | **ECB**                          | **CBC**                           | **OFB**                                      | **CFB**                             | **CTR**                          |
| --------------------- | -------------------------------- | --------------------------------- | -------------------------------------------- | ----------------------------------- | -------------------------------- |
| **Patterns**          | – (patterns visible)             | + (patterns hidden)               | +                                            | +                                   | +                                |
| **Repetitions**       | – (repeats if plaintext repeats) | IV (no repetition unless IV same) | IV                                           | IV                                  | IV / nonce                       |
| **Block length**      | *n*                              | *n*                               | *j*                                          | *j*                                 | *n*                              |
| **Error propagation** | 1 block                          | 1 block + 1 bit                   | 1 bit (no propagation)                       | *n* bits + 1 bit                    | 1 block (no propagation)         |
| **Synchronization**   | block (asynchronous)             | block (self-sync)                 | exact (NOT self-sync; needs exact alignment) | *j* bits (self-sync after *n* bits) | block (NOT self-sync)            |
| **Parallel**          | enc/dec                          | dec only                          | enc/dec                                      | dec only                            | enc/dec (fully parallel)         |
| **Application**       | key encryption / random blocks   | default mode                      | no error propagation                         | synchronous stream                  | parallel processing / high speed |
