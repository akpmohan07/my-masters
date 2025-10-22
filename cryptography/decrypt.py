import re
from collections import Counter

ciphertext = """QVUEVAIOHJCFMINRCFIYTMPZTFGVKIEKSQAVLOXWVBEKCEOXTEXPKIQVXRHTRQI
SPZYBRSWEHJPOGHJYDXJGEJTWPHXEXRIESKVITHVJDVGXBMAMEBXQWEPIMMHS
KVEIAIFFCPTPHIMMPRFVEJTXQUIZPPNXHMXTKZMSTTKKVFEEUFCCPXJXVPUEOBP
VCEOXSKILGTYQWSTBXVDJJBWQDQDLXLCICGHJNWKLXBGQTLNLTKCKKBGCYASJ
PVTBIAILGUPXROUFMQWEUBRBKITLETBCHTXETVQKQVBSVAIOPRFGISTVUTAXCC
NBOBCIULSCTMVAIODJVAIJUSTMLBXVFTCPLITXPLCKDXJLGIVAIAPCUHJMWSVHKO
PTJLQVUMTLXCPREBIPGIITVAXRIPLXIXJXCTTVGEMHTAGKIRCVGTWLCEDECATVKO
IAUVQFXETMTMSJQWVHRBHXJXWEPTGHJQWINXXQTVUHRJNJCMLBGWITZBBICGS
ASMFXEQWEVAITPWCLURPVGLXLJXFTVHBEPPMQWGWKPVQPCVOEPMTYVLBXJX
GEPVCVXBGEPWXRGRQYXETMPLGOXTVBSKPPUHKBDVIBEKPAKYILUXJXEYDZGBH
OTACVLFAHKLLZDREEYPXSPMLXIQAFSQWITPEPUVGVOITHCGHPXGMECQDJKOIIX
XVEIPISPXPLOIPZIPTEEAEYDYVTJLDXCGHXWENYPLCKYAMZWAGKIXGVCGKBSMP
TRBPXTHAYTWKWIQWIKKKOPZGTRALITXWXRVGWXLILGFIJDVAHJCXZGEMQIPGUV
LILGKWLUQKGITWSITZBJTVKCFCKVHKBIENBZFCKGQGBTHKGKINICKPVXRVAEQJR
KOIOHENLXOJKIEIFPQKGHBQXGWJLGEDXPFTJKKIIXKKHYPACGGXBGXCBRBSXJTX
QWIAAEAPPNUIBCFQKRLCXJXMOQEEDWTXXJMLBXVJTRAHMPMLBXVVKSRHITLTL
ROGMWXCHJTHKTZGKXXZIPMLBBSWMMKILKLWQPXGHJBMMUMIKRIQNVPLEUMLB
BETLLZDYPMVVSSYGFVILGKMSTVYBXEXRCLXETVKOIOLSWGHQLIPMCJXPGLSCIL
GLIXBCHBVPIQQLXSXZKWEKSFTHEAXQRKIPHMQGSCILGBHBCXKMCLUXJBRDHWG
XQPISOXXLWEXXFBTRITMKTHQGEJTQQKEYAITTAXUXGKRLDRVHAXGHUXZBCMPZ
EQHYEAEQXQGBJLJRFHYQUSTVIOIEKGXEPXVAMPQPGTOMAEEXSSTVIKSTCAKML
KTXVEIPLEUMLBRLWKGENETWEKSXJTXMWMNBTMXVTBTIPXGHJQWMUIEOXWJTR
APPUHKBDVIBEKPAKYILUXJXEYDZGPIOTHGTHXCHDNVFTHCGHQWEVTPBMEPWIO
QETMLLASOXAXQVCAEJISDBEPPRFKSDTVKGJXCXEAMISVGGSCILGTJLGIUTMALIT
XEIHSFXEAPRFUYOXIFTRAILCMXETHCKOCAEVPMISITGIPHFGRSKSXJXGEJVEACX
GHKGXBGWGVXBSAKMLANOGLEKSQQNRAHEPWKXIIUPMQWWETXQTVGWGXIXNX
JBTHKGKLCMVPEPILGFEOHLGLEKSXJTXQWINHAITEFXRIXRGUIVDRFPEPILGKMSTV
CGHQWEVMLBSMUMEKIWCOEDTPCBVCGSOPLFRLVAITXRFPEPGYUAMKVACLXET
WGTEKSXJTXQWIUFEIAFWGHITSHLLFKITLKODAKGKXUVCBHLUMVTPIPRFUIDXRPB
RDISEKCTPWRBT"""

# Remove whitespace and newlines
ciphertext = re.sub(r'\s', '', ciphertext)
print(f"Ciphertext length: {len(ciphertext)}")

# Function to calculate Index of Coincidence
def index_of_coincidence(text):
    n = len(text)
    freq = Counter(text)
    ic = sum(f * (f - 1) for f in freq.values()) / (n * (n - 1))
    return ic

# Function to find key length
def find_key_length(ciphertext, max_length=20):
    ic_values = {}
    for length in range(1, max_length + 1):
        subsequences = [''] * length
        for i, char in enumerate(ciphertext):
            subsequences[i % length] += char
        
        avg_ic = sum(index_of_coincidence(seq) for seq in subsequences) / length
        ic_values[length] = avg_ic
        print(f"Key length {length}: IC = {avg_ic:.4f}")
    
    return ic_values

# Find likely key length
ic_values = find_key_length(ciphertext, 20)

# Function to find the key for a given length
def find_key(ciphertext, key_length):
    key = []
    for i in range(key_length):
        subsequence = ciphertext[i::key_length]
        
        # Try all possible key characters
        best_chi_squared = float('inf')
        best_char = 'A'
        
        english_freq = {'E': 0.127, 'T': 0.091, 'A': 0.082, 'O': 0.075, 'I': 0.070,
                       'N': 0.067, 'S': 0.063, 'H': 0.061, 'R': 0.060, 'D': 0.043,
                       'L': 0.040, 'C': 0.028, 'U': 0.028, 'M': 0.024, 'W': 0.024,
                       'F': 0.022, 'G': 0.020, 'Y': 0.020, 'P': 0.019, 'B': 0.015,
                       'V': 0.010, 'K': 0.008, 'J': 0.002, 'X': 0.002, 'Q': 0.001,
                       'Z': 0.001}
        
        for shift in range(26):
            decrypted = ''
            for char in subsequence:
                decrypted += chr((ord(char) - ord('A') - shift) % 26 + ord('A'))
            
            # Calculate chi-squared statistic
            freq = Counter(decrypted)
            chi_squared = 0
            for letter in 'ABCDEFGHIJKLMNOPQRSTUVWXYZ':
                expected = len(decrypted) * english_freq.get(letter, 0.001)
                observed = freq.get(letter, 0)
                chi_squared += (observed - expected) ** 2 / expected
            
            if chi_squared < best_chi_squared:
                best_chi_squared = chi_squared
                best_char = chr(shift + ord('A'))
        
        key.append(best_char)
    
    return ''.join(key)

# Try key length 7 (highest IC)
key_length = 7
key = find_key(ciphertext, key_length)
print(f"\nFound key (length {key_length}): {key}")

# Decrypt with found key
def vigenere_decrypt(ciphertext, key):
    plaintext = []
    key_length = len(key)
    for i, char in enumerate(ciphertext):
        shift = ord(key[i % key_length]) - ord('A')
        decrypted_char = chr((ord(char) - ord('A') - shift) % 26 + ord('A'))
        plaintext.append(decrypted_char)
    return ''.join(plaintext)

plaintext = vigenere_decrypt(ciphertext, key)
print(f"\nDecrypted text:\n{plaintext}")