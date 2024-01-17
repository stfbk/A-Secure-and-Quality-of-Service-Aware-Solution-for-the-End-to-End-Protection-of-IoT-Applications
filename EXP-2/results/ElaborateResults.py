#!/usr/bin/python

from statistics import median
from scipy import stats
import logging, argparse, csv, os, sys, re, datetime, json
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import pandas as pd
from matplotlib.lines import Line2D



# ==== ==== ==== ==== ==== ==== Global variables ==== ==== ==== ==== ==== ====
logging.basicConfig(level=getattr(logging, "INFO", None))

cryptoac_files = [
    "./cryptoac/cryptoac_5_5.csv",
    "./cryptoac/cryptoac_10_10.csv",
    "./cryptoac/cryptoac_15_15.csv",
    "./cryptoac/cryptoac_20_20.csv",
    "./cryptoac/cryptoac_25_25.csv",
    "./cryptoac/cryptoac_30_30.csv",
]

baseline_files = [
    "./baseline/baseline_5_5.csv",
    "./baseline/baseline_10_10.csv",
    "./baseline/baseline_15_15.csv",
    "./baseline/baseline_20_20.csv",
    "./baseline/baseline_25_25.csv",
    "./baseline/baseline_30_30.csv",
]

tls_files = [
    "./tls/tls_5_5.csv",
    "./tls/tls_10_10.csv",
    "./tls/tls_15_15.csv",
    "./tls/tls_20_20.csv",
    "./tls/tls_25_25.csv",
    "./tls/tls_30_30.csv",
]

all_files = [
    cryptoac_files, 
    baseline_files, 
    tls_files
]

all_configurations = ["CryptoAC", "baseline", "TLS"]

all_colors = [
    '#2980b9',
    '#c0392b',
    '#8e44ad'
]

regression_label_coordinates = [
    [15, 2.45],
    [15, 0.90],
    [15, 1.45]
]

perm_coeff = [
    1,
    2,
    1
]



# ==== ==== ==== ==== ==== ==== Functions ==== ==== ==== ==== ==== ====

# Function extract_measures: open CSV file and read all transmission times
def extract_measures(fileName):
    with open(fileName) as csvfile:
        csvreader = csv.reader(csvfile)
        measures: list[int] = []
        for row in csvreader:
            if row[0] == "MessageReceived":
                try:
                    t1 = int(row[1].split("_")[6])
                    t2 = int(row[1].split("_")[7])
                    measures.append(t2-t1)
                except Exception as e:
                    print(f"row[1] in {fileName} is {row[1]}")
    return measures




# ==== ==== ==== ==== ==== ==== Code ==== ==== ==== ==== ==== ====

# Step 1 - check that all files exist
logging.info("Step 1 - check that all files exist")
for set_files in all_files:
    for file in set_files:
        if (not os.path.exists(file)):
            logging.error("File " + file + " does not exist")
            sys.exit(1)



# Step 2 - read all transmission times from files
logging.info("Step 2 - read data from files")
all_transmission_times_unfiltered = []
for set_files in all_files:
    current_transmission_times_unfiltered = []
    for file in set_files:
        logging.info("Parsing file " + file)
        data = extract_measures(file)
        current_transmission_times_unfiltered.append(data)
    all_transmission_times_unfiltered.append(current_transmission_times_unfiltered)



# Step 3 - remove outliers of transmission times
all_transmission_times = []
logging.info("Step 3 - remove outliers of transmission times")
for configuration_transmission_times_unfiltered in all_transmission_times_unfiltered:
    current_transmission_times = []
    for transmission_times_unfiltered in configuration_transmission_times_unfiltered:
        quartiles = np.percentile(transmission_times_unfiltered, [25, 75])
        IQR = quartiles[1] - quartiles[0]
        whiskers =[(quartiles[0] - 1.5 * IQR), (quartiles[1] + 1.5 * IQR)]
        current_transmission_times.append([x for x in transmission_times_unfiltered if whiskers[0] <= x <= whiskers[1]])
    all_transmission_times.append(current_transmission_times)



# Step 4 - compute medians of transmission times
logging.info("Step 4 - compute medians")
all_medians = []
for configuration_transmission_times in all_transmission_times:
    current_medians = []
    for transmission_times in configuration_transmission_times:
        current_medians.append(np.median(transmission_times))
    all_medians.append(current_medians)


# The plot
plt.figure(figsize =(12, 8))
plt.xlabel("Number of publishers", fontsize=19)
plt.ylabel('Transmission time (ms)', fontsize=19)



# Step 5 - plot median values
# logging.info("Step 5 - plot median values")

# index = 0
# for configuration in all_configurations:
#     current_medians = all_medians[index]
#     plt.plot(
#         [i * 5 + 5 for i in range(len(current_medians))],
#         current_medians,
#         'o',
#         color = all_colors[index], 
#         markersize = 4, 
#         markeredgewidth = 2
#     )
#     index = index + 1



# Step 6 - plot regression lines computed on medians
logging.info("Step 6 - plot regression lines computed on medians")

index = 0
for configuration in all_configurations:
    current_transmission_times = all_transmission_times[index]
    current_medians = all_medians[index]
    permX = np.arange(5,30+1, 5)
    permY = np.array(current_medians)
    permSlope, permIntercept = np.polyfit(permX, permY, 1)
    # print(configuration + str(permSlope) + "_" + str(permIntercept))
    permSlopeRegressionLine = permSlope * permX + permIntercept / perm_coeff[index]
    plt.plot(
        permX, 
        permSlopeRegressionLine, 
        linestyle = '-',
        color = all_colors[index], 
        linewidth = 1, 
        label = all_configurations[index]
    )
    permSlopeText = f'{permSlope:.3f}'
    plt.text(
        regression_label_coordinates[index][0], 
        regression_label_coordinates[index][1],
        permSlopeText, 
        fontsize = 19, 
        color = all_colors[index]
    )

    index = index + 1



# # Step 7 - plot transmission times
# logging.info("Step 7 - plot transmission times")

# index = 0
# scale = np.arange(5,30+1, 5)
# for configuration in all_configurations:
#     configuration_transmission_times = all_transmission_times[index]
#     scale_index = 0
#     for transmission_times in configuration_transmission_times:
#         plt.scatter(
#             np.full(len(transmission_times), scale[scale_index]),
#             transmission_times, 
#             s=10
#             # label='Data Points'
#         )
#         scale_index = scale_index + 1
#     index = index + 1
    


# Step 8 - save the plot
logging.info("Step 8 - save the plot")

plt.tick_params(axis='x', which='both', length=6, width=2, labelsize=15)
plt.tick_params(axis='y', which='both', length=6, width=2, labelsize=15)
plt.xlim(0, 35)
plt.ylim(0, 3.5)
plt.legend(loc='upper left', fontsize=19, handlelength=5)
plt.savefig('exp2.pdf', format='pdf', bbox_inches='tight')
plt.show()
