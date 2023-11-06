#!/usr/bin/python

from statistics import mean, median
from scipy import stats
import logging, argparse, csv, os, sys, re, datetime, json
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from matplotlib.lines import Line2D

logging.basicConfig(level=getattr(logging, "INFO", None))


cryptoACFile = "./EXP_1_CryptoAC.csv"
tlsFile = "./EXP_1_TLS.csv"
baselineFile = "./EXP_1_Baseline.csv"

# Check all files exist
logging.info("Checking that all files exist")
if (not os.path.exists(cryptoACFile)):
    logging.error("Results file " + cryptoACFile + " does not exist")
    sys.exit(1)
if (not os.path.exists(tlsFile)):
    logging.error("Results file " + tlsFile + " does not exist")
    sys.exit(1)
if (not os.path.exists(baselineFile)):
    logging.error("Results file " + baselineFile + " does not exist")
    sys.exit(1)


# Reading data from files
cryptoACResults = []
logging.info("Parsing file " + cryptoACFile)
with open(cryptoACFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        cryptoACResults.append(float(entry["Time"]))
tlsResults = []
logging.info("Parsing file " + tlsFile)
with open(tlsFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        tlsResults.append(float(entry["Time"]))
baselineResults = []
logging.info("Parsing file " + baselineFile)
with open(baselineFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        baselineResults.append(float(entry["Time"]))













# Plot

# Calculate the median values for each dataset
data = [cryptoACResults, tlsResults, baselineResults]

# Define colors for the boxplots
colors = ['#1f77b4', '#ff7f0e', '#2ca02c']

# Calculate the median value for each dataset
medians = [np.median(d) for d in data]

# Create a figure and axes
fig, ax = plt.subplots()

# Plot the boxplots with whiskers and outliers
bp = sns.boxplot(data=data, orient='v', ax=ax, palette=colors, width=0.6, showfliers=False, sym='', medianprops={'visible': False}, flierprops={'visible': False, 'marker': 'None'})
#flierprops=){'marker': 'D', 'markerfacecolor': '#95a5a6', 'markeredgecolor': '#95a5a6', 'markersize': 1}

# remove outliers from data
quartiles1 = np.percentile(data[0], [25, 75])
IQR1 = quartiles1[1] - quartiles1[0]
whiskers1 =[(quartiles1[0] - 1.5 * IQR1), (quartiles1[1] + 1.5 * IQR1)]
print("CryptoAC:" + str(whiskers1))
quartiles2 = np.percentile(data[1], [25, 75])
IQR2 = quartiles2[1] - quartiles2[0]
whiskers2 =[(quartiles2[0] - 1.5 * IQR2), (quartiles2[1] + 1.5 * IQR2)]
print("TLS:" + str(whiskers2))
quartiles3 = np.percentile(data[2], [25, 75])
IQR3 = quartiles3[1] - quartiles3[0]
whiskers3 =[(quartiles3[0] - 1.5 * IQR3), (quartiles3[1] + 3.5 * IQR3)]
print("Baseline:" + str(whiskers3))
filteredData = [
    [x for x in data[0] if whiskers1[0] <= x <= whiskers1[1]],
    [x for x in data[1] if whiskers2[0] <= x <= whiskers2[1]],
    [x for x in data[2] if whiskers3[0] <= x <= whiskers3[1]]
]
sns.stripplot(data=filteredData, orient='v', ax=ax, color='#95a5a6', alpha=0.5, size=1)



# Set the borders of the boxplots to the same color
for i, box in enumerate(bp.artists):
    box.set_edgecolor(colors[i])


# Add markers for the median values
ax.plot(0, medians[0], marker='o', color='#c0392b', markersize=6, zorder=10)
ax.plot(1, medians[1], marker='o', color='#c0392b', markersize=6, zorder=10)
ax.plot(2, medians[2], marker='o', color='#c0392b', markersize=6, zorder=10)


print("Median of CryptoAC: " + str(medians[0]))
print(" ")
print("Median of TLS: " + str(medians[1]))
print(" ")
print("Median of Baseline: " + str(medians[2]))
print(" ")

# Set labels and titlej
ax.set_ylabel('Connection Time (ms)', fontsize=12)
#ax.set_xlabel('Category', fontsize=12)

# Set tick parameters
ax.tick_params(axis='x', labelrotation=0)
ax.tick_params(axis='both', which='major', labelsize=10)

ax.set_xticklabels(['CryptoAC', 'TLS', 'Baseline'], fontsize=12)


# Adjust the plot limits
# ax.set_ylim([-5, 5])

# Remove top and right spines
# sns.despine()

# Tight layout
plt.tight_layout()

# Create a custom legend 
legend_entry_med = Line2D([0], [0], marker='o', color='#c0392b', markersize=4, label='Median')
legend_entry_out = Line2D([0], [0], marker='D', color='#95a5a6', alpha=0.5, markersize=4, label='Data Point')

# Add the custom legend entry to the legend
ax.legend(handles=[legend_entry_med, legend_entry_out])


plt.savefig('EXP_1.pdf', format='pdf', bbox_inches='tight')

# Show the plot
plt.show()

