import matplotlib.pyplot as plt
import re

log_file = "log.txt"
duraciones = []

pendientes = [[] for _ in range(200)]
confirmadas = [[] for _ in range(200)]
canceladas = [[] for _ in range(200)]
verificadas = [[] for _ in range(200)]

with open(log_file, 'r') as file:    
    for line in file:
        if "Duración del programa" in line:
            duration = line.split(": ")[-1].split(" ")[0]
            duraciones.append(int(duration))

        match = re.search(r'(\d+).Tamaño de la lista de Reservas Pendientes: (\d+)', line)
        if match:
            # Extraer los números encontrados
            numeroA = int(match.group(1))
            numeroB = int(match.group(2))
            
            # Almacenar el número B en el arreglo correspondiente
            pendientes[numeroA // 200].append(numeroB)
        
        match = re.search(r'(\d+).Tamaño de la lista de Reservas Confirmadas: (\d+)', line)
        if match:
            # Extraer los números encontrados
            numeroA = int(match.group(1))
            numeroB = int(match.group(2))
            
            # Almacenar el número B en el arreglo correspondiente
            confirmadas[numeroA // 200].append(numeroB)
        
        match = re.search(r'(\d+).Tamaño de la lista de Reservas Canceladas: (\d+)', line)
        if match:
            # Extraer los números encontrados
            numeroA = int(match.group(1))
            numeroB = int(match.group(2))
            
            # Almacenar el número B en el arreglo correspondiente
            canceladas[numeroA // 200].append(numeroB)
        
        match = re.search(r'(\d+).Tamaño de la lista de Reservas Verificadas: (\d+)', line)
        if match:
            # Extraer los números encontrados
            numeroA = int(match.group(1))
            numeroB = int(match.group(2))
            
            # Almacenar el número B en el arreglo correspondiente
            verificadas[numeroA // 200].append(numeroB)
            
            
media_verificadas=[]
media_confirmadas=[]
media_canceladas=[]
media_pendientes=[]


for i in range(14):
    media_verificadas.append(sum(verificadas[i])/len(verificadas[i]))
    media_confirmadas.append(sum(confirmadas[i])/len(confirmadas[i]))
    media_canceladas.append(sum(canceladas[i])/len(canceladas[i]))
    media_pendientes.append(sum(pendientes[i])/len(pendientes[i]))

plt.plot(media_verificadas, color='red', label='Verificadas')
plt.plot(media_confirmadas, color='blue', label='Confirmadas')
plt.plot(media_canceladas, color='green', label='Canceladas')
plt.plot(media_pendientes, color='orange', label='Pendientes')
plt.xlabel('Índice')
plt.ylabel('Media')
plt.title('Gráfico de Medias')

plt.legend()
plt.show()






plt.hist(duraciones, bins=10)
plt.xlabel('Duración')
plt.ylabel('Frecuencia')
plt.title('Histograma de Duraciones')
plt.show()
        
        


