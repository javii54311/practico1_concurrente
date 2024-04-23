import numpy as np
log_file = "slep50.txt"
duraciones = []

with open(log_file, 'r') as file:    
    for line in file:
        if "Duración del programa" in line:
            duration = line.split(": ")[-1].split(" ")[0]
            duraciones.append(int(duration))

print("Duraciones: ", len(duraciones))
print("media: ", np.mean(duraciones))

    