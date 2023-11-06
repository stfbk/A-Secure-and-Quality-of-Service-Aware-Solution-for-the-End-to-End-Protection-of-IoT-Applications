#!/usr/bin/python

from statistics import mean, median
from scipy import stats
import logging, argparse, csv, os, sys, re, datetime, json
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import pandas as pd


rvkPermFile = "./EXP_3_Section_9_3_2_revokePermissionFromRole.csv"
rvkUserUsersFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherUsers.csv"
rvkUserRolesFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherRoles.csv"
rvkUserResourcesFile = "./EXP_3_Section_9_3_1_revokeUserFromRole_OtherResources.csv"

with open(rvkPermFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        print(str((int(entry["OtherRoles"])-1)*5) + "," + str(entry["Time"]))

print(" ")
print(" ")
print(" ")

with open(rvkUserUsersFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        print(str((int(entry["OtherUsers"])-1)*5) + "," + str(entry["Time"]))

print(" ")
print(" ")
print(" ")

with open(rvkUserRolesFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        print(str((int(entry["OtherRoles"])-1)*5) + "," + str(entry["Time"]))

print(" ")
print(" ")
print(" ")

with open(rvkUserResourcesFile, 'r') as file:
    resultsDict = csv.DictReader(file)
    for row in resultsDict:
        entry = dict(row)
        print(str((int(entry["OtherResources"])-1)*5) + "," + str(entry["Time"]))
