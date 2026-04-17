import urllib.request
import zipfile
import sys
import os

url = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.10_7.zip"
file_name = "java_github.zip"
extract_dir = r"C:\java_jdk"

print(f"Creating extraction directory: {extract_dir}")
os.makedirs(extract_dir, exist_ok=True)

def reporthook(blocknum, blocksize, totalsize):
    readsofar = blocknum * blocksize
    if totalsize > 0:
        percent = readsofar * 1e2 / totalsize
        sys.stdout.write("\r%5.1f%% %*d / %d" % (percent, len(str(totalsize)), readsofar, totalsize))
        sys.stdout.flush()

print(f"Downloading JDK 17 to {file_name}...")
urllib.request.urlretrieve(url, file_name, reporthook)

print(f"\nExtracting to {extract_dir}...")
with zipfile.ZipFile(file_name, 'r') as zip_ref:
    zip_ref.extractall(extract_dir)

print("Cleaning up...")
os.remove(file_name)
print("Done!")
