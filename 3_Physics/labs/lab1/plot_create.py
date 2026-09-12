import numpy as np
import matplotlib.pyplot as plt

data = np.array([312, 245, 288, 356, 210, 275, 298, 330, 262, 241, 305, 284, 350, 220, 267, 295, 315, 255, 290, 335, 230, 272, 301, 260, 325, 248, 285, 340, 215, 278, 308, 252, 320, 265, 292, 345, 225, 282, 310, 258, 328, 238, 288, 302, 270, 338, 205, 275, 318, 244])

N = len(data)
mean = np.mean(data)
std = np.std(data, ddof=1)

bin_count = int(np.sqrt(N))
counts, bin_edges = np.histogram(data, bins=bin_count)
bin_centers = (bin_edges[:-1] + bin_edges[1:]) / 2
bin_width = bin_edges[1] - bin_edges[0]

exp_density = counts / (N * bin_width)
theo_density = (1 / (std * np.sqrt(2 * np.pi))) * np.exp(-0.5 * ((bin_centers - mean) / std)**2)

x_vals = np.linspace(bin_edges[0], bin_edges[-1], 100)
gauss_curve = (1 / (std * np.sqrt(2 * np.pi))) * np.exp(-0.5 * ((x_vals - mean) / std)**2)

fig, ax1 = plt.subplots(figsize=(10, 6))
ax2 = ax1.twinx()

bar_width = bin_width * 0.95
ax1.bar(bin_centers, exp_density, width=bar_width, color='#8ab6d6', edgecolor='black', alpha=0.8, label='Экспериментальные данные (ΔN/NΔh)')
ax1.plot(x_vals, gauss_curve, color='red', linewidth=2, label=f'Подобранное распределение Гаусса\n(μ={mean:.2f}, σ={std:.2f})')
ax1.scatter(bin_centers, theo_density, color='red', s=50, zorder=5, label='Теоретическая плотность (ρ)')

ax1.set_xlabel('Время реакции h, мс', fontsize=12)
ax1.set_ylabel('Плотность вероятности', fontsize=12)
ax1.set_title('Диаграмма распределения времени реакции', fontsize=14)

def density_to_freq(density):
    return density * N * bin_width

def freq_to_density(freq):
    return freq / (N * bin_width)

ax2 = ax1.secondary_yaxis('right', functions=(density_to_freq, freq_to_density))
ax2.set_ylabel('Частота ΔN', fontsize=12)

ax1.grid(True, linestyle='--', alpha=0.6)
ax1.legend(loc='upper left', fontsize=10)

plt.tight_layout()
plt.savefig('docs/assets/histogram.png', dpi=300)
plt.show()