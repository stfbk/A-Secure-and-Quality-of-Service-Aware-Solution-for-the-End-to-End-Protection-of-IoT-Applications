#!/usr/bin/python

from statistics import mean, median
from scipy import stats
import logging, argparse, csv, os, sys, re, datetime, json
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import pandas as pd

logging.basicConfig(level=getattr(logging, "INFO", None))


rvkPermFile = "./EXP_3_Section_9_3_2_revokePermissionFromRole.csv"

rvkUserUsersFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherUsers.csv"
rvkUserRolesFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherRoles.csv"
rvkUserResourcesFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherResources.csv"


# Check all files exist
logging.info("Checking that all files exist")
if (not os.path.exists(rvkPermFile)):
    logging.error("Results file " + rvkPermFile + " does not exist")
    sys.exit(1)
if (not os.path.exists(rvkPermFile)):
    logging.error("Results file " + rvkUserUsersFile + " does not exist")
    sys.exit(1)
if (not os.path.exists(rvkPermFile)):
    logging.error("Results file " + rvkUserRolesFile + " does not exist")
    sys.exit(1)
if (not os.path.exists(rvkPermFile)):
    logging.error("Results file " + rvkUserResourcesFile + " does not exist")
    sys.exit(1)


# Reading data from files
rvkPermResults = []
logging.info("Parsing file " + rvkPermFile)
with open(rvkPermFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        rvkPermResults.append(float(entry["Time"]))

rvkUserUsersResults = []
logging.info("Parsing file " + rvkUserUsersFile)
with open(rvkUserUsersFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        rvkUserUsersResults.append(float(entry["Time"]))

rvkUserRolesResults = []
logging.info("Parsing file " + rvkUserRolesFile)
with open(rvkUserRolesFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        rvkUserRolesResults.append(float(entry["Time"]))

rvkUserResourcesResults = []
logging.info("Parsing file " + rvkUserResourcesFile)
with open(rvkUserResourcesFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        rvkUserResourcesResults.append(float(entry["Time"]))
















# Plotting
logging.info("Plotting results for " + rvkPermFile)
rvkPermFilePlot = plt.figure(figsize =(12, 8))
plt.xlabel("Number of Other Roles - |{r' : (r', f , -, -, -, -, -, -) ∈ PAc ∧ r' ≠ r }|", fontsize=19)
plt.ylabel('Execution time (ms)', fontsize=19)

# Show the implementation time (upper bound) and slope
plt.plot(
    np.linspace(0, 501, 5), 
    3.500 * np.linspace(0, 501, 5) + 2.43, 
    color='#9b59b6', 
    linewidth=1, 
    linestyle=':',
    label='Implementation Time'
)
plt.text(
    250, 
    1050, 
    '3.500', 
    fontsize=19, 
    color='#9b59b6'
)

# Show the data
plt.plot(
    [i * 5 for i in range(len(rvkPermResults))],
    rvkPermResults,
    'o',
    color='#3498db', 
    markersize=1, 
    #alpha=0.5, 
    #markeredgecolor='black', 
    markeredgewidth=2
)

# Show the regression line
rvkPermX = np.arange(0, 501, 5)
rvkPermY = np.array(rvkPermResults)
rvkPermSlope, rvkPermIntercept = np.polyfit(rvkPermX, rvkPermY, 1)
rvkPermSlopeRegressionLine = rvkPermSlope * rvkPermX + rvkPermIntercept
plt.plot(
    rvkPermX, 
    rvkPermSlopeRegressionLine, 
    linestyle='-',
    color='#2980b9', 
    linewidth=1, 
    label='Regression Line'
)

# Show the regression line slope
rvkPermSlopeText = f'{rvkPermSlope:.3f}'
plt.text(
    250, 
    275, 
    rvkPermSlopeText, 
    fontsize=19, 
    color='#2980b9'
)

# Show the cryptographic time (lower bound)
plt.plot(
    np.linspace(0, 501, 5), 
    0.325 * np.linspace(0, 501, 5) + 0.23, 
    color='#2ecc71', 
    linewidth=1, 
    linestyle='--',
    label='Cryptographic Time'
)
plt.text(
    250, 
    115, 
    '0.325', 
    fontsize=19, 
    color='#2ecc71'
)

plt.tick_params(axis='x', which='both', length=6, width=2, labelsize=15)
plt.tick_params(axis='y', which='both', length=6, width=2, labelsize=15)

plt.xlim(0, 500)
plt.ylim(0, 1800)
plt.legend(fontsize=19, handlelength=5)

plt.savefig('revokePermissionFromRole.pdf', format='pdf', bbox_inches='tight')

plt.show()










logging.info("Plotting results for " + rvkUserUsersFile)
rvkUserUsersFilePlot = plt.figure(figsize =(12, 8))
plt.xlabel("Number of Users - |{u' : (u', r, -, -) ∈ URc ∧ u' ≠ u}|)", fontsize=19)
plt.ylabel('Execution time (ms)', fontsize=19)

# Show the implementation time (upper bound) and slope
plt.plot(
    np.linspace(0, 501, 5), 
    1.292 * np.linspace(0, 501, 5) + 5.708, 
    color='#9b59b6', 
    linewidth=1, 
    linestyle=':',
    label='Implementation Time'
)
plt.text(
    250, 
    400, 
    '1.292', 
    fontsize=19, 
    color='#9b59b6'
)

# Show the data
plt.plot(
    [i * 5 for i in range(len(rvkUserUsersResults))],
    rvkUserUsersResults,
    'o',
    color='#3498db', 
    markersize=1, 
    #alpha=0.5, 
    #markeredgecolor='black', 
    markeredgewidth=2
)

# Show the regression line
rvkUserUsersX = np.arange(0, 501, 5)
rvkUserUsersY = np.array(rvkUserUsersResults)
rvkUserUsersSlope, rvkUserUsersIntercept = np.polyfit(rvkUserUsersX, rvkUserUsersY, 1)
rvkUserUsersSlopeRegressionLine = rvkUserUsersSlope * rvkUserUsersX + rvkUserUsersIntercept
plt.plot(
    rvkUserUsersX, 
    rvkUserUsersSlopeRegressionLine, 
    linestyle='-',
    color='#2980b9', 
    linewidth=1, 
    label='Regression Line'
)

# Show the regression line slope
rvkUserUsersSlopeText = f'{rvkUserUsersSlope:.3f}'
plt.text(
    250, 
    290, 
    rvkUserUsersSlopeText, 
    fontsize=19, 
    color='#2980b9'
)

# Show the cryptographic time (lower bound)
plt.plot(
    np.linspace(0, 501, 5), 
    0.235 * np.linspace(0, 501, 5) + 1.059, 
    color='#2ecc71', 
    linewidth=1, 
    linestyle='--',
    label='Cryptographic Time'
)
plt.text(
    250, 
    80, 
    '0.235', 
    fontsize=19, 
    color='#2ecc71'
)

plt.tick_params(axis='x', which='both', length=6, width=2, labelsize=15)
plt.tick_params(axis='y', which='both', length=6, width=2, labelsize=15)

plt.xlim(0, 500)
plt.ylim(0, 700)
plt.legend(fontsize=19, handlelength=5)

plt.savefig('revokeUserFromRole_Users.pdf', format='pdf', bbox_inches='tight')

plt.show()













logging.info("Plotting results for " + rvkUserRolesFile)
rvkUserRolesFilePlot = plt.figure(figsize =(12, 8))
plt.xlabel("Number of Roles - | { (r', f') : (r', f', -, -, -, -, -, -) ∈ PAc ∧ r' ≠ r } |", fontsize=19)
plt.ylabel('Execution time (ms)', fontsize=19)

# Show the implementation time (upper bound) and slope
plt.plot(
    np.linspace(0, 501, 5), 
    1.649 * np.linspace(0, 501, 5) + 5.351, 
    color='#9b59b6', 
    linewidth=1, 
    linestyle=':',
    label='Implementation Time'
)
plt.text(
    250, 
    500, 
    '1.649', 
    fontsize=19, 
    color='#9b59b6'
)

# Show the data
plt.plot(
    [i * 5 for i in range(len(rvkUserRolesResults))],
    rvkUserRolesResults,
    'o',
    color='#3498db', 
    markersize=1, 
    #alpha=0.5, 
    #markeredgecolor='black', 
    markeredgewidth=2
)

# Show the regression line
rvkUserRolesX = np.arange(0, 501, 5)
rvkUserRolesY = np.array(rvkUserRolesResults)
rvkUserRolesSlope, rvkUserRolesIntercept = np.polyfit(rvkUserRolesX, rvkUserRolesY, 1)
rvkUserRolesSlopeRegressionLine = rvkUserRolesSlope * rvkUserRolesX + rvkUserRolesIntercept
plt.plot(
    rvkUserRolesX, 
    rvkUserRolesSlopeRegressionLine, 
    linestyle='-',
    color='#2980b9', 
    linewidth=1, 
    label='Regression Line'
)

# Show the regression line slope
rvkUserRolesSlopeText = f'{rvkUserRolesSlope:.3f}'
plt.text(
    250, 
    275, 
    rvkUserRolesSlopeText, 
    fontsize=19, 
    color='#2980b9'
)

# Show the cryptographic time (lower bound)
plt.plot(
    np.linspace(0, 501, 5), 
    0.325 * np.linspace(0, 501, 5) + 0.969, 
    color='#2ecc71', 
    linewidth=1, 
    linestyle='--',
    label='Cryptographic Time'
)
plt.text(
    250, 
    105, 
    '0.325', 
    fontsize=19, 
    color='#2ecc71'
)

plt.tick_params(axis='x', which='both', length=6, width=2, labelsize=15)
plt.tick_params(axis='y', which='both', length=6, width=2, labelsize=15)

plt.xlim(0, 500)
plt.ylim(0, 850)
plt.legend(fontsize=19, handlelength=5)

plt.savefig('revokeUserFromRole_Roles.pdf', format='pdf', bbox_inches='tight')

plt.show()














logging.info("Plotting results for " + rvkUserResourcesFile)
rvkUserResourcesFilePlot = plt.figure(figsize =(12, 8))
plt.xlabel("Number of Resources - | {f : (r, f , -, -, -, -, -, -) ∈ PAc } |", fontsize=19)
plt.ylabel('Execution time (ms)', fontsize=19)

# Show the implementation time (upper bound) and slope
plt.plot(
    np.linspace(0, 501, 5), 
    4.866 * np.linspace(0, 501, 5) + 2.134, 
    color='#9b59b6', 
    linewidth=1, 
    linestyle=':',
    label='Implementation Time'
)
plt.text(
    250, 
    1440, 
    '4.866', 
    fontsize=19, 
    color='#9b59b6'
)

# Show the data
plt.plot(
    [i * 5 for i in range(len(rvkUserResourcesResults))],
    rvkUserResourcesResults,
    'o',
    color='#3498db', 
    markersize=1, 
    #alpha=0.5, 
    #markeredgecolor='black', 
    markeredgewidth=2
)

# Show the regression line
rvkUserResourcesX = np.arange(0, 501, 5)
rvkUserResourcesY = np.array(rvkUserResourcesResults)
rvkUserResourcesSlope, rvkUserResourcesIntercept = np.polyfit(rvkUserResourcesX, rvkUserResourcesY, 1)
rvkUserResourcesSlopeRegressionLine = rvkUserResourcesSlope * rvkUserResourcesX + rvkUserResourcesIntercept
plt.plot(
    rvkUserResourcesX, 
    rvkUserResourcesSlopeRegressionLine, 
    linestyle='-',
    color='#2980b9', 
    linewidth=1, 
    label='Regression Line'
)

# Show the regression line slope
rvkUserResourcesSlopeText = f'{rvkUserResourcesSlope:.3f}'
plt.text(
    250, 
    925, 
    rvkUserResourcesSlopeText, 
    fontsize=19, 
    color='#2980b9'
)

# Show the cryptographic time (lower bound)
plt.plot(
    np.linspace(0, 501, 5), 
    0.700 * np.linspace(0, 501, 5) + 0.594, 
    color='#2ecc71', 
    linewidth=1, 
    linestyle='--',
    label='Cryptographic Time'
)
plt.text(
    250, 
    250, 
    '0.700', 
    fontsize=19, 
    color='#2ecc71'
)

plt.tick_params(axis='x', which='both', length=6, width=2, labelsize=15)
plt.tick_params(axis='y', which='both', length=6, width=2, labelsize=15)

plt.xlim(0, 500)
plt.ylim(0, 2500)
plt.legend(fontsize=19, handlelength=5)

plt.savefig('revokeUserFromRole_Resources.pdf', format='pdf', bbox_inches='tight')

plt.show()
