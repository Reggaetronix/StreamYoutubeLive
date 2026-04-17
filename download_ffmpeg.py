import urllib.request
import zipfile
import sys
import os

url = "https://github.com/arthenica/ffmpeg-kit/releases/download/v6.0/ffmpeg-kit-full-6.0-android-java.zip"
file_name = "ffmpeg-kit.zip"
extract_dir = r"libs_temp"

print(f"Creating extraction directory: {extract_dir}")
os.makedirs(extract_dir, exist_ok=True)

def reporthook(blocknum, blocksize, totalsize):
    readsofar = blocknum * blocksize
    if totalsize > 0:
        percent = readsofar * 1e2 / totalsize
        sys.stdout.write("\r%5.1f%% %*d / %d" % (percent, len(str(totalsize)), readsofar, totalsize))
        sys.stdout.flush()

print(f"Downloading AAR zip from {url}...")
urllib.request.urlretrieve(url, file_name, reporthook)

print(f"\nExtracting to {extract_dir}...")
with zipfile.ZipFile(file_name, 'r') as zip_ref:
    zip_ref.extractall(extract_dir)

print("\nFiles in extracted zip:")
for root, dirs, files in os.walk(extract_dir):
    for f in files:
        print(os.path.join(root, f))

print("Done!")
