import matplotlib.pyplot as plt

log_file = "log.txt"
durations = []
pendientes = [0]*12
confirmadas = [0]*12
canceladas = [0]*12
verificadas = [0]*12
t = []
tiempos = [0]*12
for i in range(12):
    tiempos[i] = i*200


with open(log_file, "r") as file:
    for line in file:
        if "DuraciÃ³n del programa: " in line:
            duration = line.split("DuraciÃ³n del programa: ")[1].split(" ms")[0]
            durations.append(int(duration))
        if "TamaÃ±o de la lista de Reservas Pendientes: " in line:
            pendiente = line.split("TamaÃ±o de la lista de Reservas Pendientes: ")[1].split("\n")[0]
            pendientes.append(int(pendiente))
        if "TamaÃ±o de la lista de Reservas Confirmadas: " in line:
            confirmada = line.split("TamaÃ±o de la lista de Reservas Confirmadas: ")[1].split("\n")[0]
            confirmadas.append(int(confirmada))
        if "TamaÃ±o de la lista de Reservas Canceladas: " in line:
            cancelada = line.split("TamaÃ±o de la lista de Reservas Canceladas: ")[1].split("\n")[0]
            canceladas.append(int(cancelada))
        if "TamaÃ±o de la lista de Reservas Verificadas: " in line:
            verificada = line.split("TamaÃ±o de la lista de Reservas Verificadas: ")[1].split("\n")[0]
            verificadas.append(int(verificada))

