import matplotlib.pyplot as plt
import re
import numpy as np
import statsmodels.api as sm

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
            if(int(duration)<3500):
                duraciones.append(int(duration))
            #else:
                #duraciones.append(3500)
        

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


for i in range(13):
    media_verificadas.append(sum(verificadas[i])/len(verificadas[i]))

for i in range(13):
    media_confirmadas.append(sum(confirmadas[i])/len(confirmadas[i]))

for i in range(13):
    media_canceladas.append(sum(canceladas[i])/len(canceladas[i]))

for i in range(13):
    media_pendientes.append(sum(pendientes[i])/len(pendientes[i]))

a = 13
while(a<20):
    media_verificadas.append(148.2)
    media_confirmadas.append(0)
    media_canceladas.append(37.8)  
    media_pendientes.append(0)
    a+=1



plt.plot(media_verificadas, color='red', label='Verificadas')
plt.plot(media_confirmadas, color='blue', label='Confirmadas')
plt.plot(media_canceladas, color='green', label='Canceladas')
plt.plot(media_pendientes, color='orange', label='Pendientes')
plt.xlabel('Índice [200 milis]')
plt.ylabel('Media')
plt.title('Gráfico de Medias')

plt.legend()
plt.show()





print("Duraciones: ", len(duraciones))
print("media:" + str(np.mean(duraciones)) )
print("desvio" + str(np.std(duraciones)) )
plt.hist(duraciones, bins=80)
plt.xlabel('Duración (useg)')
plt.ylabel('Frecuencia')
plt.title('Histograma de Duraciones')
plt.show()
        
        

# Generate QQ plot
sm.qqplot(np.array(duraciones), line='s')

# Display the plot
plt.title('QQ Plot')
plt.show()


