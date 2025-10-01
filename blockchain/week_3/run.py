# https://docs.google.com/document/d/10jKekVILHgA_icI3lMAvEit9MjtfgP5n/edit?usp=sharing&ouid=101115313909685436283&rtpof=true&sd=true

import hashlib
import time


def proof_of_work(block_data, difficulty=1):
    prefix = "0" * difficulty   # target pattern
    nonce = 0
    while True:
        text = f"{block_data}{nonce}"
        hash_result = hashlib.sha256(text.encode()).hexdigest()
        # TODO: check if hash_result starts with the right prefix
        if hash_result.startswith(prefix):
            return nonce, hash_result
        nonce += 1



start = time.time()
nonce, hash_result = proof_of_work("Alice pays Bob 1 BTC", difficulty=6)
end = time.time()

print("Nonce:", nonce)
print("Hash:", hash_result[:15])
print("Time taken:", end - start, "seconds")